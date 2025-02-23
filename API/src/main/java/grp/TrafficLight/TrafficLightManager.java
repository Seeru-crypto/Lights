package grp.TrafficLight;

import java.util.concurrent.ConcurrentHashMap;

public class TrafficLightManager {
    private static final ConcurrentHashMap<Long, TrafficWrapper> trafficMap = new ConcurrentHashMap<>();

    public static void addTrafficWrapper(Long trafficLightId, TrafficWrapper wrapper) {
//        trafficMap.put(trafficLight, wrapper);
        trafficMap.put(trafficLightId, wrapper);
    }

    public static TrafficWrapper getTrafficWrapper(Long trafficLightId) {
        return trafficMap.get(trafficLightId);
    }

    public static void removeTrafficWrapper(Long trafficLightId) {
        trafficMap.remove(trafficLightId);
    }
}
