package gui

import makemkv._

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.Button
import scalafx.scene.layout._
import scalafx.Includes._
/**
 * Created by alex on 12/05/15.
 */
abstract class AbstractGui(val windowTitle: String) extends JFXApp with MakeMkvConCommandProvider {

  val topPane: Pane

  val titlesNode: Node

  val actionButtons: Seq[Button]

  val makeMkvConInfo: MakeMkvConInfo = new MakeMkvConInfoImpl(makeMkvConCommand)

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
        center = Anchor.fill(titlesNode)
        bottom = actionButtonBar
      }
    }
  }

}
