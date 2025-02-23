package grp.TrafficLight.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TrafficLightBroadcastMessage {
        private Long id;
        private String status;
        private String name;
        private String delay;
        LocalTime timeSent;
}
