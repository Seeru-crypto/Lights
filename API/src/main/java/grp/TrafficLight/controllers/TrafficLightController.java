package grp.TrafficLight.controllers;

import grp.TrafficLight.TrafficLight;
import grp.TrafficLight.TrafficService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/lights")
public class TrafficLightController {
    private final TrafficService trafficService;

    @GetMapping
    public ResponseEntity<List<TrafficLight>> findAll() {
        log.info("REST request to findAll lights");
        return ResponseEntity.ok(trafficService.getTrafficLights());
    }

    @PostMapping
    public ResponseEntity<TrafficLight> post(
            @RequestParam(name = "delay") int delay,
            @RequestParam(name = "name") String name
    ) {

        log.info("REST request to create light with delay: "+  delay + " and name "+name);
        return ResponseEntity.ok(trafficService.createNewTrafficLight(name, delay ));
    }

    @DeleteMapping
    public void delete(
            @RequestParam(name = "id") int threadId
    ) {

        log.info("REST request to create light ", threadId);
        trafficService.deleteThread(threadId);
    }
}


