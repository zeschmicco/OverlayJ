package overlayj

import overlayj.config.ConfigCrosshair
import overlayj.config.ConfigData
import overlayj.config.read
import java.awt.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JSlider
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class Settings() : JFrame() {
    var config: ConfigData = read()
    var crosshairConfig: ConfigCrosshair = config.crosshairs.get(0)

    val changeListeners = mutableListOf<ChangeListener>()

    init {
        setBounds()
        isVisible = false
        layout = BorderLayout()

        add(
            JSlider(
                JSlider.HORIZONTAL,
                12,
                96,
                28
            ).also { slider ->
                slider.addChangeListener {
                    crosshairConfig.layers.get(0).line.length = slider.value
                    notifyChangeListeners()
                }
            })
        add(JButton("Close").also { it.addActionListener { isVisible = false } }, BorderLayout.SOUTH)
    }

    fun notifyChangeListeners() {
        println("settings.notifyChangeListeners()")
        changeListeners.forEach {
            it.stateChanged(ChangeEvent(crosshairConfig))
        }
    }

    fun addChangeListener(cl: ChangeListener) {
        cl.stateChanged(ChangeEvent(crosshairConfig))
        changeListeners.add(cl)
    }

    fun setBounds() {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val dm = gd.getDisplayMode()

        val size = Dimension(640, 480)
        val location = Point(dm.width - size.width, dm.height - size.height - 48)

        super.setBounds(location.x, location.y, size.width, size.height)
    }

    companion object {
        fun decodeColor(color: String): Color {
            return when (color.length) {
                7 -> Color(
                    Integer.valueOf(color.substring(1, 3), 16),
                    Integer.valueOf(color.substring(3, 5), 16),
                    Integer.valueOf(color.substring(5, 7), 16),
                )

                9 -> Color(
                    Integer.valueOf(color.substring(1, 3), 16),
                    Integer.valueOf(color.substring(3, 5), 16),
                    Integer.valueOf(color.substring(5, 7), 16),
                    Integer.valueOf(color.substring(7, 9), 16),
                )

                else -> throw IllegalStateException("Color could not be parsed: $color")
            }
        }
    }
}