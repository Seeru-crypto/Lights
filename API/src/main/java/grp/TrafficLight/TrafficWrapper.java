package grp.TrafficLight;

import grp.TrafficLight.controllers.WebSocketController;
import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

import static grp.TrafficLight.LightColor.RED;
import static grp.TrafficLight.LightColor.YELLOW;
import static grp.TrafficLight.LightDirection.GREENING;
import static grp.TrafficLight.LightDirection.REDDENING;

@Slf4j
public class TrafficWrapper extends Thread {
    private TrafficLight _trafficLight;
    private final WebSocketController _greetingControllre;

    public TrafficWrapper(TrafficLight trafficLight, WebSocketController webSocketController) {
        this._greetingControllre = webSocketController;
        this._trafficLight = trafficLight;
        TrafficLightManager.addTrafficWrapper(_trafficLight.getLightId(), this);
    }

    public void run() {
        _trafficLight.isEnabled();

        while (_trafficLight.isEnabled()) {
            if (_trafficLight.getLightDirection() == REDDENING) {
                going_red();
            }
            else {
                going_green();
            }
        }
    }


    private void going_red() {
        switch (_trafficLight.getLightColor()) {
            case GREEN:
                changeLight(YELLOW, "🚦 YELLOW - SLOW DOWN!", REDDENING);
                break;

            case YELLOW:
                changeLight(RED, "🚦 RED - STOP!", GREENING);
                break;
        }
    }

    private void going_green() {
        switch (_trafficLight.getLightColor()) {
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
        TrafficLightBroadcastMessage msg = new TrafficLightBroadcastMessage()
                .setId(_trafficLight.getLightId())
                .setTimeSent(LocalTime.now())
                .setName(_trafficLight.getLightName())
                .setStatus(status);
        _greetingControllre.sendTrafficLightUpdate(msg);
        _trafficLight.setLightColor(lightColor);
        _trafficLight.setLightDirection(lightDirection);

        sleepFor(_trafficLight.getDelay());
    }

    public TrafficLight getTrafficLight() {
        return _trafficLight;
    }

    private void log(String status) {
        // log.info("light_id : " + _trafficLight.getLightId() + " thread_id : " + Thread.currentThread().threadId() + " " + status);
    }

    public void stopThread() {
        _trafficLight.setEnabled(false);
        sleepFor(_trafficLight.getDelay());
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
