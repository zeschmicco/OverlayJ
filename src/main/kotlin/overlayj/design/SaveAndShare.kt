package overlayj.design

import org.greenrobot.eventbus.EventBus
import overlayj.DesignSaveEvent
import overlayj.config.ConfigCrosshair
import java.awt.*
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextField

class SaveAndShare(config: ConfigCrosshair) : JPanel(GridBagLayout()) {
    private var nameField: JTextField

    init {
        JPanel(BorderLayout()).apply { // name
            nameField = JTextField(config.name).also {
                it.columns = 48
                it.horizontalAlignment = JTextField.LEFT
                add(it, BorderLayout.CENTER)
            }
        }.also {
            add(it, GridBagConstraints().apply {
                fill = GridBagConstraints.HORIZONTAL
                weightx = 1.0
                gridx = 0
            })
        }
        JPanel(FlowLayout(FlowLayout.LEFT)).apply { // buttons
            JButton("Save").apply {
                background = Color.ORANGE
                foreground = Color.BLACK
                isFocusable = false

                addActionListener {
                    EventBus.getDefault().post(DesignSaveEvent(nameField.text))
                }
            }.also {
                add(it)
            }
            JButton("Export").apply {
                background = Color.ORANGE
                foreground = Color.BLACK
                isFocusable = false
            }.also {
                add(it)
            }
        }.also {
            add(it, GridBagConstraints().apply {
                gridx = 1
            })
        }
    }

    companion object {
        val constraints = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 0
            insets = Insets(0, 10, 0, 0)
        }
    }
}