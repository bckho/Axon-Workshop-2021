package nl.rso.axon.happergame.commandside

import nl.rso.axon.happergame.commandside.model.Coordinate
import nl.rso.axon.happergame.commandside.model.Direction
import nl.rso.axon.happergame.commandside.model.Happer
import nl.rso.axon.happergame.commandside.model.Human
import nl.rso.axon.happergame.commandside.model.Square
import org.junit.Assert.*

import org.junit.Test

class SquareUtilsTest {

    @Test
    fun getNeighbourCoordinate() {
        val testSquare = Square(Coordinate(4, 2), listOf())

        assertEquals(Coordinate(5, 2), testSquare.getNeighbourCoordinate(Direction.EAST))
        assertEquals(Coordinate(3, 2), testSquare.getNeighbourCoordinate(Direction.WEST))
        assertEquals(Coordinate(4, 1), testSquare.getNeighbourCoordinate(Direction.NORTH))
        assertEquals(Coordinate(4, 3), testSquare.getNeighbourCoordinate(Direction.SOUTH))

    }

    @Test
    fun pushGameElement() {

        val testSquare: Square = Square(Coordinate(3, 3), listOf(Human()))
        val expectedSquare= Square(Coordinate(3, 3), listOf(Human(), Happer()))

        assertEquals(expectedSquare, testSquare.pushGameElement(Happer()))
    }

    @Test
    fun pushFromBottomGameElement() {
    }

    @Test
    fun popGameElement() {
    }

    @Test
    fun popGameElementFromBottom() {
    }

    @Test
    fun peekGameElement() {
    }

    @Test
    fun elementOnThisSquare() {
    }

    @Test
    fun isElementOnTopOfThisSquare() {
    }

    @Test
    fun elementOnTopOfThisSquare() {
    }

    @Test
    fun isEmpty() {
    }
}