package overlayj.design

import overlayj.Settings
import overlayj.config.ConfigCrosshairLayerLine
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.event.ChangeListener

class LineEditor(line: ConfigCrosshairLayerLine) : BaseEditor() {
    init {
        EditorHeader("Lines").also { add(it, BorderLayout.NORTH) }

        JPanel()
            .apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                EditorColorPicker(line.color, line.opacity) {
                    val color = it.source as Color
                    line.color = Settings.encodeColor(color)
                    line.opacity = color.alpha
                }.also { add(it) }
                LengthSlider(line).also { add(it) }

            }.also {
                add(it, BorderLayout.CENTER)
            }
    }
}


private class LengthSlider(line: ConfigCrosshairLayerLine) : EditorSlider("Length", 1, 36, line.length) {
    override val changeListener = ChangeListener {
        line.length = slider.value
    }
}