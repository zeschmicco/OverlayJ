package overlayj.saves

import org.greenrobot.eventbus.EventBus
import overlayj.Constants
import overlayj.CrosshairPanel
import overlayj.CrosshairSelectionEvent
import overlayj.config.ConfigCrosshair
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.MatteBorder

class SavePanel(crosshairs: MutableList<ConfigCrosshair>) : JPanel() {
    init {
        layout = FlowLayout()
        border =
            CompoundBorder(
                MatteBorder(1, 0, 0, 0, Constants.BORDER_COLOR),
                EmptyBorder(10, 10, 10, 10)
            )

        crosshairs.forEach {
            println("#panel for ${it.name}, $it")
            CrosshairPanel(it).apply {
                val m = object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent?) {
                        EventBus.getDefault().post(CrosshairSelectionEvent(it))
                    }
                }
                addMouseListener(m)
            }.also { add(it) }
        }
    }
}
