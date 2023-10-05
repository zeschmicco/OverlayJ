package overlayj

import com.formdev.flatlaf.util.UIScale
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import overlayj.config.ConfigCrosshair
import overlayj.config.clone
import overlayj.config.read
import overlayj.config.write
import overlayj.design.DesignPanel
import overlayj.saves.SavePanel
import overlayj.ui.Sidebar
import java.awt.*
import javax.swing.JFrame
import javax.swing.UIManager

class Settings : JFrame() {
    private val windowSize = Dimension(1024, 600)

    var config = read()
    private var currentCrosshair = config.current

    private var mainComponent: Component? = null

    init {
        printUiInformation()
        prepare()

        EventBus.getDefault().post(ConfigEvent(currentCrosshair))
        EventBus.getDefault().register(this)

        Sidebar().also {
            add(it, BorderLayout.WEST)
        }

        mainComponent = SavePanel(config.crosshairs).also {
            add(it, BorderLayout.CENTER)
        }


        pack()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onConfigChanged(evt: ConfigChangedEvent) {
        EventBus.getDefault().post(ConfigEvent(currentCrosshair))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDesignSave(evt: DesignSaveEvent) {
        println("saving current crosshair to ${evt.name}")
        val newCrosshairConfig = clone(currentCrosshair)
        config.crosshairs.add(0, newCrosshairConfig)
        write(config)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCrosshairSelection(evt: CrosshairSelectionEvent) {
        println("crosshair selection to ${evt.configCrosshair.name}")
        currentCrosshair = evt.configCrosshair
        EventBus.getDefault().post(ConfigEvent(currentCrosshair))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSidebarButton(evt: SidebarButtonClicked) {
        println("switching panel to ${evt.name}")

        if (mainComponent != null)
            contentPane.remove(mainComponent)

        when (evt.name) {
            "saved" -> {
                mainComponent = SavePanel(config.crosshairs).also {
                    add(it, BorderLayout.CENTER)
                }
            }

            "design" -> {
                mainComponent = DesignPanel(currentCrosshair).also {
                    add(it, BorderLayout.CENTER)
                }
            }

            else -> {
                println("Unknown Sidebar Button ${evt.name}")
            }
        }

        contentPane.revalidate()
        contentPane.repaint()
    }

    private fun prepare() {
        setLocationRelativeTo(null)
        minimumSize = windowSize
        isVisible = true

        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val gd = ge.defaultScreenDevice
        val dm = gd.getDisplayMode()

        val location = Point(dm.width / 2 - windowSize.width / 2, dm.height / 2 - windowSize.height / 2)

        super.setBounds(location.x, location.y, windowSize.width, windowSize.height)
    }

    private fun printUiInformation() {
        var javaVendor = System.getProperty("java.vendor")
        if ("Oracle Corporation" == javaVendor) javaVendor = null
        val systemScaleFactor: Double = UIScale.getSystemScaleFactor(graphicsConfiguration)
        val userScaleFactor = UIScale.getUserScaleFactor()
        val font = UIManager.getFont("Label.font")
        val newInfo =
            ("(Java " + System.getProperty("java.version") + (if (javaVendor != null) "; $javaVendor" else "") + (if (systemScaleFactor != 1.0) ";  system scale factor $systemScaleFactor" else "") + (if (userScaleFactor != 1f) ";  user scale factor $userScaleFactor" else "") + (if (systemScaleFactor == 1.0 && userScaleFactor == 1f) "; no scaling" else "") + "; " + font.family + " " + font.size + (if (font.isBold) " BOLD" else "") + (if (font.isItalic) " ITALIC" else "") + ")")
        println(newInfo)
    }

    companion object {
        fun encodeColor(color: Color): String {
            return String.format("#%02x%02x%02x", color.red, color.green, color.blue)
        }

        fun decodeColor(color: String, alpha: Int): Color {
            return Color(
                Integer.valueOf(color.substring(1, 3), 16),
                Integer.valueOf(color.substring(3, 5), 16),
                Integer.valueOf(color.substring(5, 7), 16),
                alpha
            )
        }
    }
}

class SidebarButtonClicked(val name: String)
class CrosshairSelectionEvent(val configCrosshair: ConfigCrosshair)
class DesignSaveEvent(val name: String)
class ConfigChangedEvent
class ConfigEvent(val config: ConfigCrosshair)