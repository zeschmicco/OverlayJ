package overlayj.design

import overlayj.Settings
import overlayj.config.ConfigCrosshairLayerDot
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.event.ChangeListener

class DotEditor(dot: ConfigCrosshairLayerDot) : BaseEditor() {
    init {
        EditorHeader("Center Dot").also { add(it, BorderLayout.NORTH) }

        JPanel()
            .apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                EditorColorPicker(dot.color, dot.opacity) {
                    val color = it.source as Color
                    dot.color = Settings.encodeColor(color)
                    dot.opacity = color.alpha
                }.also { add(it) }
                OpacitySlider(dot).also { add(it) }
                RadiusSlider(dot).also { add(it) }
            }.also {
                add(it, BorderLayout.CENTER)
            }
    }
}

private class OpacitySlider(dot: ConfigCrosshairLayerDot) :
    EditorSlider("Opacity", 0, 255, dot.opacity) {
    override val changeListener = ChangeListener {
        dot.opacity = slider.value
    }
}

private class RadiusSlider(dot: ConfigCrosshairLayerDot) : EditorSlider("Radius", 1, 16, dot.radius) {
    override val changeListener = ChangeListener {
        dot.radius = slider.value
    }
}
