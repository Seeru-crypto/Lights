package grp.TrafficLight.controllers;

import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.time.LocalTime;

@Slf4j
@Controller
public class GreetingController {

    private SimpMessagingTemplate _messagingTemplate;

    public GreetingController(SimpMessagingTemplate messagingTemplate) {
        this._messagingTemplate =  messagingTemplate;
    }

    public void sendTrafficLightUpdate(TrafficLightBroadcastMessage message) {
        _messagingTemplate.convertAndSend("/topic/greetings", message);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        log.info("incomming websocket request with " + message);
        Thread.sleep(1000); // simulated delay

        String res = "Hello, " + message + " " + LocalTime.now();
        return res;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException {
        log.info("New WebSocket connection established.");
        _messagingTemplate.convertAndSend("/topic/greetings", "Welcome to the WebSocket server!");
    }
}
