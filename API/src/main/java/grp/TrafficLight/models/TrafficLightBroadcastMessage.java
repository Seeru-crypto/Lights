package grp.TrafficLight.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TrafficLightBroadcastMessage {
        private Long Id;
        private String status;
        private String name;
        LocalTime timeSent;
}
