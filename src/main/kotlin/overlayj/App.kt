package overlayj

import com.formdev.flatlaf.FlatLightLaf
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import java.awt.Desktop
import java.net.URI
import kotlin.system.exitProcess

class App() : NativeMouseListener, NativeKeyListener {
    private val settings = Settings(Config())
    private val crosshair = Crosshair(settings.config)

    init {
        GlobalScreen.addNativeMouseListener(this)
        GlobalScreen.addNativeKeyListener(this)

        Systray { menuEvent ->
            when (menuEvent.actionCommand) {
                "showSettings" -> {
                    settings.isVisible = true
                }

                "showAbout" -> {
                    val aboutURI = URI("https://github.com/zeschmicco/OverlayJ")
                    Desktop.getDesktop().browse(aboutURI)
                }

                "doQuit" -> {
                    exitProcess(0)
                }
            }
        }
    }

    override fun nativeKeyTyped(evt: NativeKeyEvent) {
        when(evt.keyChar) {
            'X' -> {
                if(evt.modifiers == 9)
                    crosshair.isVisible = !crosshair.isVisible
            }
        }
    }

    override fun nativeMousePressed(evt: NativeMouseEvent) {
        when (evt.button) {
            settings.config.adsButton -> if (settings.config.hideOnADS) crosshair.isVisible = false
        }
    }

    override fun nativeMouseReleased(evt: NativeMouseEvent) {
        when (evt.button) {
            settings.config.adsButton -> if (settings.config.hideOnADS) crosshair.isVisible = true
        }
    }
}

fun main(args: Array<String>) {
    try {
        GlobalScreen.registerNativeHook()
        FlatLightLaf.setup()
        App()
    } catch (ex: NativeHookException) {
        System.err.println("There was a problem registering the native hook.")
        System.err.println(ex.message)
        exitProcess(1)
    }
}
