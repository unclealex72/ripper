package gui

import commands.TvEpisode
import file.{ScriptingService, ScriptingServiceImpl}
import commands.makemkv._

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.layout.{BorderPane, Pane}
import scalafx.util.StringConverter

/**
 * Created by alex on 13/05/15.
 */
trait TvDvdGui extends TitlesModel[TvTitle] {

  self: MakeMkvConCommandProvider =>

  val makeMkvConMkv: MakeMkvConMkv[TvEpisode] = new TvMakeMkvConMkv(makeMkvConCommand)

    val scriptingService: ScriptingService[TvEpisode] = new ScriptingServiceImpl(makeMkvConMkv)

    lazy val titlesNode: Node = new Pane {
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

      val autoPopulateEpisodesButton = new Button("Auto Populate") {
        onAction = handle {
          autoPopulate
        }
      }

      // Define the table columns
      val titleColumn = new TableColumn[TvTitle, String] {
        text = "Title"
        cellValueFactory = {
          _.value.trackNumber
        }
        prefWidth = 180
      }
      val lengthColumn = new TableColumn[TvTitle, String] {
        text = "Length"
        cellValueFactory = {
          _.value.length
        }
        prefWidth = 180
      }
      val seasonColumn = new TableColumn[TvTitle, Option[Int]] {
        text = "Season"
        cellValueFactory = {
          _.value.season
        }
        cellFactory = _ => new TextFieldTableCell[TvTitle, Option[Int]](optionalIntStringConverter)
        prefWidth = 180
        editable = true
      }
      val episodeColumn = new TableColumn[TvTitle, Option[Int]] {
        text = "Episode"
        cellValueFactory = {
          _.value.episode
        }
        cellFactory = _ => new TextFieldTableCell[TvTitle, Option[Int]](optionalIntStringConverter)
        prefWidth = 180
        editable = true
      }

      // Compose the table
      val titlesTable = new TableView[TvTitle](titlesModel) {
        columns +=(titleColumn, lengthColumn, seasonColumn, episodeColumn)
        editable = true
        selectionModel.value.setSelectionMode(SelectionMode.MULTIPLE)
      }

      children = new BorderPane {
        padding = Insets(10, 10, 10, 10)
        top = new Label("Titles")
        center = titlesTable
        bottom = new Button("Auto-populate") {
          onAction = handle {
            autoPopulate()
          }
        }
        BorderPane.setMargin(bottom.value, Insets(10, 0, 0, 0))
      }

      def autoPopulate(): Unit = {
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
              0 until titles.length foreach { idx =>
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

      def selectedIndicies: IndexedSeq[Int] =
        titlesTable.selectionModel.value.selectedIndices.map(_.intValue).toSet.toSeq.sorted.toIndexedSeq
    }

    def ripTypes(name: String): Seq[TvEpisode] = {
      for {
        title <- titlesModel.toList
        season <- title.season.value
        episode <- title.episode.value
      } yield TvEpisode(title.id, title.target, name, season, episode)
    }

    def generateTitle(titleInfo: TitleInfo): TvTitle = {
      new TvTitle(titleInfo.id, titleInfo.trackNumber, titleInfo.duration, titleInfo.target)
    }
}
