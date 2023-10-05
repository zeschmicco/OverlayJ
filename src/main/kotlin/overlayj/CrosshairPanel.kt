package overlayj

import overlayj.Constants.Companion.BORDER_COLOR
import overlayj.config.ConfigCrosshair
import overlayj.config.ConfigCrosshairLayerDot
import overlayj.config.ConfigCrosshairLayerLine
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.MatteBorder

class CrosshairPanel(private val crosshair: ConfigCrosshair) : JPanel() {
    private val transparent = Color(0, 0, 0, 16)
    private val size = 128

    init {
        border = CompoundBorder(
            EmptyBorder(0,0,0,0),
            MatteBorder(1,1,1,1, BORDER_COLOR)
        )
        background = transparent
        isVisible = true

        preferredSize = Dimension(size, size)
        maximumSize = Dimension(size, size)
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        crosshair.layers.forEachIndexed { idx, layer ->
            println("painting layer $idx: $layer")
            if (layer.dot.show)
                paintDot(graphics, layer.dot)
            paintCross(graphics, layer.line)
        }
    }

    private fun paintDot(graphics: Graphics, dot: ConfigCrosshairLayerDot) {
        graphics.color = Settings.decodeColor(dot.color, dot.opacity)

        val dotOffset = offsetFromCenter(dot.radius / 2)
        graphics.drawOval(dotOffset - 1, dotOffset - 1, dot.radius, dot.radius)

        if (dot.filled)
            graphics.fillRect(dotOffset, dotOffset, dot.radius - 1, dot.radius - 1)
    }

    private fun paintCross(graphics: Graphics, line: ConfigCrosshairLayerLine) {
        graphics.color = Settings.decodeColor(line.color, line.opacity)

        if (line.top) {
            val x = offsetFromCenter(line.thickness / 2)
            val y = offsetFromCenter(line.length + line.offset)
            graphics.fillRect(x, y, line.thickness, line.length)
        }

        if (line.bottom) {
            val x = offsetFromCenter(line.thickness / 2)
            val y = offsetFromCenter(line.offset * -1)
            graphics.fillRect(x, y, line.thickness, line.length)
        }

        if (line.left) {
            val x = offsetFromCenter(line.offset + line.length)
            val y = offsetFromCenter(line.thickness / 2)
            graphics.fillRect(x, y, line.length, line.thickness)
        }
        if (line.right) {
            val x = offsetFromCenter(line.offset * -1)
            val y = offsetFromCenter(line.thickness / 2)
            graphics.fillRect(x, y, line.length, line.thickness)
        }
    }

    private fun offsetFromCenter(size: Int): Int {
        return this.size / 2 - size
    }
}