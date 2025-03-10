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

    @Mock
    private TrafficLightRepository trafficLightRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private final Long LIGHT_ID = 123L;
    private final int LIGHT_DELAY = 3000;

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
    void wrapperThread_shouldStartFromRedAndGoGreen() throws InterruptedException {
        // Arrange
        trafficLight
                .setLightColor(GREEN)
                .setLightDirection(REDDENING)
                .setDelay(LIGHT_DELAY);
        TrafficWrapper trafficWrapper = new TrafficWrapper(trafficLight, trafficService);
        trafficWrapper.run();

        Thread.sleep(LIGHT_DELAY + 100); // Convert seconds to milliseconds


        // Act - simulate one cycle through the state machine

        // Assert
        // Verify that the traffic light color changed to YELLOW during the transition
        assertEquals(YELLOW, trafficLight.getLightColor(), "Light should transition to YELLOW when going GREEN from RED");
        assertEquals(REDDENING, trafficLight.getLightDirection(), "Direction should remain GREENING during transition");

        // Verify that the service was called to broadcast the message
        verify(trafficService).sendTrafficLightUpdate(any(TrafficLightBroadcastMessage.class));
    }

//    @Test
//    public void testGoingRedFromGreen() throws InterruptedException {
//        // Setup
//        when(trafficLight.isEnabled()).thenReturn(true, true, false);
//        when(trafficLight.getLightDirection()).thenReturn(REDDENING);
//        when(trafficLight.getLightColor()).thenReturn(GREEN);
//        when(trafficLight.getDelay()).thenReturn(100);
//        when(trafficLight.getLightName()).thenReturn("Test Light");
//
//        // Use try-with-resources to handle mocking static methods
//        try (MockedStatic<Thread> threadMock = mockStatic(Thread.class);
//             MockedStatic<LocalTime> timeMock = mockStatic(LocalTime.class)) {
//
//            // Mock the static methods
//            LocalTime fixedTime = LocalTime.of(12, 0);
//            timeMock.when(LocalTime::now).thenReturn(fixedTime);
//
//            // Start the thread and give it time to execute
//            trafficWrapper.run();
//
//            // Verify light color was changed to YELLOW
//            verify(trafficLight).setLightColor(YELLOW);
//            verify(trafficLight).setLightDirection(REDDENING);
//
//            // Verify the message was sent
//            ArgumentCaptor<TrafficLightBroadcastMessage> messageCaptor =
//                    ArgumentCaptor.forClass(TrafficLightBroadcastMessage.class);
//            verify(trafficService).sendTrafficLightUpdate(messageCaptor.capture());
//
//            TrafficLightBroadcastMessage sentMessage = messageCaptor.getValue();
//            assertEquals(1L, sentMessage.getId());
//            assertEquals(YELLOW, sentMessage.getLightColor());
//            assertEquals("🚦 YELLOW - SLOW DOWN!", sentMessage.getStatus());
//            assertEquals(fixedTime, sentMessage.getTimeSent());
//        }
//    }
}
