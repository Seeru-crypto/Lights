package grp.TrafficLight;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        log.info("incomming websocket request with " + message);
        Thread.sleep(1000); // simulated delay

        String res = "Hello, " + message + " " + LocalTime.now();
        return res;

//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message) + "!");
    }

    @EventListener
    public String handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException {
        log.info("New WebSocket connection established.");

        // Automatically send a greeting message to the "/topic/greetings" topic

        // Use SimpMessagingTemplate to send the message
        messagingTemplate.convertAndSend("/topic/greetings", "Welcome to the WebSocket server!");

        new Thread(() -> {
            while (true) {
                try {
                    // Get current time and log it
                    String currentTime = LocalDateTime.now().toString();
                    log.info("Current time: " + currentTime);

                    String res = "Welcome to the WebSocket server! "+ LocalTime.now();

                    messagingTemplate.convertAndSend("/topic/greetings", res);
                    // You would normally send this current time to a WebSocket channel here.
                    // In your case, the WebSocket framework you're using would likely handle this
                    // in the @SendTo annotations.
                    // sendMessage(currentTime); // hypothetical method for sending the message

                    // Sleep for 5 seconds before sending again
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    log.error("Thread interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        // Simulate delay for the original greeting
        Thread.sleep(1000);

        return "tere";
    }
}
