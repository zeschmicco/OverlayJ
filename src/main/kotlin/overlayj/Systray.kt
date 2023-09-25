package overlayj

import java.awt.AWTException
import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

class SystrayIcon : TrayIcon(Companion.getImage(), "OverlayJ", SystrayMenu()) {
    init {
        setImageAutoSize(true)
    }
    
    companion object {
        fun getImage(): Image {
            val imageURL = ::SystrayIcon.javaClass.getResource("icon.png")
            return Toolkit.getDefaultToolkit().getImage(imageURL)
        }
    }
}

class SystrayMenu() : PopupMenu("systray menu") {
    init {
        add(MenuItem("About").also { it.addActionListener { about() } })
        add(MenuItem("Quit").also { it.addActionListener { exit() } })
    }
}

fun enableSystray() {
    try {
        SystemTray.getSystemTray().add(SystrayIcon())
    } catch (e: AWTException) {
        e.printStackTrace()
        exit(1)
    }
}

fun about() {
    // TODO: open browser with github repo
}

fun exit(code: Int = 0) {
    System.exit(code)
}
