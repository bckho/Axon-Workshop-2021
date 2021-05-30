package nl.rso.axon.happergame.commandside.restcontrollers;

import lombok.extern.slf4j.Slf4j;
import nl.rso.axon.happergame.commandside.model.Direction;
import nl.rso.axon.happergame.coreapi.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static nl.rso.axon.happergame.commandside.utils.DomainUtilsKt.getLevel;

@RestController
@RequestMapping("/api/games")
@Slf4j
public class GameRestController {

    private final CommandGateway commandGateway;


    public GameRestController(final CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @PostMapping("/create/{gameId}/{level}")
    void createGame(final @PathVariable String gameId, final @PathVariable String level) {
        commandGateway.sendAndWait(new CreateGameWithGivenLevelCommand(gameId, Instant.now(), getLevel(level)));
    }

    @PostMapping("/create/random/{gameId}/{width}/{height}/{difficulty}")
    void createRandomGame(final @PathVariable String gameId,
                          final @PathVariable int width,
                          final @PathVariable int height,
                          final @PathVariable Difficulty difficulty) {
        commandGateway.sendAndWait(new CreateRandomGameWithGivenDifficultyCommand(gameId, Instant.now(), width, height, difficulty));
    }

    @PostMapping("/movehuman/{gameId}/{direction}")
    public void moveHuman(final @PathVariable String gameId,
                          final @PathVariable Direction direction){
        commandGateway.send(new MoveHumanCommand(gameId, Instant.now(), direction));
    }

    @PostMapping ("/stop/{gameId}")
    void stopGame(final @PathVariable String gameId){
        commandGateway.send(new StopGameCommand(gameId, Instant.now()));
    }

    /**
     *
     * Opgave 6. Implementeer methoden
     */
    @PostMapping ("/restart/{gameId}")
    void restartGame(final @PathVariable String gameId){

    }

    @PostMapping ("/pause/{gameId}")
    void pauseGame(final @PathVariable String gameId){

    }

}
