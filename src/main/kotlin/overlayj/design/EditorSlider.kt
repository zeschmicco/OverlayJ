package overlayj.design

import org.greenrobot.eventbus.EventBus
import overlayj.ConfigChangedEvent
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.event.ChangeListener

abstract class EditorSlider(label: String, min: Int, max: Int, initValue: Int) : JPanel(BorderLayout()) {
    abstract val changeListener: ChangeListener

    val slider: JSlider

    init {
        JLabel(label).also { add(it, BorderLayout.WEST) }

        val valueLabel = JLabel(initValue.toString()).also {
            add(it, BorderLayout.EAST)
        }

        slider = JSlider(JSlider.HORIZONTAL, min, max, initValue)
            .apply {
                addChangeListener {
                    changeListener.stateChanged(it)
                    valueLabel.text = value.toString()
                    EventBus.getDefault().post(ConfigChangedEvent())
                }
            }
            .also { add(it, BorderLayout.CENTER) }
    }
}
