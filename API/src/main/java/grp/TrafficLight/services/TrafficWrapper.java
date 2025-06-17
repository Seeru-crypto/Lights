package grp.TrafficLight.services;

import grp.TrafficLight.models.ChangeCounter;
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
import static grp.TrafficLight.services.TrafficService.SIGNAL_LIGHTS;

@Slf4j
public class TrafficWrapper extends Thread {
    private final TrafficLight trafficLight;
    private final TrafficService trafficService;
    private ChangeCounter changeCounter;

    public TrafficWrapper(ChangeCounter changeCounter, TrafficLight trafficLight, TrafficService trafficService) {
        this.trafficLight = trafficLight;
        this.trafficService = trafficService;
        this.changeCounter = changeCounter;
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

        trafficService.sendTrafficLightUpdate(msg, SIGNAL_LIGHTS);
        log(status);
        changeCounter.update(trafficLight.getLightId(), lightColor, trafficService);
        sleepFor(trafficLight.getDelay());
    }

    private void log(String status) {
         // log.info("light_id : " + trafficLight.getLightId() + " thread_id : " + Thread.currentThread().threadId() + " " + status);
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

// Proposal for a refactor
//public class TrafficWrapper implements Runnable {
//    private final TrafficLight trafficLight;
//    private final TrafficService trafficService;
//
//    public TrafficWrapper(TrafficLight trafficLight, TrafficService trafficService) {
//        this.trafficLight = trafficLight;
//        this.trafficService = trafficService;
//    }
//
//    @Override
//    public void run() {
//        while (trafficLight.isEnabled()) {
//            processLightChange();
//            sleep(trafficLight.getDelay());
//        }
//    }
//
//    // Make this public (or package-private) for testing
//    void processLightChange() {
//        // Your light changing logic here
//        // ...
//        trafficService.sendTrafficLightUpdate(new TrafficLightBroadcastMessage(trafficLight));
//    }
//
//    private void sleep(long delay) {
//        try {
//            Thread.sleep(delay);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
