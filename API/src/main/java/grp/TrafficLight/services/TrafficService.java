package grp.TrafficLight.services;

import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import grp.TrafficLight.models.TrafficLightDto;
import grp.TrafficLight.repository.TrafficLightRepository;
import grp.TrafficLight.models.TrafficLight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class TrafficService {
    protected final TrafficLightRepository trafficLightRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public TrafficLight createNewTrafficLight(TrafficLightDto dto) {

        TrafficLight trafficLight = new TrafficLight()
                .setLightName(dto.getName())
                .setDelay(dto.getDelay());

        trafficLightRepository.save(trafficLight);

        TrafficWrapper trafficWrapper = new TrafficWrapper(trafficLight, this);
        trafficWrapper.start();
        log.info("created new wrapper with ID " + trafficWrapper.threadId());
        return trafficLight;
    }

    public List<TrafficLight> getTrafficLights() {
        return trafficLightRepository.findAll();
    }

    public void deleteLight(long lightId) {
        TrafficLight trafficLight1 = trafficLightRepository.findById(lightId).orElseThrow();

        TrafficWrapper wrapper = TrafficWrapperManager.getTrafficWrapper(lightId);

        if (wrapper == null) {
            throw new NullPointerException("wrapper is null");
        }

        wrapper.stopThread();

        TrafficWrapperManager.removeTrafficWrapper(lightId);

        trafficLight1.setEnabled(false);
        trafficLightRepository.save(trafficLight1);
    }

    public void sendTrafficLightUpdate(TrafficLightBroadcastMessage message) {
        messagingTemplate.convertAndSend("/get/lights", message);
    }
}
