package grp.TrafficLight.repository;

import grp.TrafficLight.models.TrafficLight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficLightRepository extends JpaRepository<TrafficLight, Long> {}
