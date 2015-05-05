package gui

import org.joda.time.Duration

import scala.collection.SortedMap
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.TableView.TableViewSelectionModel
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.control._
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font
import scalafx.util.StringConverter

object Gui extends JFXApp {

  val titlesModel = ObservableBuffer[Title]((1 to 10).map { id =>
    new Title("" + id, Duration.standardMinutes(30 + id).toPeriod, Some(1), Some(id))
  })

  stage = new PrimaryStage {
    title = "DVD Ripper"

    scene = new Scene {

      val loadButton = new Button("Load DVD") {
        onAction = handle {
          loadDvdInfo(titlesModel)
        }
      }

      val titlesLabel = new Label("Titles") {
        font = Font("Arial", 20)
      }

      val optionalIntStringConverter = {
        val optionalIntToString: Option[Int] => String = _.map(_.toString).getOrElse("")
        val stringToOptionalInt: String => Option[Int] = { s =>
          val trimmed = if (s == null) "" else s.trim
          if (trimmed.isEmpty) None else Some(Integer.parseInt(trimmed))
        }
        StringConverter(stringToOptionalInt, optionalIntToString)
      }
      // Define the table columns
      val titleColumn = new TableColumn[Title, String] {
        text = "Title"
        cellValueFactory = {_.value.id}
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

      val autoPopulateEpisodesButton = new Button("Auto Populate") {
        disable = true
        onAction = handle {
          val selectedIndicies: IndexedSeq[Int] =
            titlesTable.selectionModel.value.selectedIndices.map(_.intValue).sorted.toIndexedSeq
          if (selectedIndicies.nonEmpty) {
            val titles = titlesTable.items.value
            val firstSelectedTitle = titles(selectedIndicies(0))
            val optionalFirstSeasonAndEpisode = for {
              season <- firstSelectedTitle.season.value
              episode <- firstSelectedTitle.episode.value
            } yield (season, episode)
            optionalFirstSeasonAndEpisode match {
              case Some((firstSeason, firstEpisode)) => {
                0 to titles.length - 1 foreach { idx =>
                  val title = titles(idx)
                  val posn = selectedIndicies.indexOf(idx)
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
      }

      val ripButton = new Button("Rip") {
        disable = true
      }
      content = new VBox {
        children = List(loadButton, autoPopulateEpisodesButton, titlesLabel, titlesTable, ripButton)
        spacing = 10
        padding = Insets(10, 10, 10, 10)
      }
    }
  }

  def loadDvdInfo(titlesModel: ObservableBuffer[Title]) = {

  }
}