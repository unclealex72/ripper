package gui

import java.nio.file.{Path, Paths}

import commands.RipType
import gui.AutoComplete._
import commands.makemkv.{DVD, MakeMkvConMkv, ProgressListener}
import suggestions.{LookupProvider, SuggestionProvider}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.sys.process._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{GridPane, Pane}
import scalafx.stage.{DirectoryChooser, Stage}
/**
 * Created by alex on 14/05/15.
 */
trait RipGui[TITLE <: Title, RIPTYPE <: RipType] extends JFXApp with DvdLoader[TITLE] {

  self: SuggestionProvider with LookupProvider =>

  val makeMkvConMkv: MakeMkvConMkv[RIPTYPE]

  lazy val loadButton = new Button("Load DVD") {
    onAction = handle {
      loadDvdInfo(DVD)
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

  lazy val topPane: Pane = new GridPane {
    hgap = 10
    vgap = 10
    padding = Insets(10, 10, 10, 10)
    add(loadButton, 0, 0, 2, 1)
    add(new Label("Name:"), 0, 1)
    add(nameField, 1, 1)
    add(lookupButton, 2, 1)
    add(directoryButton, 0, 2)
    add(directoryField, 1, 2)
  }

  lazy val actionButtons = Seq(
    new Button("Eject") {
      onAction = handle {
        ejectDvd
      }
    },
    new Button("Rip") {
      onAction = handle {
        rip(stage)
      }
    }
  )

  def rip(stage: Stage) = {
    val rips: Seq[RIPTYPE] = ripTypes(nameField.text.value)
    val targetDirectory: Path = Paths.get(directoryField.text.value)
    val action = { (progressListener: ProgressListener) => (titleUpdater: String => Unit) =>
      rips.foreach { ripType =>
        titleUpdater(s"Ripping $ripType")
        makeMkvConMkv.rip(DVD, ripType, targetDirectory, progressListener)
      }
    }
    for {
      rip <- modal(action)
      eject <- ejectDvd
    } yield (rip, eject)
  }

  def ripTypes(name: String): Seq[RIPTYPE]

  def ejectDvd = Future {
    Seq("eject", "/dev/sr0").!<
  }
}
