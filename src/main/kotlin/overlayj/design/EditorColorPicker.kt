package overlayj.design

import org.greenrobot.eventbus.EventBus
import overlayj.ConfigChangedEvent
import overlayj.Settings
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JPanel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class EditorColorPicker(color: String, opacity: Int, changeListener: ChangeListener) : JPanel(BorderLayout()) {
    val button: JButton

    init {
        button = JButton(" ")
            .apply {
                isFocusable = false
                background = Settings.decodeColor(color, opacity)
                addActionListener {
                    background = JColorChooser.showDialog(
                        this,
                        "Choose Color",
                        background
                    )
                    changeListener.stateChanged(ChangeEvent(background))
                    EventBus.getDefault().post(ConfigChangedEvent())
                }
            }
            .also {
                add(it, BorderLayout.WEST)
            }


//            {
//                changeListener.stateChanged(it)
//                EventBus.getDefault().post(ConfigChangedEvent())
//            }
    }

//        slider = JSlider(JSlider.HORIZONTAL, min, max, initValue)
//            .apply {
//                addChangeListener {
//                    changeListener.stateChanged(it)
//                    valueLabel.text = value.toString()
//
//                }
//            }
//            .also { add(it, BorderLayout.CENTER) }
}
