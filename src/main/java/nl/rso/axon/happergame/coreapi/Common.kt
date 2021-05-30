package nl.rso.axon.happergame.coreapi

import nl.rso.axon.happergame.commandside.model.Coordinate

enum class GameStatus {
    GAME_ON,
    PAUSED,
    WON,
    CANCELED,
    LOST,
    LOST_BY_INACTIVITY,
    LOST_BY_EXCEEDING_MAX_NUMBER_OF_STEPS
}

enum class MoveStrategyHapper {
    SHORTESTPATH,
    RANDOM
}

enum class Difficulty(val percentageBoxes: Double, val percentagesBlocks: Double) {
    EASY(percentageBoxes = 0.30, percentagesBlocks = 0.10 ),
    MEDIUM(percentageBoxes = 0.20, percentagesBlocks = 0.10 ),
    HARD(percentageBoxes = 0.10, percentagesBlocks = 0.20 )
}

data class Level(val simplifiedPlayground: String,
                 val width: Int,
                 val height: Int,
                 val moveStrategyHapper: MoveStrategyHapper)


fun calcCoordinate(index: Int, width: Int) : Coordinate {
    val y = index / width
    val x = index % width

    return Coordinate(x, y)
}


fun calcIndex(coordinate: Coordinate, width: Int) : Int {
    return (coordinate.x + (coordinate.y * width))
}


val level1 : Level = Level("""
                     HXXXBXBXXXXXXXXBXBXXXXXX
                     XBRXBRBXXRBXBRXBRBXXRBXX
                     XBRBRXXXBXXXBRXXXXXXXXXX
                     XXXXXXXXBRRXXXBBXXXBRRXX
                     XBRXBRBXXRBXBRXBRBXXRBXX
                     XXXBBXXXBRRXXXBBXXXBRRXX
                     XBRXBRBXXRBXBRXBRBXXRBXX
                     XBRBRXXXBXXXBRBRXXXBXXXX
                     XXXBBXXXBRRXXXBBXXXBRRXX
                     XXXXBBRXXBXBXXXBBRXXBXXE


                  """.replace("\\s".toRegex(), ""), 24, 10, MoveStrategyHapper.RANDOM)


val level2 : Level = Level("""
                     HXXXXXBXXXXXXXXXXBXXXXXX
                     XBRXXXBXXXXXBRXXXBXXXXXX
                     XBRXRXXXXBRXBRXRXXXXBRXX
                     BXXBXXXBBXXXXXBXXXBBXXXX
                     BXXBXXXBBXXXXXBXXXBBXXXX
                     XBRXXXBXXXXXBRXXXBXXXXXX
                     XBRXRXXXXBRXBRXRXXXXBRXX
                     BXXBXXXBBXXXXXBXXXBBXXXX
                     BXXBXXXBBXXXXXBXXXBBXXXX
                     XXXXBBRXRRXBXXXBBRXRRXXE

                  """.replace("\\s".toRegex(), ""), 24, 10, MoveStrategyHapper.SHORTESTPATH)



typealias GameElements = String