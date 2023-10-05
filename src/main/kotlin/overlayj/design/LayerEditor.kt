package overlayj.design

import overlayj.config.ConfigCrosshairLayer
import java.awt.GridBagConstraints
import java.awt.Insets
import javax.swing.BoxLayout
import javax.swing.JPanel

class LayerEditor(layers: List<ConfigCrosshairLayer>) : JPanel() {
    init {
        val layer = layers[0]
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        add(DotEditor(layer.dot))
        add(LineEditor(layer.line))
    }

    companion object {
        val constraints = GridBagConstraints().apply {
            weightx = 1.0
            weighty = 1.0
            anchor = GridBagConstraints.LINE_START
            fill = GridBagConstraints.VERTICAL and GridBagConstraints.HORIZONTAL
            gridy = 2
            insets = Insets(0, 10, 0, 10)
        }
    }
}