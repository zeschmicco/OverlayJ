package overlayj

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import java.awt.Color
import java.awt.Graphics
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class Crosshair(private val config: Config) : JFrame(), ChangeListener {
    private val transparent = Color(0, 0, 0, 0)

    private var center = config.edgeLength / 2
    private var offset = center - config.thickness / 2

    init {
        config.addChangeListener(this)

        background = transparent
        type = Type.UTILITY
        isUndecorated = true
        isAlwaysOnTop = true
        isVisible = true

        setBounds()
        enableWindowsTransparency()
    }

    fun setBounds() {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val dm = gd.getDisplayMode()

        center = config.edgeLength / 2
        offset = center - config.thickness / 2

        val x = (dm.width / 2) - center
        val y = (dm.height / 2) - center

        super.setBounds(x, y, config.edgeLength, config.edgeLength)
    }

    fun enableWindowsTransparency() {
        val hwnd = WinDef.HWND()
        hwnd.setPointer(Native.getComponentPointer(this))

        var wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE)
        wl = wl or WinUser.WS_EX_LAYERED or WinUser.WS_EX_TRANSPARENT

        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl)
    }

    override fun stateChanged(e: ChangeEvent?) {
        setBounds()
        repaint()
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        paintCross(graphics)
        paintDot(graphics)
    }

    private fun paintDot(graphics: Graphics) {
        graphics.color = Color.GREEN

        // center, rectangle
        graphics.fillRect(offset, offset, config.thickness, config.thickness)

        // center, open circle
        graphics.drawOval(offset - 1, offset - 1, 3, 3)
    }

    private fun paintCross(graphics: Graphics) {
        graphics.color = Color.red

        // left
        graphics.fillRect(0, offset, center - 6, config.thickness)

        // right
        graphics.fillRect(center + 6, offset, center - 6, config.thickness)

        // top
        // g.fillRect(y, 0, thickness, center - 4)

        // bottom
        graphics.fillRect(offset, center + 6, config.thickness, center - 6)
    }
}