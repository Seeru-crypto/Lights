package grp.TrafficLight.controllers;

import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.services.TrafficService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RequestMapping(path = "/lights")
public class TrafficLightController {
    private final TrafficService trafficService;

    @GetMapping
    public ResponseEntity<List<TrafficLight>> findAll() {
        log.info("REST request to findAll lights");
        return ResponseEntity.ok(trafficService.getTrafficLights());
    }
    // TOOO: update to use proper POST body
@PostMapping
public ResponseEntity<Void> post(
    @RequestParam(name = "delay") int delay,
    @RequestParam(name = "name") String name
) throws URISyntaxException {
    log.info("REST request to create light with delay: " + delay + " and name " + name);
    var id = trafficService.createNewTrafficLight(name, delay).getLightId();
    URI location = new URI(id.toString());
    return ResponseEntity.created(location).build();
}

    @DeleteMapping(("/{id}"))
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("REST request to delete light  with id "+ id);
        trafficService.deleteLight(id);
        return ResponseEntity.noContent().build();
    }
}
