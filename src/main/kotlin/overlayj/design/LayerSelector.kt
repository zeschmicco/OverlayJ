package overlayj.design

import overlayj.config.ConfigCrosshairLayer
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JPanel

class LayerSelector(layers: List<ConfigCrosshairLayer>) : JPanel(FlowLayout()) {
    init {
        layers.forEachIndexed { index, _ ->
            JButton("#$index").apply {
                addActionListener { }
            }.also {
                add(it)
            }
        }
        add(JButton("[+]"))
    }

    companion object {
        val constraints = GridBagConstraints().apply {
            weightx = 1.0
            weighty = 0.0
            anchor = GridBagConstraints.LINE_START
            gridy = 1
            insets = Insets(0, 5, 0, 0)
        }
    }
}