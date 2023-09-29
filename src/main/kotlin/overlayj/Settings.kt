package overlayj

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Point
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JSlider

class Settings(val config: Config) : JFrame() {
    init {
        calcSize()
        calcLocation()
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
                    config.edgeLength = slider.value
                }
            })
        add(JButton("Close").also { it.addActionListener { isVisible = false } }, BorderLayout.SOUTH)
    }

    fun calcSize() {
        size = Dimension(640, 480)
    }

    fun calcLocation(): Unit {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val dm = gd.getDisplayMode()

        location = Point(dm.width - size.width, dm.height - size.height - 48)
    }
}