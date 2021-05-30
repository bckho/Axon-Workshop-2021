package nl.rso.axon.happergame.commandside.model


import nl.rso.axon.happergame.coreapi.GameStatus
import nl.rso.axon.happergame.coreapi.MoveStrategyHapper
import java.time.Instant

data class Game (
        val started: Instant = Instant.now(),
        val ended: Instant? = null,
        val playground: Playground,
        val gameStatus: GameStatus = GameStatus.GAME_ON,
        val moveStrategyHapper: MoveStrategyHapper = MoveStrategyHapper.RANDOM,
        val bouncers: Int = 0,
        val movesHuman: Int = 0,
        val movesHapper: Int = 0,
        val boxesMoved: Int = 0
        /**
         * Opgave 5
         */
        ) {


    companion object {
        /**
         *Uitwerking opgave 5
         */
        val MAX_NUMBER_OF_STEPS = 50

    }

    fun moveHapperPathinfo() : Movement<Happer>? {
        return playground.getHapperMovementInfo()
    }

    fun moveHumanInfo(direction: Direction): MovePlayableResult<Human, Pushable> {
        return playground.moveHumanToResult(direction)
    }

    fun determineGameStatus(): GameStatus {

        /**
         * Opgave 4
         */
        if (movesHuman == MAX_NUMBER_OF_STEPS) return GameStatus.LOST_BY_EXCEEDING_MAX_NUMBER_OF_STEPS


        if (playground.humanOnSameSquareAsHapper()) return GameStatus.LOST

        /**
         * Opgave 5
         */


        if (!playground.happerIsAbleToMove()) return GameStatus.WON

        return GameStatus.GAME_ON
    }

    fun changeStatus(gameStatus: GameStatus): Game {
        return this.copy(gameStatus = gameStatus)
    }

    fun moveGameElement(gameElement: GameElement, from: Coordinate, to: Coordinate): Game {

        val toBePlayground = playground.moveGameElement(gameElement, from, to)
        return when(gameElement) {

            /**
             * Opgave 5:
             */
            Human() -> this.copy(playground = toBePlayground, movesHuman = this.movesHuman + 1)
            /**
             * Opgave 5
             */
            Happer() -> this.copy(playground = toBePlayground, movesHapper = this.movesHapper + 1)
            Box() -> this.copy(playground = toBePlayground, boxesMoved = this.boxesMoved + 1)
            else     -> this
        }
    }


    fun humanBounced() : Game {
        return this.copy(bouncers = this.bouncers + 1)
    }
}


typealias MoveHumanInfo = Pair<Boolean, List<Square>>

data class Coordinate(val x: Int, val y: Int)
data class MovePlayableResult<T: Playable, P: Pushable>(val movement: Movement<T>?, val movedBoxes: List<Movement<P>> = listOf())
data class Movement<T: Movable>(val gameElementK: T, val direction: Direction?, val from: Coordinate, val to: Coordinate)

