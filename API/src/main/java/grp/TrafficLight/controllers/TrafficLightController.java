package grp.TrafficLight.controllers;

import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.models.TrafficLightDto;
import grp.TrafficLight.services.TrafficService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Void> post(@Valid @RequestBody TrafficLightDto dto) {
        log.info("REST request to create light with delay: " + dto.getDelay() + " and name " + dto.getName());
        long lightId = trafficService.createNewTrafficLight(dto).getLightId();
        URI lights = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{lightId}")
                .buildAndExpand(lightId)
                .toUri();
        return ResponseEntity.created(lights).build();
    }

    @DeleteMapping(("/{id}"))
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("REST request to delete light  with id " + id);
        trafficService.deleteLight(id);
        return ResponseEntity.noContent().build();
    }
}
