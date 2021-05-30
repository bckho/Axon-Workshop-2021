package nl.rso.axon.happergame.queryside.viewmodels

import nl.rso.axon.happergame.commandside.model.Box
import nl.rso.axon.happergame.commandside.model.Coordinate
import nl.rso.axon.happergame.commandside.model.Happer
import nl.rso.axon.happergame.commandside.model.Human
import nl.rso.axon.happergame.queryside.model.GameStats
import nl.rso.axon.happergame.queryside.model.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class GameViewModelTest {


    @Test
    fun moveBoxToBoxSquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                height = 3,
                gameStats = GameStats(),
                simplePlayground = listOf("X", "X", "X",
                                          "H", "BB", "B",
                                          "X", "B", "R"))


        val toBePlayGround = listOf("X", "X", "X",
                                               "H", "B", "BB",
                                               "X", "B", "R")

        val result = testGameModel.moveGameElement(Coordinate(1, 1), Coordinate(2, 1), Box())

        assertEquals(testGameModel.copy(simplePlayground = toBePlayGround, gameStats = GameStats().copy(boxesMoved = 1)), result)
    }

    @Test
    fun moveHumanToBoxSquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                height = 3,
                gameStats = GameStats(),
                simplePlayground = listOf("X", "X", "X",
                                          "H", "B", "B",
                                          "X", "B", "R"))


        val toBePlayGround = listOf("X", "X", "X",
                                               "X", "HB", "B",
                                               "X", "B", "R")

        val result = testGameModel.moveGameElement(Coordinate(0, 1), Coordinate(1, 1), Human())

        assertEquals(testGameModel.copy(simplePlayground = toBePlayGround, gameStats = GameStats().copy(movesHuman = 1)), result)

    }


    @Test
    fun moveHumanBoxToBoxSquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                height = 3,
                gameStats = GameStats(),
                simplePlayground = listOf("X", "X", "X",
                                          "X", "HB", "B",
                                          "X", "B", "R"))


        val toBePlayGround = listOf("X", "X", "X",
                                               "X", "B", "HB",
                                               "X", "B", "R")

        val result = testGameModel.moveGameElement(Coordinate(1, 1), Coordinate(2, 1), Human())

        assertEquals(testGameModel.copy(simplePlayground = toBePlayGround, gameStats = GameStats().copy(movesHuman = 1)), result)

    }


    @Test
    fun moveHumanToHapperSquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                height = 3,
                gameStats = GameStats(),
                simplePlayground = listOf("X", "X", "X",
                                          "H", "E", "X",
                                          "X", "B", "R"))


        val toBePlayGround = listOf("X", "X", "X",
                                               "X", "HE", "X",
                                               "X", "B", "R")

        val result = testGameModel.moveGameElement(Coordinate(0, 1), Coordinate(1, 1), Human())


        assertEquals(result, testGameModel.copy(simplePlayground = toBePlayGround, gameStats = GameStats().copy(movesHuman = 1, inActivity = 0)))
    }

    @Test
    fun moveHumanToEmptySquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                gameStats = GameStats(),
                height = 3,
                simplePlayground = listOf("X", "X", "X",
                                          "H", "X", "X",
                                          "E", "B", "R"))



        val toBeSimplePlayground = listOf("X", "X", "X",
                                                     "X", "H", "X",
                                                     "E", "B", "R")


        val resultGameViewModel = testGameModel.moveGameElement(Coordinate(0, 1), Coordinate(1, 1), Human())


        assertEquals(resultGameViewModel, testGameModel.copy(simplePlayground = toBeSimplePlayground, gameStats = GameStats().copy(movesHuman = 1, inActivity = 0)))

    }

    @Test
    fun moveHapperToEmptySquare() {
        val testGameModel = GameViewModel(startDate = Instant.now(),
                width = 3,
                height = 3,
                gameStats = GameStats(),
                simplePlayground = listOf("X", "X", "X",
                                          "H", "X", "X",
                                          "E", "X", "R"))

        val toBeSimplePlayground = listOf("X", "X", "X",
                                                     "H", "X", "X",
                                                     "X", "E", "R")


        val resultGameViewModel = testGameModel.moveGameElement(Coordinate(0, 2), Coordinate(1, 2), Happer())


        assertEquals(resultGameViewModel, testGameModel.copy(simplePlayground = toBeSimplePlayground, gameStats = GameStats().copy(movesHapper = 1, inActivity = 1)))
    }

}