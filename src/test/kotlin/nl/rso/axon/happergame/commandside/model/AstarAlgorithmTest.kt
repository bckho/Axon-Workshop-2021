package nl.rso.axon.happergame.commandside.model

import nl.rso.axon.happergame.commandside.utils.getPlayGround
import org.junit.Test

import org.junit.Assert.*

class AstarAlgorithmTest {

    @Test
    fun getShortestPath() {

        val testPlayground = """
                     HXXXX
                     XBHBX
                     XBRXE
                  """.replace("\\s".toRegex(), "")

        val playGround = getPlayGround(testPlayground.toList().map({ "" + it }), 5, 3)
        val path = AstarAlgorithm().getShortestPath(playGround)

        print(path)
    }
}