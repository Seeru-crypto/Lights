package grp.TrafficLight;

import grp.TrafficLight.controllers.WebSocketController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class TrafficService {
    protected final Repository repository;
    private final WebSocketController webSocketController;

    public TrafficLight createNewTrafficLight(String name, int delay) {

        TrafficLight trafficLight = new TrafficLight()
                .setLightName(name)
                .setDelay(delay)
                .setEnabled(true);

        repository.save(trafficLight);

        TrafficWrapper trafficWrapper = new TrafficWrapper(trafficLight, webSocketController);
        trafficWrapper.start();
        log.info("created new wrapper with ID " + trafficWrapper.threadId());
        return trafficLight;
    }

    public List<TrafficLight> getTrafficLights() {
        return repository.findAll();
    }

    public void deleteThread(long threadId) {
        Thread thread = Thread.currentThread();
        boolean isSuccessful = false;
        TrafficLight trafficLight = null;

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getId() == threadId) {

                if (t instanceof TrafficWrapper) {
                    System.out.println("Stopping thread with ID: " + threadId);
                    trafficLight = ((TrafficWrapper) t).getTrafficLight();
                    ((TrafficWrapper) t).stopThread(); // Call stop method
                    isSuccessful = true;

                }
            }
        }

        if (isSuccessful && trafficLight != null) {
            log.info("success");
            trafficLight.setEnabled(false);
            repository.save(trafficLight);

        } else {
            log.info("boo");
        }
    }

    public void deleteLight(long lightId) {
        TrafficLight trafficLight1 = repository.findById(lightId).orElseThrow();

        TrafficWrapper wrapper = TrafficLightManager.getTrafficWrapper(lightId);

        if (wrapper == null) {
            throw new NullPointerException("wrapper is null");
        }

        wrapper.stopThread();

        trafficLight1.setEnabled(false);
        repository.save(trafficLight1);

    }
}

