package overlayj

import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.fonts.inter.FlatInterFont
import com.formdev.flatlaf.themes.FlatMacDarkLaf
import com.formdev.flatlaf.util.FontUtils
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent
import com.github.kwhat.jnativehook.mouse.NativeMouseListener
import java.awt.Desktop
import java.awt.Font
import java.net.URI
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess


class App : NativeMouseListener, NativeKeyListener {
    private val crosshair = Crosshair()
    private val settings = Settings()

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
        when (evt.keyChar) {
            'X' -> {
                if (evt.modifiers == 9) crosshair.isVisible = !crosshair.isVisible
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
    } catch (ex: NativeHookException) {
        System.err.println("There was a problem registering the native hook.")
        System.err.println(ex.message)
        exitProcess(1)
    }
    SwingUtilities.invokeLater {
        FlatMacDarkLaf.setup()
        //FlatDarkLaf.setup()
        FlatInspector.install("ctrl shift alt X")

        // use Inter font by default
        FlatInterFont.install()
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY)
        FlatLaf.setPreferredLightFontFamily(FlatInterFont.FAMILY_LIGHT)
        FlatLaf.setPreferredSemiboldFontFamily(FlatInterFont.FAMILY_SEMIBOLD)

        UIManager.put(
            "defaultFont",
            FontUtils.getCompositeFont(FlatInterFont.FAMILY, Font.PLAIN, 14)
        )

        FlatLaf.updateUI()

        App()
    }
}
