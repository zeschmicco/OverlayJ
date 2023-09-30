package overlayj

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import overlayj.config.ConfigCrosshair
import overlayj.config.ConfigCrosshairLayerDot
import overlayj.config.ConfigCrosshairLayerLine
import java.awt.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class Crosshair(private val settings: Settings) : JFrame(), ChangeListener {
    private lateinit var crosshairCfg: ConfigCrosshair
    private val transparent = Color(127, 0, 0, 32)

    private val size = 128

    init {
        settings.addChangeListener(this)

        isUndecorated = true
        isAlwaysOnTop = true
        background = transparent
        type = Type.UTILITY
        isVisible = true

        setBounds()
        enableWindowsTransparency()
    }

    fun setBounds() {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val dm = gd.getDisplayMode()

        val location = Point(
            (dm.width / 2).minus(size / 2),
            (dm.height / 2).minus(size / 2)
        )

        super.setBounds(location.x, location.y, size, size)
    }

    fun enableWindowsTransparency() {
        val hwnd = WinDef.HWND()
        hwnd.setPointer(Native.getComponentPointer(this))

        var wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE)
        wl = wl or WinUser.WS_EX_LAYERED or WinUser.WS_EX_TRANSPARENT

        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl)
    }

    override fun stateChanged(e: ChangeEvent?) {
        println("crosshair.stateChanged()")
        crosshairCfg = e!!.source as ConfigCrosshair
        repaint()
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        crosshairCfg.layers.forEachIndexed { idx, layer ->
            println("painting layer $idx: $layer")
            if (layer.dot.show)
                paintDot(graphics, layer.dot)
            paintCross(graphics, layer.line)
        }
    }

    private fun paintDot(graphics: Graphics, dot: ConfigCrosshairLayerDot) {
        graphics.color = Settings.decodeColor(dot.color)

        val dotOffset = offsetFromCenter(dot.radius / 2)
        graphics.drawOval(dotOffset - 1, dotOffset - 1, dot.radius, dot.radius)

        if (dot.filled)
            graphics.fillRect(dotOffset, dotOffset, dot.radius - 1, dot.radius - 1)
    }

    private fun paintCross(graphics: Graphics, line: ConfigCrosshairLayerLine) {
        graphics.color = Settings.decodeColor(line.color)

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

    companion object {
        fun getImage(): Image {
            val imageURL = ::Crosshair.javaClass.getResource("crosshair.png")
            return ImageIO.read(imageURL)
        }
    }
}