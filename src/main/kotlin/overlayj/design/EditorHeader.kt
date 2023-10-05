package overlayj.design

import javax.swing.JLabel
import javax.swing.border.EmptyBorder

class EditorHeader(label: String) : JLabel(label) {
    init {
        font = font.deriveFont(16f)
        border = EmptyBorder(0, 0, 10, 0)
    }
}