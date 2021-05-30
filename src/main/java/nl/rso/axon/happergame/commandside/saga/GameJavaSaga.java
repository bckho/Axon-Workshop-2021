package nl.rso.axon.happergame.commandside.saga;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rso.axon.happergame.coreapi.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.scheduling.EventScheduler;
import org.axonframework.eventhandling.scheduling.ScheduleToken;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Saga
@NoArgsConstructor
@Slf4j
public class GameJavaSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient EventScheduler eventScheduler;

    private ScheduleToken scheduleToken;

    private String gameId;

    public GameJavaSaga(final CommandGateway commandGateway, final EventScheduler eventScheduler) {
        this.commandGateway = commandGateway;
        this.eventScheduler = eventScheduler;
    }

    // Opgave 3. Uncomment alle regels die in commentaar staan.

    @StartSaga
    @SagaEventHandler(associationProperty = "gameId")
    public void on(LevelGameCreatedEvent event) {
        this.gameId = event.getGameId();
         scheduleToken = eventScheduler.schedule(Duration.of(0L, ChronoUnit.SECONDS), new HapperReadyToMoveEvent(gameId));
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "gameId")
    public void on(FixedGameCreatedEvent event) {
        this.gameId = event.getGameId();
        scheduleToken = eventScheduler.schedule(Duration.of(0L, ChronoUnit.SECONDS), new HapperReadyToMoveEvent(gameId));
    }

    @SagaEventHandler(associationProperty = "gameId")
    public void on(final HapperReadyToMoveEvent event) {
         commandGateway.sendAndWait(new MoveHapperCommand(event.getGameId(), Instant.now()));
         scheduleToken = eventScheduler.schedule(Duration.of(1L, ChronoUnit.SECONDS), new HapperReadyToMoveEvent(gameId));
    }

    @SagaEventHandler(associationProperty = "gameId")
    public void on(final GamePausedEvent event) {
        eventScheduler.cancelSchedule(scheduleToken);
    }

    @SagaEventHandler(associationProperty = "gameId")
    public void on(final GameRestartedEvent event) {
         scheduleToken = eventScheduler.schedule(Duration.of(0L, ChronoUnit.SECONDS), new HapperReadyToMoveEvent(gameId));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "gameId")
    public void on(final GameEndedEvent event) {
        eventScheduler.cancelSchedule(scheduleToken);
    }
}