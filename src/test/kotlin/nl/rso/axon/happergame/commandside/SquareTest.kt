package nl.rso.axon.happergame.commandside

import nl.rso.axon.happergame.commandside.model.Box
import nl.rso.axon.happergame.commandside.model.Coordinate
import nl.rso.axon.happergame.commandside.model.Direction
import nl.rso.axon.happergame.commandside.model.Happer
import nl.rso.axon.happergame.commandside.model.Human
import nl.rso.axon.happergame.commandside.model.Square
import org.junit.Test

import org.junit.Assert.*

class SquareTest {

    @Test
    fun getDirection() {
        val square = Square(coordinate = Coordinate(2, 3), gameElements = listOf())
        val squareEast = Square(coordinate = Coordinate(3, 3), gameElements = listOf())
        val squareWest = Square(coordinate = Coordinate(1, 3), gameElements = listOf())
        val squareSouth = Square(coordinate = Coordinate(2, 4), gameElements = listOf())
        val squareNorth = Square(coordinate = Coordinate(2, 2), gameElements = listOf())
        assertEquals(Direction.EAST, square.getDirection(squareEast))
        assertEquals(Direction.WEST, square.getDirection(squareWest))
        assertEquals(Direction.SOUTH, square.getDirection(squareSouth))
        assertEquals(Direction.NORTH, square.getDirection(squareNorth))
    }

    @Test
    fun removeGameElement() {
        val square = Square(coordinate = Coordinate(2, 3), gameElements = listOf(Box(), Human(), Box()))

        val resultSquare = square.removeGameElement(Box())
        val expectedSquare = Square(coordinate = Coordinate(2, 3), gameElements = listOf(Box(), Human()))

        assertEquals(expectedSquare, resultSquare)

    }

    @Test
    fun pushGameElement() {
        val square = Square(coordinate = Coordinate(2, 3), gameElements = listOf(Human(), Box()))
        val resultSquare = square.pushGameElement(Happer())

        val expectedSquare = Square(coordinate = Coordinate(2, 3), gameElements = listOf(Human(), Box(), Happer()))

        assertEquals(expectedSquare, resultSquare)
    }
}