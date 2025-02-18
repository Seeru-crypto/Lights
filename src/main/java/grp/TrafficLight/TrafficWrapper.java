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
            switch (_trafficLight.getLightColor()) {
                case RED:
                    log(" 🚦 RED - Stop");
                    sleepFor(_trafficLight.getDelay());
                    _trafficLight.setLightColor(LightColor.GREEN);
                    break;

                case GREEN:
                    log(" 🚦 GREEN - Go");
                    sleepFor(_trafficLight.getDelay());
                    _trafficLight.setLightColor(LightColor.YELLOW);
                    break;

                case YELLOW:
                    log(" 🚦 YELLOW - Slow down");
                    sleepFor(_trafficLight.getDelay());
                    _trafficLight.setLightColor(LightColor.RED);
                    break;
            }
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
