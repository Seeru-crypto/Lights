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
public class WebSocketController {

    private SimpMessagingTemplate _messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this._messagingTemplate =  messagingTemplate;
    }

    public void sendTrafficLightUpdate(TrafficLightBroadcastMessage message) {
        _messagingTemplate.convertAndSend("/get/lights", message);
    }

    @MessageMapping("/hello")
    @SendTo("/get/lights")
    public TrafficLightBroadcastMessage greeting(String message) throws Exception {
        log.info("incomming websocket request with " + message);
        Thread.sleep(1000); // simulated delay

        TrafficLightBroadcastMessage res = new TrafficLightBroadcastMessage()
                .setId(0L)
                .setStatus("TEST")
                .setName(message)
                .setTimeSent(LocalTime.now());
        return res;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException {
        log.info("New WebSocket connection established.");
    }
}
