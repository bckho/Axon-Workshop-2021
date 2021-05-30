package nl.rso.axon.happergame.coreapi

import nl.rso.axon.happergame.commandside.model.*
import java.time.Instant


abstract class GameEvent(open val gameId: String, val occurDate: Instant)

data class HumanBouncedEvent(override val gameId: String, val direction: Direction, val bounceDate: Instant) : GameEvent(gameId, bounceDate)

abstract class GameElementMovedEvent(override val gameId: String, open val gameElement: GameElement, open val direction: Direction, open val from: Coordinate, open val to: Coordinate, open val moveDate: Instant) : GameEvent(gameId, moveDate)
data class HumanMovedEvent(override val gameId: String, override val direction: Direction, override val from: Coordinate, override val to: Coordinate, override val moveDate: Instant) : GameElementMovedEvent(gameId, Human(), direction, from, to, moveDate)
data class HapperMovedEvent(override val gameId: String, override val direction: Direction, override  val from: Coordinate, override val to: Coordinate, override val moveDate: Instant) : GameElementMovedEvent(gameId, Happer(), direction, from, to, moveDate)
data class BoxMovedEvent(override val gameId: String, override val direction: Direction, override val from: Coordinate, override val to: Coordinate, override val moveDate: Instant) : GameElementMovedEvent(gameId, Box(), direction, from, to, moveDate)

abstract class GameCreatedEvent(override val gameId: String, open val creationDate: Instant) : GameEvent(gameId, creationDate)
data class LevelGameCreatedEvent(override val gameId: String, val level: Level, override val creationDate: Instant) : GameCreatedEvent(gameId, creationDate)
data class FixedGameCreatedEvent(override val gameId: String, val simplePlayground: List<GameElements>, val width: Int, val height: Int, override val creationDate: Instant) : GameCreatedEvent(gameId, creationDate)

abstract class GameStatusChangedEvent(override val gameId: String, open val gameStatus: GameStatus, open val changeDate: Instant) : GameEvent(gameId, changeDate)
data class GameStoppedEvent(override val gameId: String, override val changeDate: Instant) : GameEndedEvent(gameId, GameStatus.CANCELED, changeDate)
data class GamePausedEvent(override val gameId: String, override val changeDate: Instant) : GameStatusChangedEvent(gameId, GameStatus.PAUSED, changeDate)
data class GameRestartedEvent(override val gameId: String, override val changeDate: Instant) : GameStatusChangedEvent(gameId, GameStatus.GAME_ON, changeDate)
abstract class GameEndedEvent(override  val gameId: String, override val gameStatus: GameStatus, open val endDate: Instant): GameStatusChangedEvent(gameId, gameStatus, endDate)
abstract class GameLostEvent(override val gameId: String, override val endDate: Instant) : GameEndedEvent(gameId, GameStatus.LOST, endDate)
data class GameLostByCatchingEvent(override val gameId: String, override val endDate: Instant) : GameLostEvent(gameId, endDate)
data class GameLostByMaxNumberOfStepsEvent(override val gameId: String, override val endDate: Instant) : GameLostEvent(gameId, endDate)
data class GameLostByInactivityEvent(override val gameId: String, val inactivitySteps: Int, override val endDate: Instant) : GameLostEvent(gameId, endDate)
data class GameWonEvent(override val gameId: String, override val endDate: Instant) : GameEndedEvent(gameId, GameStatus.WON, endDate)
data class HapperReadyToMoveEvent(val gameId: String)