package grp.TrafficLight.models;

import grp.TrafficLight.models.enums.LightColor;
import grp.TrafficLight.services.TrafficService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static grp.TrafficLight.services.TrafficService.SIGNAL_COUNTER;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCounter {
    private Long latestChangeLightId;

    private LightColor changedTo = LightColor.RED;

    private int numberOfChanges = 0;

    private List<ListEntity> history = new ArrayList<>();

    public synchronized void update(long lightId, LightColor newColor, TrafficService trafficService) {
        try {
            wait(100);
            numberOfChanges++;
            latestChangeLightId = lightId;
            changedTo=newColor;
            history.add(new ListEntity()
                    .setLightColor(newColor)
                    .setLightId(lightId));
            TrafficLightBroadcastMessage msg = new TrafficLightBroadcastMessage().setId(lightId).setLightColor(newColor);
            trafficService.sendTrafficLightUpdate(msg, SIGNAL_COUNTER);
        }
        catch (InterruptedException e) {
            log.error("ChangeCounter exception with ", e);
        }
        log.info("CHANGE LOG light id " + latestChangeLightId +" nr changes " + numberOfChanges + " color "+ newColor );
    }

    @Setter
    @Getter
    private static class ListEntity {
        private LightColor lightColor;
        private long lightId;
    }

}