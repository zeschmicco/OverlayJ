package overlayj.design

import overlayj.Constants.Companion.BORDER_COLOR
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.EmptyBorder
import javax.swing.border.MatteBorder

abstract class BaseEditor() : JPanel(
    BorderLayout()
) {
    init {
        setBorder()
    }

    private fun setBorder() {
        val padding = 10
        border = BorderFactory.createCompoundBorder(
            MatteBorder(1, 1, 1, 1, BORDER_COLOR),
            EmptyBorder(padding, padding, padding, padding)
        )
    }
}