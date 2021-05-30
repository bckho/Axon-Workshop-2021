package nl.rso.axon.happergame.commandside

import nl.rso.axon.happergame.commandside.model.*
import nl.rso.axon.happergame.commandside.utils.getPlayGround
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaygroundTest {

    @Test
    fun moveHumanToResultWithoutPushes() {

        val testPlayground = """
                     XXXXBXBX
                     XBHXBRBX
                     XBRBRXXE
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 8, 3)

        val moveHumanToResult = playGround.moveHumanToResult(Direction.EAST)
        val expectedHumanMovement = Movement(Human(), direction = Direction.EAST,
                from = Coordinate(x = 2, y = 1),
                to = Coordinate(x = 3, y = 1))


        val expectedResult = MovePlayableResult<Human, Pushable>(movement = expectedHumanMovement)


        assertEquals(expectedResult, moveHumanToResult)
    }


    @Test
    fun moveHumanToResultWithBouncing() {

        val testPlayground = """
                     XXXXBXBX
                     XBHRBRBX
                     XBRBRXXE
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 8, 3)

        val moveHumanToResult = playGround.moveHumanToResult(Direction.EAST)
        val expectedResult = MovePlayableResult<Human, Pushable>(movement = null)

        assertEquals(expectedResult, moveHumanToResult)
    }

    @Test
    fun moveHumanToResultMoveToHapper() {

        val testPlayground = """
                     XXXXBXBX
                     XBHEBRBX
                     XBRBRXXX
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 8, 3)

        val moveHumanToResult = playGround.moveHumanToResult(Direction.EAST)
        val expectedHumanMovement = Movement(Human(), direction = Direction.EAST,
                from = Coordinate(x = 2, y = 1),
                to = Coordinate(x = 3, y = 1))


        val expectedResult = MovePlayableResult<Human, Pushable>(movement = expectedHumanMovement)

        assertEquals(expectedResult, moveHumanToResult)
    }
    @Test
    fun moveHumanToResultWithMultipleBoxesPushed() {

        val testPlayground = """
                     XXXXBXBX
                     XBHBBXXX
                     XBRBRXXX
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 8, 3)

        val moveHumanToResult = playGround.moveHumanToResult(Direction.EAST)
        val expectedHumanMovement = Movement(Human(), direction = Direction.EAST,
                from = Coordinate(x = 2, y = 1),
                to = Coordinate(x = 3, y = 1))

        val expectedBoxPushed1 = Movement(Box(),
                from = Coordinate(x = 3, y = 1), direction = Direction.EAST,
                to = Coordinate(x = 4, y = 1))

        val expectedBoxPushed2 = Movement(Box(),
                from = Coordinate(x = 4, y = 1), direction = Direction.EAST,
                to = Coordinate(x = 5, y = 1))


        val expectedResult = MovePlayableResult<Human, Box>(movement = expectedHumanMovement, movedBoxes = listOf(expectedBoxPushed1, expectedBoxPushed2))

        assertEquals(expectedResult, moveHumanToResult)
    }

    @Test
    fun moveGameElement() {
        val testPlayground = """
                     XXXXBXBX
                     XBHBBXXX
                     XBRBRXXE
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 8, 3)


        val from = Coordinate(2, 1)
        val to = Coordinate(3, 1)

        val result = playGround.moveGameElement(Human(), from, to)

        assertTrue(result.findSquare(from)!!.peekGameElement() == null)
        assertEquals(result.findSquare(to)!!.gameElements, listOf(Box(), Human()))
    }

    @Test
    fun moveBoxToEmptySquare() {
        val testPlayGround = """
            XXX
            XBX
            EHX
           """.replace("\\s".toRegex(), "")

        val playround = getPlayGround(testPlayGround.toList().map({ "" + it }), 3, 3)

        val from = Coordinate(1,1)
        val to = Coordinate(2, 1)

        val result = playround.moveGameElement(Box(), from, to)

        assertTrue(result.findSquare(from)!!.peekGameElement() == null)
        assertEquals(listOf(Box()), result.findSquare(to)!!.gameElements)
    }

    @Test
    fun moveHapperToEmptySquare() {
        val testPlayGround = """
            XXX
            XBX
            EHX
           """.replace("\\s".toRegex(), "")

        val playround = getPlayGround(testPlayGround.toList().map({ "" + it }), 3, 3)

        val from = Coordinate(0,2)
        val to = Coordinate(0, 1)

        val result = playround.moveGameElement(Happer(), from, to)

        assertTrue(result.findSquare(from)!!.peekGameElement() == null)
        assertEquals(listOf(Happer()), result.findSquare(to)!!.gameElements)
    }



}