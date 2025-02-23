package grp.TrafficLight;

import grp.TrafficLight.controllers.WebSocketController;
import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

import static grp.TrafficLight.LightColor.RED;
import static grp.TrafficLight.LightColor.YELLOW;
import static grp.TrafficLight.LightDirection.GREENING;
import static grp.TrafficLight.LightDirection.REDDENING;

@Slf4j
public class TrafficWrapper extends Thread {
    private final TrafficLight trafficLight;
    private final WebSocketController webSocketController;

    public TrafficWrapper(TrafficLight trafficLight, WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
        this.trafficLight = trafficLight;
        TrafficLightManager.addTrafficWrapper(this.trafficLight.getLightId(), this);
    }

    public void run() {
        trafficLight.isEnabled();

        while (trafficLight.isEnabled()) {
            if (trafficLight.getLightDirection() == REDDENING) {
                going_red();
            }
            else {
                going_green();
            }
        }
    }

    private void going_red() {
        switch (trafficLight.getLightColor()) {
            case GREEN:
                changeLight(YELLOW, "🚦 YELLOW - SLOW DOWN!", REDDENING);
                break;

            case YELLOW:
                changeLight(RED, "🚦 RED - STOP!", GREENING);
                break;
        }
    }

    private void going_green() {
        switch (trafficLight.getLightColor()) {
            case RED:
                changeLight(YELLOW, "🚦 YELLOW - get ready to go", GREENING);
                break;

            case YELLOW:
                changeLight(LightColor.GREEN, "🚦 GREEN - Go", REDDENING);
                break;
        }
    }

    private void changeLight(LightColor lightColor, String status, LightDirection lightDirection) {
        log(status);
        trafficLight.setLightColor(lightColor);
        trafficLight.setLightDirection(lightDirection);

        TrafficLightBroadcastMessage msg = new TrafficLightBroadcastMessage()
                .setId(trafficLight.getLightId())
                .setTimeSent(LocalTime.now())
                .setDelay(String.valueOf(trafficLight.getDelay()))
                .setName(trafficLight.getLightName())
                .setStatus(status);
        webSocketController.sendTrafficLightUpdate(msg);
        sleepFor(trafficLight.getDelay());
    }

    private void log(String status) {
        // log.info("light_id : " + _trafficLight.getLightId() + " thread_id : " + Thread.currentThread().threadId() + " " + status);
    }

    public void stopThread() {
        trafficLight.setEnabled(false);
        sleepFor(trafficLight.getDelay());
        this.interrupt();
    }

    private void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
