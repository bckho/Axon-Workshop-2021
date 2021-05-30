package nl.rso.axon.happergame.coreapi

import nl.rso.axon.happergame.commandside.model.Direction
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.Instant

abstract class GameCommand(@TargetAggregateIdentifier open val gameId: String, open val commandDate: Instant)
data class CreateGameWithGivenLevelCommand(override val gameId: String, override val commandDate: Instant, val level: Level) : GameCommand(gameId, commandDate )
data class CreateRandomGameWithGivenDifficultyCommand(override val gameId: String, override val commandDate: Instant, val width: Int, val height: Int, val difficulty: Difficulty) : GameCommand(gameId, commandDate)
data class CreateFixedGameCommand(override val gameId: String, override val commandDate: Instant, val playground: List<GameElements>, val width: Int, val height: Int) : GameCommand(gameId, commandDate)

data class MoveHapperCommand(override val gameId: String, override val commandDate: Instant)  : GameCommand(gameId, commandDate)
data class MoveHumanCommand(override val gameId: String, override val commandDate: Instant, val direction: Direction) : GameCommand(gameId, commandDate)
data class StopGameCommand(override val gameId: String, override val commandDate: Instant) : GameCommand(gameId, commandDate)
/**
 * Opgave 6
 */
