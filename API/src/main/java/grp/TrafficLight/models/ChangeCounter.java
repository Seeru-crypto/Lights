package grp.TrafficLight.models;

import grp.TrafficLight.models.enums.LightColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCounter {

    private Long latestChangeLightId;

    private LightColor changedTo = LightColor.RED;

    private int numberOfChanges = 0;

    public synchronized void update(long lightId, LightColor newColor) {
        try {
            wait(100);
            numberOfChanges++;
            latestChangeLightId = lightId;
            changedTo=newColor;
        }
        catch (InterruptedException e) {
            log.error("ChangeCounter exception with ", e);

        }
        log.info("CHANGE LOG light id " + latestChangeLightId +" nr changes " + numberOfChanges + " color "+ newColor );
    }

}