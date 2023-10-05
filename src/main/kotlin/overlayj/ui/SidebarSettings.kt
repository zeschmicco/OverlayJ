package overlayj.ui

import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class SidebarSettings : JPanel() {
    init {
        border = EmptyBorder(20, 0, 0, 0)
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        JLabel("Settings").also { add(it) }

        SidebarButton("Foobar") {
            println("#clicked Foobar")
        }.also { add(it) }
    }
}