package gui

import scaldi.Module

/**
 * Created by alex on 06/05/15.
 */
class GuiModule extends Module {

  bind [Gui] to injected [Gui]
}
