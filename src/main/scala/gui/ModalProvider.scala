package gui

import javafx.{concurrent => jfxc, event => jfxe}

import makemkv.ProgressListener

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.concurrent.Task
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}

/**
 * Created by alex on 13/05/15.
 */
trait ModalProvider extends JFXApp {

  def modal(action: ProgressListener => (String => Unit) => Unit): Future[Unit] = {
    val modalStage = new Stage()
    val progressBarCurrent = new ProgressTextBar {
      progress = 0.0
      minWidth = 300.0
    }
    val progressBarTotal = new ProgressTextBar {
      progress = 0.0
      minWidth = 300.0
    }

    modalStage.scene = new Scene {
      content = new VBox {
        children = List(progressBarCurrent, progressBarTotal)
        spacing = 10
        padding = Insets(10, 10, 10, 10)
      }
    }
    modalStage.initModality(Modality.APPLICATION_MODAL)
    modalStage.initOwner(stage)
    modalStage.show

    val progressListener = new ProgressListener {
      override def onStart: Unit = {}
      override def onProgressUpdated(current: Int, total: Int, max: Int): Unit = {
        progressBarCurrent.progress = current.asInstanceOf[Double] / max
        progressBarTotal.progress = total.asInstanceOf[Double] / max
      }
      override def onFinish: Unit = {
      }
      override def onMessage(message: String): Unit = {}
    }
    val task = new jfxc.Task[Unit] {
      def call = {
        val titleUpdater = (text: String) => updateTitle(text)
        action(progressListener)(titleUpdater)
      }
    }
    modalStage.title <== task.title
    task.onSucceeded = handle {
      modalStage.close
    }
    Future {
      task.run()
    }
  }

}