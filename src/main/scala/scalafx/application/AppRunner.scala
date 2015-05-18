package scalafx.application

import javafx.{application => jfxca}

class AppRunner(val jfxAppFactory: () => JFXApp) extends AppHelper {

  def run = {
    val app = jfxAppFactory()
    JFXApp.ACTIVE_APP = app
    jfxca.Application.launch(this.getClass)
  }
}