package nl.rso.axon.happergame.commandside.utils

import nl.rso.axon.happergame.commandside.model.*
import nl.rso.axon.happergame.coreapi.*

fun getSimplePlayground(width: Int, height: Int, difficulty: Difficulty): Playground {

    val nrOfSquares = width * height;

    val nrOfBoxes = (difficulty.percentageBoxes * nrOfSquares).toInt()
    val nrOfBlocks = (difficulty.percentagesBlocks * nrOfSquares).toInt()
    val nrOfActors = 2
    val nrOfEmptySquares = nrOfSquares - nrOfBoxes - nrOfBlocks - nrOfActors


    val actors = listOf<GameElement>(Happer(), Human())
    val blocks = (1 .. nrOfBlocks).flatMap({listOf<GameElement>(Block())})
    val boxes = (1 .. nrOfBoxes).flatMap({listOf<GameElement>(Box())})
    val emptySquares = (1 .. nrOfEmptySquares).flatMap { listOf<GameElement?>(null)}


    val gameElementsUnshuffled =
            actors
                    .plus(blocks)
                    .plus(boxes)
                    .plus(emptySquares)

    val gameElementsShuffled = gameElementsUnshuffled.shuffled();
    val squares = gameElementsShuffled.mapIndexed { index, gameElement ->

        if (gameElement == null) {
            Square(calcCoordinate(index, width), listOf())
        } else {
            Square(calcCoordinate(index, width), listOf(gameElement))
        }
    }

    return Playground(width, height, squares)
}

fun getSimplePlayground(playground: Playground): List<GameElements> {
    return playground.squares
            .flatMap { square ->
                if (square.gameElements.isEmpty()) listOf("X")
                else square.gameElements.map({ gameElement -> "" + gameElement.asciRepresentation })
            }
}


fun getLevel(level: String) : Level {
    return when (level) {
        "1" -> level1
        "2" -> level2
        else -> throw java.lang.IllegalArgumentException("Unknown playground")
    }
}

fun getGame(level: Level) : Game {
    return Game(playground = getPlayGround(level.simplifiedPlayground.map { it.toString() }, level.width, level.height), moveStrategyHapper = level.moveStrategyHapper)
}

fun getFixedGame(playground: List<String>, width: Int, height: Int): Game {
    return Game(playground = getPlayGround(playground, width, height))
}

fun getPlayGround(playGround: List<GameElements>, width: Int, height: Int) : Playground {

    val squares = playGround.mapIndexed({ index, gameElements -> Square(calcCoordinate(index, width), getGameElements(gameElements)) })

    return Playground(width, height, squares)
}

fun getGameElements(gameElements: String): List<GameElement> {

    fun getGameElement(gameElement: Char): List<GameElement> {
        val gameElement =
                when(gameElement){
                    'H' -> listOf<GameElement>(Human())
                    'E' -> listOf<GameElement>(Happer())
                    'B' -> listOf<GameElement>(Box())
                    'R' -> listOf<GameElement>(Block())
                    'X' -> listOf<GameElement>()
                    else -> throw IllegalArgumentException("Wrong character")
                }
        return gameElement
    }

    return gameElements.flatMap {getGameElement(it) }
}