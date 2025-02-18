package grp.TrafficLight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class TrafficService {
    protected final Repository repository;

    public TrafficLight createNewTrafficLight(String name, int delay) {

        TrafficLight trafficLight = new TrafficLight()
                .setName(name)
                .setDelay(delay)
                .setEnabled(true)
                ;

        return repository.save(trafficLight);
    }

    public List<TrafficLight> getTrafficLights() {
        return repository.findAll();
    }

}
