package overlayj.tray

import java.awt.MenuItem
import java.awt.event.ActionListener

class QuitMenuItem(listener: ActionListener) : MenuItem() {
    init {
        label = "Quit"
        actionCommand = "doQuit"
        addActionListener(listener)
    }
}