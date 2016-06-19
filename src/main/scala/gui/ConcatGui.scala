package gui

import java.nio.file.{Path, Paths}

import commands.RipType
import gui.AutoComplete._
import commands.makemkv.{DVD, MakeMkvConMkv, ProgressListener}
import commands.mp4box.Mp4Box
import suggestions.{LookupProvider, SuggestionProvider}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.sys.process._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, Pane}
import scalafx.stage.{DirectoryChooser, Stage}

/**
 * Created by alex on 14/05/15.
 */
trait ConcatGui[TITLE <: Title, RIPTYPE <: RipType] extends ModalProvider with Mp4FilesLoader {

  self: SuggestionProvider with LookupProvider =>

  val mp4Box: Mp4Box[RIPTYPE]
  def showEpisodeFields: Boolean

  lazy val filesButton = new Button("Select files") {
    onAction = handle {
      loadFiles()
    }
  }

  lazy val directoryField = new TextField {
    minWidth = 500
    editable = false
  }

  lazy val directoryButton = new Button("Directory") {
    onAction = handle {
      val directoryChooser = new DirectoryChooser {
        title = "Select target directory"
      }
      val selectedDirectory = Option(directoryChooser.showDialog(stage))
      selectedDirectory.foreach { selectedDirectory =>
        directoryField.text = selectedDirectory.toString
      }
    }
  }

  lazy val nameField = new TextField {
    minWidth = 500.0
   } autoComplete(3, suggestions)

  lazy val lookupButton = new Button("Lookup") {
    onAction = handle {
      lookupUrl(nameField.text.value).foreach { url =>
        Future(Seq("google-chrome", url.toString).!)
      }
    }
  }

  lazy val seasonField = new TextField

  lazy val episodeField = new TextField

  lazy val topPane: Pane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10, 10, 10, 10)
    add(filesButton, 0, 0, 2, 1)
    add(new Label("Name:"), 0, 1)
    add(nameField, 1, 1)
    add(lookupButton, 2, 1)
    add(directoryButton, 0, 2)
    add(directoryField, 1, 2)
    if (showEpisodeFields) {
      add(new Label("Season:"), 0, 3)
      add(seasonField, 1, 3)
      add(new Label("Episode:"), 0, 4)
      add(episodeField, 1, 4)
    }
  }

  lazy val actionButtons = Seq(
    new Button("Concatenate") {
      onAction = handle {
        concatenate()
      }
    }
  )

  def concatenate() = {
    ripType(nameField.text.value).foreach {
      rip =>
        val targetDirectory: Path = Paths.get(directoryField.text.value)
        val overwrite: Path => Boolean = path => {
          val alert = new Alert(AlertType.Confirmation) {
            initOwner(stage)
            title = "File already exists"
            headerText = s"File $path already exists"
            contentText = "Overwrite?"
          }
          val result = alert.showAndWait()
          result match {
            case Some(ButtonType.OK) => true
            case _ => false
          }
        }
        val existingFile = mp4Box.fileExists(rip, targetDirectory)
        val continue = existingFile match {
          case Some(existing) => overwrite(existing)
          case None => true
        }
        if (continue) {
          val action = { (progressListener: ProgressListener) => (titleUpdater: String => Unit) =>
            titleUpdater(s"Concatenating $rip")
            mp4Box.concatenate(rip, filesModel.map(_._file.toPath), targetDirectory, progressListener)
          }
          modal(action)
        }
    }
  }

  def ripType(name: String): Option[RIPTYPE]
}
