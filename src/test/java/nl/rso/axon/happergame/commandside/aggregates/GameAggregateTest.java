package nl.rso.axon.happergame.commandside.aggregates;

import nl.rso.axon.happergame.commandside.model.Coordinate;
import nl.rso.axon.happergame.commandside.model.Direction;
import nl.rso.axon.happergame.coreapi.*;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static nl.rso.axon.happergame.testplaygrounds.TestPlaygroundsKt.*;

public class GameAggregateTest {

    private FixtureConfiguration fixture;
    private String gameId;


    @Before
    public void setUp() {
        fixture = new AggregateTestFixture(GameAggregate.class);
        fixture.setReportIllegalStateChange(false);
        this.gameId = "00001";
    }

    @Test
    public void create_fixed_game_command_should_emit_fixed_game_created_event() {

        final Instant now = Instant.now();

        final CreateFixedGameCommand command =
                new CreateFixedGameCommand(gameId, now, getHumanSimpleMove(), 2, 2);


        fixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(new FixedGameCreatedEvent(gameId, command.getPlayground(), 2, 2, now));

    }


    @Test
    public void move_human_command_should_emit_human_moved_event_when_move_in_direction_is_possible() {

        final Instant now = Instant.now();

        final CreateFixedGameCommand createFixedGameWithCommand =
                new CreateFixedGameCommand(gameId, now, getHumanSimpleMove(), 2, 2);

        final MoveHumanCommand moveHumanCommand = new MoveHumanCommand(gameId, now, Direction.EAST);
        fixture.givenCommands(Collections.singletonList(createFixedGameWithCommand))
                .when(moveHumanCommand)
                .expectEvents(new HumanMovedEvent(gameId, Direction.EAST, new Coordinate(0, 0), new Coordinate(1, 0), now));

    }

    @Test
    public void move_human_command_should_emit_human_moved_event_and_box_moved_event_when_move_in_direction_is_possible_by_pushing_box() {

        final Instant now = Instant.now();

        final CreateFixedGameCommand createFixedGameWithCommand =
                new CreateFixedGameCommand(gameId, now, getHumanPushTwoBoxes(), 5, 6);

        final MoveHumanCommand moveHumanCommand = new MoveHumanCommand(gameId, now, Direction.SOUTH);
        fixture.givenCommands(Collections.singletonList(createFixedGameWithCommand))
                .when(moveHumanCommand)
                .expectEvents(new HumanMovedEvent(gameId, Direction.SOUTH, new Coordinate(1, 0), new Coordinate(1, 1), now),
                        new BoxMovedEvent(gameId, Direction.SOUTH, new Coordinate(1, 1), new Coordinate(1, 2), now),
                        new BoxMovedEvent(gameId, Direction.SOUTH, new Coordinate(1, 2), new Coordinate(1, 3), now));
    }


    @Test
    public void move_human_command_should_emit_human_bounced_event_when_move_in_direction_is_not_possible() {

        final Instant now = Instant.now();

        final CreateFixedGameCommand createFixedGameWithCommand =
                new CreateFixedGameCommand(gameId, now, getHumanBounced(), 5, 4);

        final MoveHumanCommand moveHumanCommand = new MoveHumanCommand(gameId, now, Direction.SOUTH);
        fixture.givenCommands(Collections.singletonList(createFixedGameWithCommand))
                .when(moveHumanCommand)
                .expectEvents(new HumanBouncedEvent(gameId, Direction.SOUTH, now));
   }

    @Test
    public void move_human_command_should_emit_game_won_event_when_happer_is_not_able_to_move(){
        final Instant now = Instant.now();

        final CreateFixedGameCommand command =
                new CreateFixedGameCommand(gameId, now, getHumanWin(), 5, 4);

        final MoveHumanCommand moveHumanCommand = new MoveHumanCommand(gameId, now, Direction.SOUTH);
        fixture.givenCommands(Collections.singletonList(command))
                .when(moveHumanCommand)
                .expectEvents(new HumanMovedEvent(gameId, Direction.SOUTH, new Coordinate(3, 1), new Coordinate(3, 2), now),
                        new BoxMovedEvent(gameId, Direction.SOUTH, new Coordinate(3, 2), new Coordinate(3, 3), now),
                        new GameWonEvent(gameId, now));

    }

    @Test
    public void move_human_command_should_emit_game_lost_event_when_move_to_happer(){

        final Instant now = Instant.now();

        final CreateFixedGameCommand createFixedGameWithCommand =
                new CreateFixedGameCommand(gameId, now, getHumanLos(), 5, 4);

        final MoveHumanCommand moveHumanCommand = new MoveHumanCommand(gameId, now,  Direction.SOUTH);
        fixture.givenCommands(Collections.singletonList(createFixedGameWithCommand))
                .when(moveHumanCommand)
                .expectEvents(new HumanMovedEvent(gameId, Direction.SOUTH, new Coordinate(3, 1), new Coordinate(3, 2), now),
                              new GameLostByCatchingEvent(gameId, now));

    }

    @Test
    public void move_happer_command_should_emit_game_lost_event_when_move_happer_move_to_human(){

        final Instant now = Instant.now();

        final CreateFixedGameCommand createFixedGameWithCommand =
                new CreateFixedGameCommand(gameId, now,  getHumanLos2(), 5, 4);

        final MoveHapperCommand moveHapperCommand = new MoveHapperCommand(gameId, now);
        fixture.givenCommands(Collections.singletonList(createFixedGameWithCommand))
                .when(moveHapperCommand)
                .expectEvents(new HapperMovedEvent(gameId, Direction.SOUTH, new Coordinate(3, 1), new Coordinate(3, 2), now),
                              new GameLostByCatchingEvent(gameId, now));

    }

    @Test
    public void move_happer_command_should_emit_happer_moved_event_when_move_is_possible(){

        final Instant now = Instant.now();
        final CreateFixedGameCommand createGameCommandWithGivenPlayground =
                new CreateFixedGameCommand(gameId, now, getHapperMove(), 5, 4);

        final Instant now_1 = Instant.now();
        final MoveHapperCommand moveHapperCommand = new MoveHapperCommand(gameId, now_1);
        fixture.givenCommands(Collections.singletonList(createGameCommandWithGivenPlayground))
                .when(moveHapperCommand)
                .expectEvents(new HapperMovedEvent(gameId, Direction.WEST, new Coordinate(4, 3), new Coordinate(3, 3), now_1));

    }

    @Test
    public void move_human_command_should_emit_game_lost_event_when_making_move_nr_50() {

        final Instant now = Instant.now();
        final CreateFixedGameCommand createGameCommandWithGivenPlayground =
                new CreateFixedGameCommand(gameId, now, getHapperMove(), 5, 4);

        final ArrayList preCommands = new ArrayList<Object>();
        preCommands.add(createGameCommandWithGivenPlayground);

        final MoveHumanCommand moveHumanCommandEast = new MoveHumanCommand(gameId, now,  Direction.EAST);
        final MoveHumanCommand moveHumanCommandWest = new MoveHumanCommand(gameId, now,  Direction.WEST);

        for(int i=0; i < 50 -1; i++){
            if(i%2==0){
                preCommands.add(moveHumanCommandEast);
            }else{
                preCommands.add(moveHumanCommandWest);
            }
        }

        fixture.givenCommands(preCommands)
                .when(moveHumanCommandWest)
                .expectEvents(new HumanMovedEvent(gameId, Direction.WEST, new Coordinate(1, 0), new Coordinate(0, 0),  now),
                              new GameLostByMaxNumberOfStepsEvent(gameId, now));
    }

    /**
     * Opgave 5
     */

}