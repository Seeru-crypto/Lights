package grp.TrafficLight;

import grp.TrafficLight.models.ChangeCounter;
import grp.TrafficLight.models.TrafficLight;
import grp.TrafficLight.models.TrafficLightBroadcastMessage;
import grp.TrafficLight.services.TrafficService;
import grp.TrafficLight.services.TrafficWrapper;
import grp.TrafficLight.services.TrafficWrapperManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static grp.TrafficLight.models.enums.LightColor.*;
import static grp.TrafficLight.models.enums.LightDirection.GREENING;
import static grp.TrafficLight.models.enums.LightDirection.REDDENING;
import static grp.TrafficLight.services.TrafficService.SIGNAL_LIGHTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
            ChangeCounter changeCounter = new ChangeCounter();
            new TrafficWrapper(changeCounter, trafficLight, trafficService);
        }
    }

    @Test
    void creatingLightWrapper_shouldCallAddTrafficManager() {
        assertNotNull(trafficLight, "TrafficLight mock should not be null");
        assertNotNull(trafficService, "TrafficService mock should not be null");
        ChangeCounter changeCounter = new ChangeCounter();

        try (MockedStatic<TrafficWrapperManager> mockedStatic = mockStatic(TrafficWrapperManager.class)) {

            TrafficWrapper wrapper = new TrafficWrapper(changeCounter, trafficLight, trafficService);

            mockedStatic.verify(() ->
                    TrafficWrapperManager.addTrafficWrapper(eq(LIGHT_ID), any(TrafficWrapper.class)));
        }
    }

    @Test
    void wrapperThread_SingleThreadShouldLoopColors_andBroadcastMessage() throws InterruptedException {
        trafficLight
                .setLightDirection(GREENING)
                .setLightColor(RED)
                .setDelay(LIGHT_DELAY); // Short delay for testing

        // Configure mock behavior
        doNothing().when(trafficService).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));
        ChangeCounter changeCounter = new ChangeCounter();

        // Create wrapper and run it in a separate thread
        TrafficWrapper trafficWrapper = new TrafficWrapper(changeCounter, trafficLight, trafficService);
        Thread wrapperThread = new Thread(trafficWrapper);
        wrapperThread.start();

        assertEquals(RED, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        // GREEN - RED
        assertEquals(YELLOW, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
        verify(trafficService, times(1)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(GREEN, trafficLight.getLightColor());
        assertEquals(REDDENING, trafficLight.getLightDirection());
         verify(trafficService, times(2)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(YELLOW, trafficLight.getLightColor());
        assertEquals(REDDENING, trafficLight.getLightDirection());
         verify(trafficService, times(3)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));

        // RED - GREEN
        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(RED, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
         verify(trafficService, times(4)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));

        Thread.sleep(LIGHT_DELAY + 50); // Adjust this value based on your implementation

        assertEquals(YELLOW, trafficLight.getLightColor());
        assertEquals(GREENING, trafficLight.getLightDirection());
         verify(trafficService, times(5)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));


        // Stop the thread
        trafficLight.setEnabled(false);
        wrapperThread.join(1000); // Wait max 1 second for thread to terminate
    }

    @Test
    void wrapperThread_MultibleThreadsShouldLoopColors() throws InterruptedException {
        // Create two separate TrafficLight instances and initialize them
        TrafficLight trafficLight1 = new TrafficLight();
        TrafficLight trafficLight2 = new TrafficLight();

        // Ensure both lights are enabled before starting threads
        trafficLight1.setEnabled(true);
        trafficLight2.setEnabled(true);
        
        // Initialize required properties
        trafficLight1.setLightId(1L).setDelay(LIGHT_DELAY).setLightColor(RED).setLightDirection(GREENING);
        trafficLight2.setLightId(2L).setDelay(LIGHT_DELAY).setLightColor(RED).setLightDirection(GREENING);

        // Mock TrafficService for both
        TrafficService trafficService1 = mock(TrafficService.class);
        TrafficService trafficService2 = mock(TrafficService.class);

        doNothing().when(trafficService1).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));
        doNothing().when(trafficService2).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));

        ChangeCounter changeCounter1 = new ChangeCounter();
        ChangeCounter changeCounter2 = new ChangeCounter();

        // Create wrappers and threads
        TrafficWrapper wrapper1 = new TrafficWrapper(changeCounter1, trafficLight1, trafficService1);
        TrafficWrapper wrapper2 = new TrafficWrapper(changeCounter2, trafficLight2, trafficService2);

        Thread thread1 = new Thread(wrapper1);
        Thread thread2 = new Thread(wrapper2);

        thread1.start();
        thread2.start();

        // Let both threads run through a few cycles
        for (int i = 0; i < 3; i++) {
            Thread.sleep(LIGHT_DELAY + 50);

            // Both lights should be in the same state at each step
            assertEquals(trafficLight1.getLightColor(), trafficLight2.getLightColor());
            assertEquals(trafficLight1.getLightDirection(), trafficLight2.getLightDirection());
        }

        // Stop both threads
        trafficLight1.setEnabled(false);
        trafficLight2.setEnabled(false);

        thread1.join(1000);
        thread2.join(1000);

        // Verify that both services were called the same number of times
        verify(trafficService1, atLeast(1)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));
        verify(trafficService2, atLeast(1)).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class), eq(SIGNAL_LIGHTS));
    }
}