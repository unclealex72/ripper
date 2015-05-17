package gui

import java.nio.file.Path

import file.Implicits._
import file.ScriptingService
import makemkv._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, Pane, VBox}
import scalafx.stage.DirectoryChooser
/**
 * Created by alex on 10/05/15.
 */
trait ConvertGui[TITLE <: Title, RIPTYPE <: RipType] extends JFXApp with DvdLoader[TITLE] {

  val scriptingService: ScriptingService[RIPTYPE]

  val isoFiles: ObservableBuffer[Path] = ObservableBuffer[Path]()
  val currentIso = new ObjectProperty[Option[Path]](this, "currentIso", None)
  val targetDirectory = new ObjectProperty[Option[Path]](this, "targetDirectory", None)

  lazy val remainingIsosLabel = new Label {
    minWidth = 500
  }

  lazy val directoryLabel = new Label {
    minWidth = 500
  }

  lazy val directoryButton = new Button("Scan Directory") {
    onAction = handle {
      val directoryChooser = new DirectoryChooser {
        title = "Select source directory"
      }
      val selectedDirectory = Option(directoryChooser.showDialog(stage))
      selectedDirectory.foreach { selectedDirectory =>
        val path = selectedDirectory.toPath
        targetDirectory.value = Some(path)
        directoryLabel.text = path.toString
        isoFiles.clear
        isoFiles ++= scriptingService.listUnscriptedISOs(path)
      }
    }
  }

  lazy val currentIsoLabel = new Label

  lazy val topPane: Pane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10, 10, 10, 10)
    add(directoryButton, 0, 0, 2, 1)
    add(new Label("Directory:"), 0, 1)
    add(directoryLabel, 1, 1)
    add(new Label("ISO:"), 0, 2)
    add(currentIsoLabel, 1, 2)
  }

  lazy val actionButtons = Seq(
    new Button("Skip") {
      onAction = handle {
        isoFiles.remove(0)
      }
    },
    new Button("Create script") {
      onAction = handle {
        createScript
        isoFiles.remove(0)
      }
    }
  )

  isoFiles.onChange { (buf, changes) =>
    currentIso.value = isoFiles.headOption
    val size = buf.length
    val plural = if (size == 1) "" else "s"
    remainingIsosLabel.text = s"$size iso file$plural remaining"
  }

  currentIso.onChange { (_, _, optionalCurrentIso) =>
    optionalCurrentIso match {
      case Some(currentIso) => {
        currentIsoLabel.text = currentIso.toString
        loadDvdInfo(ISO(currentIso))
      }
      case _ => {
        currentIsoLabel.text = "None"
      }
    }
  }

  def createScript: Unit = {
    for {
      iso <- currentIso.value
      dir <- targetDirectory.value } {
      val name = targetMkvName(iso)
      scriptingService.script(ISO(iso), ripTypes(name), dir)
    }
  }

  def targetMkvName(iso: Path): String

  def ripTypes(name: String): Seq[RIPTYPE]
}