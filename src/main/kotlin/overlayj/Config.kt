package overlayj

import java.awt.event.MouseEvent
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class Config {
    var adsButton = MouseEvent.BUTTON2
    var hideOnADS = false
    var edgeLength = 28
        set(value) {
            field = value
            changeListeners.forEach {
                it.stateChanged(ChangeEvent(this))
            }
        }
    var thickness = 2

    val changeListeners = mutableListOf<ChangeListener>()

    fun addChangeListener(cl: ChangeListener) {
        changeListeners.add(cl)
    }
}

