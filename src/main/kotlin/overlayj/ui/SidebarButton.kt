package overlayj.ui

import java.awt.Color
import java.awt.Insets
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.SwingConstants

class SidebarButton(label: String, actionListener: ActionListener) : JButton(label) {
    init {
        isFocusable = false
        font = font.deriveFont(14f)
        foreground = Color.ORANGE
        background = Color(0, 0, 0, 0)
        margin = Insets(2, 0, 0, 0)
        setHorizontalAlignment(SwingConstants.LEFT)

        addActionListener {
            actionListener.actionPerformed(it)
        }
    }
}

