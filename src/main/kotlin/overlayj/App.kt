package overlayj

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinUser
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.Window
import java.awt.event.MouseEvent

class App() : Window(null), NativeMouseListener {
    val transparent = Color(0, 0, 0, 0)

    val hideOnADS = true
    val adsButton = MouseEvent.BUTTON2

    val edgeLength = 28
    val thickness = 2
    val center = edgeLength / 2
    val offset = center - thickness / 2

    init {
        setAlwaysOnTop(true)
        setBackground(transparent)
        setLocation(calcLocation())
        setSize(calcDimension())
        setVisible(true)

        enableSystray()
        enableWindowsTransparency()
    }

    fun calcDimension(): Dimension {
        return Dimension(edgeLength, edgeLength)
    }

    fun calcLocation(): Point {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.getDefaultScreenDevice()
        val dm = gd.getDisplayMode()

        return Point(dm.getWidth() / 2 - edgeLength / 2, dm.getHeight() / 2 - edgeLength / 2)
    }

    fun enableWindowsTransparency() {
        val hwnd = HWND()
        hwnd.setPointer(Native.getComponentPointer(this))

        var wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE)
        wl = wl or WinUser.WS_EX_LAYERED or WinUser.WS_EX_TRANSPARENT

        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl)
    }

    override fun paint(graphics: Graphics) {
        super.paint(graphics)
        paintCross(graphics)
        paintDot(graphics)
    }

    override fun nativeMousePressed(evt: NativeMouseEvent) {
        when (evt.getButton()) {
            adsButton -> if (hideOnADS) setVisible(false)
        }
    }

    override fun nativeMouseReleased(evt: NativeMouseEvent) {
        when (evt.getButton()) {
            adsButton -> if (hideOnADS) setVisible(true)
        }
    }

    private fun paintDot(graphics: Graphics) {
        graphics.setColor(Color.GREEN)

        // center, rectangle
        graphics.fillRect(offset, offset, thickness, thickness)

        // center, open circle
        graphics.drawOval(offset - 1, offset - 1, 3, 3)
    }

    private fun paintCross(graphics: Graphics) {
        graphics.setColor(Color.red)

        // left
        graphics.fillRect(0, offset, center - 6, thickness)

        // right
        graphics.fillRect(center + 6, offset, center - 6, thickness)

        // top
        // g.fillRect(y, 0, thinkness, center - 4)

        // bottom
        graphics.fillRect(offset, center + 6, thickness, center - 6)
    }
}

fun printMessageWithPrefix(message: String, prefix: String = "INFO") {
    println("[$prefix] $message")
}

fun main(args: Array<String>) {
    printMessageWithPrefix("cli args ${args.contentToString()}")

    try {
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeMouseListener(App())
    } catch (ex: NativeHookException) {
        System.err.println("There was a problem registering the native hook.")
        System.err.println(ex.message)

        System.exit(1)
    }
}
