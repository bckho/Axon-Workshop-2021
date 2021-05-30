package nl.rso.axon.happergame.commandside.aggregates;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rso.axon.happergame.commandside.model.*;
import nl.rso.axon.happergame.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;

import static nl.rso.axon.happergame.commandside.utils.DomainUtilsKt.*;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@NoArgsConstructor
@Slf4j
@Aggregate
public class GameAggregate {

    @AggregateIdentifier
    private String gameId;
    private Game game;


    @CommandHandler
    public GameAggregate(final CreateGameWithGivenLevelCommand command) {
        if (gameId == null) {
            apply(new LevelGameCreatedEvent(command.getGameId(), command.getLevel(), command.getCommandDate()));
        }
    }

    @CommandHandler
    public GameAggregate(final CreateRandomGameWithGivenDifficultyCommand command) {
        if (gameId == null) {
            final Playground playGround = getSimplePlayground(command.getHeight(), command.getWidth(), command.getDifficulty());
            apply(new FixedGameCreatedEvent(command.getGameId(), getSimplePlayground(playGround), command.getWidth(), command.getHeight(), command.getCommandDate()));
        }
    }


    @CommandHandler
    public GameAggregate(final CreateFixedGameCommand command) {
        if (gameId == null) {
            apply(new FixedGameCreatedEvent(command.getGameId(), command.getPlayground(), command.getWidth(), command.getHeight(), command.getCommandDate()));
        }
    }

    @CommandHandler
    public void handle(final StopGameCommand command) {
        if (GameStatus.GAME_ON.equals(game.getGameStatus())) {
            apply(new GameStoppedEvent(this.gameId, command.getCommandDate()));
        }
    }

    /**
     * Opgave 6: Zelf CommandHandlers toevoegen
     */


    @CommandHandler
    public void handle(final MoveHumanCommand command) {
        if (GameStatus.GAME_ON.equals(game.getGameStatus())) {
            final MovePlayableResult<Human, Pushable> result = game.moveHumanInfo(command.getDirection());
            if (result.getMovement() == null) { // Beweging is niet mogelijk
                apply(
                        new HumanBouncedEvent(
                                this.gameId,
                                command.getDirection(),
                                command.getCommandDate()))
                        .andThen(checkGameStatus(command.getCommandDate()));
            } else {

                /**
                 * Opgave 1: Beweging is wel mogelijk
                 */
                apply(
                        new HumanMovedEvent(
                                this.gameId,
                                command.getDirection(),
                                result.getMovement().getFrom(),
                                result.getMovement().getTo(),
                                command.getCommandDate())
                ).andThen(checkGameStatus(command.getCommandDate()));

                /**
                 * Opgave 2
                 */
                for (Movement<Pushable> m : result.getMovedBoxes()) {
                    apply(
                            new BoxMovedEvent(
                                    this.gameId,
                                    command.getDirection(),
                                    m.getFrom(), m.getTo(),
                                    command.getCommandDate())
                    ).andThen(checkGameStatus(command.getCommandDate()));
                }
            }
        }
    }


    /**
     * Opgave 3
     */
    @CommandHandler
    public void handle(final MoveHapperCommand command) {
        if (GameStatus.GAME_ON.equals(game.getGameStatus())) {
            if (this.game.moveHapperPathinfo().getDirection() != null) {
                apply(
                        new HapperMovedEvent(
                                this.gameId,
                                this.game.moveHapperPathinfo().getDirection(),
                                this.game.moveHapperPathinfo().getFrom(),
                                this.game.moveHapperPathinfo().getTo(),
                                command.getCommandDate())
                ).andThen(checkGameStatus(command.getCommandDate()));
            } else {
                this.game.changeStatus(GameStatus.WON);
            }
        }
    }

    /**
     * Opgave 4 en 5: voeg cases toe
     */
    private Runnable checkGameStatus(final Instant occurDate) {
        return () -> {
            final GameStatus gameStatus = this.game.determineGameStatus();
            switch (gameStatus) {
                case LOST_BY_EXCEEDING_MAX_NUMBER_OF_STEPS:
                    apply(new GameLostByMaxNumberOfStepsEvent(this.gameId, occurDate));
                    break;
                case LOST:
                    apply(new GameLostByCatchingEvent(this.gameId, occurDate));
                    break;
                case WON:
                    apply(new GameWonEvent(this.gameId, occurDate));
                    break;
                case GAME_ON:
                    break;
            }
        };
    }

    @EventSourcingHandler
    void on(final LevelGameCreatedEvent event) {
        this.gameId = event.getGameId();
        this.game = getGame(event.getLevel());
    }

    @EventSourcingHandler
    void on(final FixedGameCreatedEvent event) {
        this.gameId = event.getGameId();
        this.game = getFixedGame(event.getSimplePlayground(), event.getWidth(), event.getHeight());
    }

    @EventSourcingHandler
    void on(final GameStatusChangedEvent event) {
        this.game = this.game.changeStatus(event.getGameStatus());
    }

    @EventSourcingHandler
    void on(final GameElementMovedEvent event) {
        this.game = this.game.moveGameElement(event.getGameElement(), event.getFrom(), event.getTo());
    }

    @EventSourcingHandler
    void on(final HumanBouncedEvent event) {
        this.game = this.game.humanBounced();
    }
}
