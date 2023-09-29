package overlayj;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    fun appHasADimension() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.calcDimension(), "app should have a dimension")
    }
    @Test
    fun appHasALocation() {
        val classUnderTest = App()
        assertNotNull(classUnderTest.calcLocation(), "app should have a location")
    }
}
