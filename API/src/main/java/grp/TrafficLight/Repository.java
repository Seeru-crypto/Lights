package grp.TrafficLight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repository extends JpaRepository<TrafficLight, Long> {

    List<TrafficLight> findAll();

}
