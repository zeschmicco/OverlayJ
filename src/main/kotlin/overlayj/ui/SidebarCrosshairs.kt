package overlayj.ui

import org.greenrobot.eventbus.EventBus
import overlayj.SidebarButtonClicked
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class SidebarCrosshairs : JPanel() {
    init {
        border = EmptyBorder(10, 0, 0, 0)
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        JLabel("Crosshairs").also { add(it) }

        SidebarButton("Saved") {
            println("#clicked Saved")
            EventBus.getDefault().post(SidebarButtonClicked("saved"))
        }.also { add(it) }

        SidebarButton("Design") {
            println("#clicked Design")
            EventBus.getDefault().post(SidebarButtonClicked("design"))
        }.also { add(it) }

        SidebarButton("About") {
            println("#clicked About")
        }.also { add(it) }

    }
}