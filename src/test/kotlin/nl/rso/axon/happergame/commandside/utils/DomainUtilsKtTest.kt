package nl.rso.axon.happergame.commandside.utils

import nl.rso.axon.happergame.commandside.model.*
import nl.rso.axon.happergame.coreapi.Difficulty
import nl.rso.axon.happergame.testplaygrounds.humanWin
import org.junit.Assert.assertEquals
import org.junit.Test

class DomainUtilsKtTest {

    @Test
    fun getGameElementsTest() {

        assertEquals(getGameElements("XXHEBBR"),
                listOf(
                    Human(),
                    Happer(),
                    Box(),
                    Box(),
                    Block())
        )
    }


    @Test
    fun getPlayGroundTest() {
        println(getPlayGround(listOf("X", "X", "H", "E", "B", "B", "R", "R"), 4, 2 ))
    }

    @Test
    fun getPlayGroundSimpleMoveTest() {
        println(getPlayGround(humanWin, 5, 4))
    }

    @Test
    fun of() {
        val playground = getSimplePlayground(12, 5, Difficulty.EASY)
        assertEquals( 12 * 5, playground.squares.size )
    }
}