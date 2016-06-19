package gui

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.control.Button
import scalafx.scene.layout._
import scalafx.scene.{Node, Scene}

/**
 * Created by alex on 12/05/15.
 */
abstract class AbstractConcatGui(val windowTitle: String) extends JFXApp {

  val topPane: Pane

  val filesNode: Node

  val actionButtons: Seq[Button]

  stage = new PrimaryStage {
    title = windowTitle

  }

  val actionButtonBar = Anchor.right {
    new HBox {
      spacing = 10
      padding = Insets(10)
      children = actionButtons
    }
  }

  stage.scene = new Scene {
    root = Anchor.fill {
      new BorderPane {
        top = topPane
        center = Anchor.fill(filesNode)
        bottom = actionButtonBar
      }
    }
  }

}
