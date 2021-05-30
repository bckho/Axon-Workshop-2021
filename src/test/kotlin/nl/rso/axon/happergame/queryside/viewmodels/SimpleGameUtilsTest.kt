package nl.rso.axon.happergame.queryside.viewmodels

import nl.rso.axon.happergame.commandside.model.Block
import nl.rso.axon.happergame.commandside.model.Box
import nl.rso.axon.happergame.commandside.model.Happer
import nl.rso.axon.happergame.commandside.model.Human
import nl.rso.axon.happergame.queryside.model.SimpleGameUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalArgumentException

class SimpleGameUtilsTest {

    @Test
    fun toGameElementHappyFlow() {
        assertEquals(SimpleGameUtils.toGameElement('H'), Human())
        assertEquals(SimpleGameUtils.toGameElement('E'), Happer())
        assertEquals(SimpleGameUtils.toGameElement('X'), null)
        assertEquals(SimpleGameUtils.toGameElement('B'), Box())
        assertEquals(SimpleGameUtils.toGameElement('R'), Block())
    }

    @Test(expected = IllegalArgumentException::class)
    fun toGameElementUnHappyFlow() {
        SimpleGameUtils.toGameElement('U')
    }
}