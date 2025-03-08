package grp.TrafficLight.services;

import grp.TrafficLight.repository.TrafficLightRepository;
import grp.TrafficLight.controllers.WebSocketController;
import grp.TrafficLight.models.TrafficLight;
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
    protected final TrafficLightRepository trafficLightRepository;
    private final WebSocketController webSocketController;

    public TrafficLight createNewTrafficLight(String name, int delay) {

        TrafficLight trafficLight = new TrafficLight()
                .setLightName(name)
                .setDelay(delay)
                .setEnabled(true);

        trafficLightRepository.save(trafficLight);

        TrafficWrapper trafficWrapper = new TrafficWrapper(trafficLight, webSocketController);
        trafficWrapper.start();
        log.info("created new wrapper with ID " + trafficWrapper.threadId());
        return trafficLight;
    }

    public List<TrafficLight> getTrafficLights() {
        return trafficLightRepository.findAll();
    }

    public void deleteLight(long lightId) {
        TrafficLight trafficLight1 = trafficLightRepository.findById(lightId).orElseThrow();

        TrafficWrapper wrapper = TrafficLightManager.getTrafficWrapper(lightId);

        if (wrapper == null) {
            throw new NullPointerException("wrapper is null");
        }

        wrapper.stopThread();

        TrafficLightManager.removeTrafficWrapper(lightId);

        trafficLight1.setEnabled(false);
        trafficLightRepository.save(trafficLight1);

    }
}

