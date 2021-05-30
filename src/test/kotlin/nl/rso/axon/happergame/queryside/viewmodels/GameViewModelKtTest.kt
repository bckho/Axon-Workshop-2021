package nl.rso.axon.happergame.queryside.viewmodels

import nl.rso.axon.happergame.commandside.model.Coordinate
import nl.rso.axon.happergame.coreapi.calcCoordinate
import nl.rso.axon.happergame.coreapi.calcIndex
import org.junit.Assert.assertEquals
import org.junit.Test

class GameViewModelKtTest {

    @Test
    fun testCalcIndex() {

        val WIDTH = 3

        for (x in 0..2) {
            for(y in 0..2) {

                val coordinate = Coordinate(x, y)
                val indexCoordinate = calcIndex(coordinate, WIDTH)
                val coordinateCalculated = calcCoordinate(indexCoordinate, WIDTH)

                assertEquals(coordinate, coordinateCalculated)
            }
        }
    }
}