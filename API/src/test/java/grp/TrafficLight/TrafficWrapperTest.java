package grp.TrafficLight;

import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import grp.TrafficLight.repository.TrafficLightRepository;
import grp.TrafficLight.services.TrafficService;
import grp.TrafficLight.services.TrafficWrapper;
import grp.TrafficLight.services.TrafficWrapperManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static grp.TrafficLight.models.enums.LightColor.*;
import static grp.TrafficLight.models.enums.LightDirection.GREENING;
import static grp.TrafficLight.models.enums.LightDirection.REDDENING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrafficWrapperTest {
    private TrafficLight trafficLight;

    @Mock
    private TrafficService trafficService;

    private final Long LIGHT_ID = 123L;
    private final int LIGHT_DELAY = 500;

    @BeforeEach
    public void setUp() {
        trafficLight = new TrafficLight().setLightId(LIGHT_ID).setDelay(LIGHT_DELAY);

        try (MockedStatic<TrafficWrapperManager> mockedStatic = mockStatic(TrafficWrapperManager.class)) {
            // For void methods with MockedStatic, use thenAnswer with null
            mockedStatic.when(() -> TrafficWrapperManager.addTrafficWrapper(anyLong(), any(TrafficWrapper.class)))
                    .thenAnswer(invocation -> null);

            // Create the TrafficWrapper instance
            new TrafficWrapper(trafficLight, trafficService);
        }
    }

    @Test
    void creatingLightWrapper_shouldCallAddTrafficManager() {
        assertNotNull(trafficLight, "TrafficLight mock should not be null");
        assertNotNull(trafficService, "TrafficService mock should not be null");

        try (MockedStatic<TrafficWrapperManager> mockedStatic = mockStatic(TrafficWrapperManager.class)) {

            TrafficWrapper wrapper = new TrafficWrapper(trafficLight, trafficService);

            mockedStatic.verify(() ->
                    TrafficWrapperManager.addTrafficWrapper(eq(LIGHT_ID), any(TrafficWrapper.class)));
        }
    }

    @Test
    void wrapperThread_ThreadShouldLoopColors_andBroadcastMessage() throws InterruptedException {
        trafficLight
                .setLightDirection(GREENING)
                .setLightColor(RED)
                .setDelay(LIGHT_DELAY); // Short delay for testing

        // Configure mock behavior
        doNothing().when(trafficService).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        // Create wrapper and run it in a separate thread
        TrafficWrapper trafficWrapper = new TrafficWrapper(trafficLight, trafficService);
        Thread wrapperThread = new Thread(trafficWrapper);
        wrapperThread.start();

        assertEquals(RED, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
        verify(trafficService, times(1)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        // GREEN - RED
        assertEquals(GREEN, trafficLight.getLightColor());
        assertEquals(REDDENING, trafficLight.getLightDirection());
        verify(trafficService, times(2)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(YELLOW, trafficLight.getLightColor());
        assertEquals(REDDENING, trafficLight.getLightDirection());
        verify(trafficService, times(3)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(RED, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
        verify(trafficService, times(4)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        // RED - GREEN
        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(YELLOW, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
        verify(trafficService, times(5)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(GREEN, trafficLight.getLightColor());
        assertEquals(REDDENING, trafficLight.getLightDirection());
        verify(trafficService, times(6)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));


        // Stop the thread
        trafficLight.setEnabled(false);
        wrapperThread.join(1000); // Wait max 1 second for thread to terminate
    }
}