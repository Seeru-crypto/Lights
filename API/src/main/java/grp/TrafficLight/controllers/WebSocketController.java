package grp.TrafficLight.controllers;

import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.time.LocalTime;

import static grp.TrafficLight.models.enums.LightColor.RED;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class WebSocketController {
    @SendTo("/get/lights")
    @MessageMapping("/hello")
    public TrafficLightBroadcastMessage greeting(String message) throws Exception {
        log.info("incomming websocket request with " + message);
        Thread.sleep(1000); // simulated delay

        TrafficLightBroadcastMessage res = new TrafficLightBroadcastMessage()
                .setId(0L)
                .setStatus("TEST")
                .setName(message)
                .setDelay("delay")
                .setLightColor(RED)
                .setTimeSent(LocalTime.now());
        return res;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException {
        log.info("New WebSocket connection established.");
    }
}
