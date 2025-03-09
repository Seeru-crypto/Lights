package grp.TrafficLight.services;

import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import grp.TrafficLight.models.enums.LightColor;
import grp.TrafficLight.models.enums.LightDirection;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

import static grp.TrafficLight.models.enums.LightColor.RED;
import static grp.TrafficLight.models.enums.LightColor.YELLOW;
import static grp.TrafficLight.models.enums.LightDirection.GREENING;
import static grp.TrafficLight.models.enums.LightDirection.REDDENING;

@Slf4j
public class TrafficWrapper extends Thread {
    private final TrafficLight trafficLight;
    private final TrafficService trafficService;

    public TrafficWrapper(TrafficLight trafficLight, TrafficService trafficService) {
        this.trafficLight = trafficLight;
        this.trafficService = trafficService;
        TrafficWrapperManager.addTrafficWrapper(this.trafficLight.getLightId(), this);
    }

    public void run() {
        trafficLight.isEnabled();

        while (trafficLight.isEnabled()) {

            if (trafficLight.getLightDirection() == REDDENING) {
                goingRed();
            } else {
                goingGreen();
            }
        }
    }

    private void goingRed() {
        switch (trafficLight.getLightColor()) {
            case GREEN -> changeLight(YELLOW, "🚦 YELLOW - SLOW DOWN!", REDDENING);
            case YELLOW -> changeLight(RED, "🚦 RED - STOP!", GREENING);
            default -> log.info("Error occured with light " + trafficLight.getLightId() );
        }
    }

    private void goingGreen() {
        switch (trafficLight.getLightColor()) {
            case RED -> changeLight(YELLOW, "🚦 YELLOW - get ready to go", GREENING);
            case YELLOW -> changeLight(LightColor.GREEN, "🚦 GREEN - Go", REDDENING);
            default -> log.info("Error occured with light " + trafficLight.getLightId() );
        }
    }

    private void changeLight(LightColor lightColor, String status, LightDirection lightDirection) {
        trafficLight.setLightColor(lightColor);
        trafficLight.setLightDirection(lightDirection);

        TrafficLightBroadcastMessage msg = new TrafficLightBroadcastMessage()
                .setId(trafficLight.getLightId())
                .setTimeSent(LocalTime.now())
                .setDelay(String.valueOf(trafficLight.getDelay()))
                .setName(trafficLight.getLightName())
                .setLightColor(lightColor)
                .setStatus(status);

        trafficService.sendTrafficLightUpdate(msg);
        sleepFor(trafficLight.getDelay());
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
