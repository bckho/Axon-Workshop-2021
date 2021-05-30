package nl.rso.axon.happergame.queryside.model

import nl.rso.axon.happergame.commandside.model.*
import nl.rso.axon.happergame.coreapi.*

import java.time.Instant

data class GameViewModel(
        val startDate: Instant,
        val endDate: Instant? = null,
        val gameStats: GameStats,
        val width: Int,
        val height: Int,
        val simplePlayground: List<GameElements>) {

    fun moveGameElement(from: Coordinate, to: Coordinate, gameElement: GameElement) : GameViewModel {

        val fromIndex = calcIndex(from, this.width)
        val toIndex = calcIndex(to, this.width)

        val fromGameElementsTemp = this.simplePlayground.get(fromIndex).replaceFirst(gameElement.asciRepresentation, 'X')
        val fromGameElements: String = if (fromGameElementsTemp.isEmpty() || fromGameElementsTemp.all { c -> c == 'X' }) { "X" } else fromGameElementsTemp.filter { c -> c != 'X' }

        val toBeGameElements = ((gameElement.asciRepresentation).plus(this.simplePlayground.get(toIndex))).filter { c -> c != 'X' };

        val toBePlayground = simplePlayground.mapIndexed { i, currentGameElements ->
            when (i) {
                fromIndex -> fromGameElements
                toIndex   -> toBeGameElements
                else      -> currentGameElements
            }
        }

       val toBeGameStats =  this.gameStats.moveGameElement(gameElement)

       return this.copy(simplePlayground = toBePlayground, gameStats = toBeGameStats)
    }

    fun changeGameStatus(newStatus: GameStatus) : GameViewModel {
        return this.copy(gameStats = this.gameStats.changeGameStatus(newStatus))
    }

    fun endGame(endDate: Instant) : GameViewModel {
        return this.copy(endDate = endDate)
    }

    fun humanBounced() : GameViewModel {
        return this.copy(gameStats = gameStats.humanBounced())
    }
}

data class GameStats(val gameStatus: GameStatus = GameStatus.GAME_ON,
                     val movesHuman: Int = 0,
                     val bouncers: Int = 0,
                     val movesHapper: Int = 0,
                     val boxesMoved: Int = 0,
                     val inActivity: Int = 0) {

    fun moveGameElement(gameElement: GameElement): GameStats {

        when (gameElement) {
            Happer() -> return this.copy(movesHapper = this.movesHapper + 1, inActivity = this.inActivity + 1)
            Box() -> return this.copy(boxesMoved = this.boxesMoved + 1)
            Human() -> return this.copy(movesHuman = this.movesHuman + 1, inActivity = 0)
            else     -> return this
        }
    }

    fun humanBounced() : GameStats {
        return this.copy(bouncers = this.bouncers + 1)
    }

    fun changeGameStatus(newStatus: GameStatus): GameStats {
        return this.copy(gameStatus = newStatus)
    }
}


object SimpleGameUtils {

    fun toGameElement(gameElement: Char): GameElement? {
        return when(gameElement)
        {
            'H'  -> Human()
            'E'  -> Happer()
            'B'  -> Box()
            'R'  -> Block()
            'X'  -> null
            else -> throw IllegalArgumentException("Wrong Character.")
        }
    }

    fun getLevelInfo(level: String): Level {

        return  when (level) {
            "1" -> level1
            "2" -> level2
            else -> throw IllegalArgumentException("No such level")
        }
    }
}