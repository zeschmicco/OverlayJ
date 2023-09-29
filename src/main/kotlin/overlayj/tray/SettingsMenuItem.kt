package overlayj.tray

import java.awt.MenuItem
import java.awt.event.ActionListener

class SettingsMenuItem(listener: ActionListener) : MenuItem() {
    init {
        label = "Settings"
        actionCommand = "showSettings"
        addActionListener(listener)
    }
}