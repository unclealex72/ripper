package gui

import java.io.File

import di.CommonModule
import makemkv.{MakeMkvConMkv, MakeMkvConInfo, ProgressListener}
import scaldi.Injectable._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Task
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.TableColumn._
import scalafx.scene.control._
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Font
import scalafx.stage.{DirectoryChooser, FileChooser, Modality, Stage}
import scalafx.util.StringConverter

object GuiMain extends App {

  implicit val module = new CommonModule :: new GuiModule

  inject[Gui].main(Array.empty[String])
}


class Gui(val makeMkvConInfo: MakeMkvConInfo, val makeMkvConvMkv: MakeMkvConMkv) extends JFXApp {

  stage = new PrimaryStage {
    title = "DVD Ripper"
  }

  val titlesModel = ObservableBuffer[Title]()

  val titlesLabel = new Label("Titles") {
    font = Font("Arial", 20)
  }

  val directoryField = new TextField {
    minWidth = 500
    editable = false
  }

  val directoryButton = new Button("Directory") {
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


  val nameField = new TextField {
    minWidth = 500.0
  }

  val optionalIntStringConverter = {
    val optionalIntToString: Option[Int] => String = { oi =>
      Option(oi).flatten.map(_.toString).getOrElse("")
    }
    val stringToOptionalInt: String => Option[Int] = { s =>
      val trimmed = if (s == null) "" else s.trim
      if (trimmed.isEmpty) None else Some(Integer.parseInt(trimmed))
    }
    StringConverter(stringToOptionalInt, optionalIntToString)
  }
  // Define the table columns
  val titleColumn = new TableColumn[Title, String] {
    text = "Title"
    cellValueFactory = {_.value.trackNumber}
    prefWidth = 180
  }
  val lengthColumn = new TableColumn[Title, String] {
    text = "Length"
    cellValueFactory = {_.value.length}
    prefWidth = 180
  }
  val seasonColumn = new TableColumn[Title, Option[Int]] {
    text = "Season"
    cellValueFactory = {_.value.season}
    cellFactory = _ => new TextFieldTableCell[Title, Option[Int]] (optionalIntStringConverter)
    prefWidth = 180
    editable = true
  }
  val episodeColumn = new TableColumn[Title, Option[Int]] {
    text = "Episode"
    cellValueFactory = {_.value.episode}
    cellFactory = _ => new TextFieldTableCell[Title, Option[Int]] (optionalIntStringConverter)
    prefWidth = 180
    editable = true
  }

  // Compose the table
  val titlesTable = new TableView[Title](titlesModel) {
    columns +=(titleColumn, lengthColumn, seasonColumn, episodeColumn)
    editable = true
    selectionModel.value.setSelectionMode(SelectionMode.MULTIPLE)
  }

  def selectedIndicies: IndexedSeq[Int] =
    titlesTable.selectionModel.value.selectedIndices.map(_.intValue).toSet.toSeq.sorted.toIndexedSeq

  val loadButton = new Button("Load DVD") {
    onAction = handle {
      loadDvdInfo(stage)
    }
  }

  val autoPopulateEpisodesButton = new Button("Auto Populate") {
    onAction = handle {
      autoPopulate
    }
  }

  val ripButton = new Button("Rip") {
    onAction = handle {
      rip(stage)
    }
  }

  stage.scene = new Scene {
    content = new VBox {
      children = List(
        loadButton,
        new HBox { children = List(new Label("Name:"), nameField) },
        new HBox { children = List(directoryButton, directoryField) },
        autoPopulateEpisodesButton,
        titlesLabel,
        titlesTable,
        ripButton)
      spacing = 10
      padding = Insets(10, 10, 10, 10)
    }
  }

  def autoPopulate: Unit = {
    val currentlySelectedIndicies = selectedIndicies
    if (currentlySelectedIndicies.nonEmpty) {
      val titles = titlesTable.items.value
      val firstSelectedTitle = titles(currentlySelectedIndicies(0))
      val optionalFirstSeasonAndEpisode = for {
        season <- firstSelectedTitle.season.value
        episode <- firstSelectedTitle.episode.value
      } yield (season, episode)
      optionalFirstSeasonAndEpisode match {
        case Some((firstSeason, firstEpisode)) => {
          0 to titles.length - 1 foreach { idx =>
            val title = titles(idx)
            val posn = currentlySelectedIndicies.indexOf(idx)
            if (posn < 0) {
              title.season.value = None
              title.episode.value = None
            }
            else {
              title.season.value = Some(firstSeason)
              title.episode.value = Some(firstEpisode + posn)
            }
          }
        }
        case _ => {}
      }
    }
  }

  def loadDvdInfo(stage: Stage) = {
    modal(stage, false, { progressListener => titleUpdater =>
      titleUpdater("Scanning DVD")
      titlesModel.clear
      titlesModel ++= makeMkvConInfo.info(progressListener).titles.map { titleInfo =>
        new Title(titleInfo.id, titleInfo.trackNumber, titleInfo.duration, titleInfo.target)
      }
    })
  }

  def rip(stage: Stage) = {
    val episodeTitles = for {
      title <- titlesModel.toList
      season <- title.season.value
      episode <- title.episode.value
    } yield (title, season, episode)
    val targetDirectory: File = new File(directoryField.text.value)
    val name: String = nameField.text.value
    val action = { (progressListener: ProgressListener) => (titleUpdater: String => Unit) =>
      episodeTitles.foreach { case (title, season, episode) =>
        titleUpdater(s"Ripping season $season, episode $episode")
        makeMkvConvMkv.rip(title.id, targetDirectory, title.target, name, season, episode, progressListener)
      }
    }
    modal(stage, true, action)
  }

  def modal(parentStage: Stage, useDoneButton: Boolean, action: ProgressListener => (String => Unit) => Unit): Unit = {
    val stage = new Stage()
    val progressBarCurrent = new ProgressBar {
      progress = 0.0
      minWidth = 300.0
    }
    val progressBarTotal = new ProgressBar {
      progress = 0.0
      minWidth = 300.0
    }
    val button = new Button("Done") {
      disable = true
      onAction = handle {
        stage.close
      }
    }
    stage.scene = new Scene {
      content = new VBox {
        children = List(progressBarCurrent, progressBarTotal) ++ Some(button).filter(_ => useDoneButton)
        spacing = 10
        padding = Insets(10, 10, 10, 10)
      }
    }
    stage.initModality(Modality.WINDOW_MODAL)
    stage.initOwner(stage.scene.window.get)
    stage.show
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
    val task = Task {
      val titleUpdater = (title: String) => stage.title = title
      action(progressListener)(titleUpdater)
    }
    task.onSucceeded = handle {
      if (useDoneButton) {
        button.disable = false
      }
      else {
        stage.close
      }
    }
    Future {
      task.run()
    }
  }
}