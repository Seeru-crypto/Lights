package grp.TrafficLight.services;

import java.util.concurrent.ConcurrentHashMap;

public class TrafficWrapperManager {
    private static final ConcurrentHashMap<Long, TrafficWrapper> trafficMap = new ConcurrentHashMap<>();

    public static void addTrafficWrapper(Long trafficLightId, TrafficWrapper wrapper) {
        trafficMap.put(trafficLightId, wrapper);
    }

    public static TrafficWrapper getTrafficWrapper(Long trafficLightId) {
        return trafficMap.get(trafficLightId);
    }

    public static void removeTrafficWrapper(Long trafficLightId) {
        trafficMap.remove(trafficLightId);
    }
}
