package overlayj.config

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.awt.Color

class ConfigDataTest {
    @Test
    fun readConfig() {
        val theConfig = read()
        assertNotNull(theConfig)
    }

    @Test
    fun parseColor() {
        val theConfig = read()
        val theCrosshair = theConfig.crosshairs.get(0)
        val theLayer = theCrosshair.layers.get(0)

        val c = Color.decode(theLayer.dot.color)
        assert(c.equals(Color.GREEN))
    }
}