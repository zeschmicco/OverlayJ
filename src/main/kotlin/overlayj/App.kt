package overlayj

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import com.github.weisj.darklaf.LafManager
import java.awt.Desktop
import java.net.URI
import kotlin.system.exitProcess

class App : NativeMouseListener, NativeKeyListener {
    private val settings = Settings()
    private val crosshair = Crosshair(settings)

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

        settings.isVisible = true
    }

    override fun nativeKeyTyped(evt: NativeKeyEvent) {
        when (evt.keyChar) {
            'X' -> {
                if (evt.modifiers == 9)
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

fun main() {
    try {
        GlobalScreen.registerNativeHook()

        //FlatLightLaf.setup()
        LafManager.setDecorationsEnabled(true)
        LafManager.installTheme(LafManager.getPreferredThemeStyle())

        App()
    } catch (ex: NativeHookException) {
        System.err.println("There was a problem registering the native hook.")
        System.err.println(ex.message)
        exitProcess(1)
    }
}
