package grp.TrafficLight;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrafficWrapper extends Thread {
    private TrafficLight _trafficLight;

    public TrafficWrapper(TrafficLight trafficLight) {
        this._trafficLight = trafficLight;
    }

    public void run() {
        _trafficLight.isEnabled();

        while (_trafficLight.isEnabled()) {
            if (_trafficLight.getLightDirection() == LightDirection.REDDENING) {
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
                log(" 🚦 YELLOW - SLOW DOWN!");
                _trafficLight.setLightColor(LightColor.YELLOW);
                sleepFor(_trafficLight.getDelay());
                break;

            case YELLOW:
                log("🚦 RED - STOP!");
                _trafficLight.setLightColor(LightColor.RED);
                _trafficLight.setLightDirection(LightDirection.GREENING);

                sleepFor(_trafficLight.getDelay());
                break;
        }
    }

    private void going_green() {
        switch (_trafficLight.getLightColor()) {
            case RED:
                log(" 🚦 YELLOW - get ready to go");
                _trafficLight.setLightColor(LightColor.YELLOW);
                sleepFor(_trafficLight.getDelay());
                break;

            case YELLOW:
                log("  GREEN - Go");
                _trafficLight.setLightColor(LightColor.GREEN);
                _trafficLight.setLightDirection(LightDirection.REDDENING);

                sleepFor(_trafficLight.getDelay());
                break;
        }
    }

    public TrafficLight getTrafficLight() {
        return _trafficLight;
    }

    private void log(String status) {
        log.info("light_id : " + _trafficLight.getLightId() + " thread_id : " + Thread.currentThread().threadId() + " " + status);
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
