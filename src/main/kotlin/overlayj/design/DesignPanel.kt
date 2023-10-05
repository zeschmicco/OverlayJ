package overlayj.design

import overlayj.Constants.Companion.BORDER_COLOR
import overlayj.config.ConfigCrosshair
import java.awt.GridBagLayout
import javax.swing.JPanel
import javax.swing.border.MatteBorder

class DesignPanel(config: ConfigCrosshair) : JPanel(GridBagLayout()) {
    init {
        setBorder()

        SaveAndShare(config)
            .also {
                add(it, SaveAndShare.constraints)
            }

        LayerSelector(config.layers)
            .also {
                add(it, LayerSelector.constraints)
            }

        LayerEditor(config.layers)
            .also {
                add(it, LayerEditor.constraints)
            }
    }

    private fun setBorder() {
        border = MatteBorder(1, 0, 0, 0, BORDER_COLOR)
    }
}