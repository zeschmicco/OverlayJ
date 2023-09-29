package overlayj.tray

import java.awt.MenuItem
import java.awt.event.ActionListener

class AboutMenuItem(listener: ActionListener) : MenuItem() {
    init {
        label = "About"
        actionCommand = "showAbout"
        addActionListener(listener)
    }
}