package overlayj.ui

import overlayj.Constants.Companion.BORDER_COLOR
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.MatteBorder

class Sidebar : JPanel() {
    init {
        border =
            CompoundBorder(
                MatteBorder(1, 0, 0, 1, BORDER_COLOR),
                EmptyBorder(0, 10, 0, 25),
            )
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        SidebarCrosshairs().also { add(it) }
        SidebarSettings().also { add(it) }
    }
}