package overlayj

import overlayj.tray.AboutMenuItem
import overlayj.tray.QuitMenuItem
import overlayj.tray.SettingsMenuItem
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter

class Systray(actionListener: ActionListener) :
    TrayIcon(getImage(), "OverlayJ", SystrayMenu(actionListener)) {
    init {
        setImageAutoSize(true)
        addMouseListener(SystrayIconMouseListener())
        SystemTray.getSystemTray().add(this)
    }

    companion object {
        fun getImage(): Image {
            val imageURL = ::Systray.javaClass.getResource("icon.png")
            return Toolkit.getDefaultToolkit().getImage(imageURL)
        }
    }
}

private class SystrayIconMouseListener : MouseAdapter() {
//    override fun mouseClicked(e: MouseEvent?) {
//        listener.actionPerformed(SystrayActionEvent("show_settings"))
//    }
}

private class SystrayMenu(listener: ActionListener) : PopupMenu() {
    init {
        add(SettingsMenuItem(listener))
        addSeparator()
        add(AboutMenuItem(listener))
        add(QuitMenuItem(listener))
    }
}
