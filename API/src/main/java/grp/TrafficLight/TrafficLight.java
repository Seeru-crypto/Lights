package grp.TrafficLight;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrafficLight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long lightId;

    private String lightName;

    private boolean isEnabled;

    // in seconds
    private int delay;

    private LightColor lightColor = LightColor.RED;

    private LightDirection lightDirection = LightDirection.GREENING;
}