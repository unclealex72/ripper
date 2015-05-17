package gui

import file.{ScriptingServiceImpl, ScriptingService}
import makemkv._

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, Pane}

/**
 * Created by alex on 13/05/15.
 */
trait FilmGui extends TitlesModel[FilmTitle] {

  self: MakeMkvConCommandProvider =>

  val makeMkvConMkv: MakeMkvConMkv[Film] = new FilmMakeMkvConMkv(makeMkvConCommand)

  val scriptingService: ScriptingService[Film] = new ScriptingServiceImpl(makeMkvConMkv)

  lazy val titlesTable = new TableView[FilmTitle](titlesModel)

  lazy val titlesNode: Node = new Pane {
    // Define the table columns
    val titleColumn = new TableColumn[FilmTitle, String] {
      text = "Title"
      cellValueFactory = {
        _.value.trackNumber
      }
      prefWidth = 180
    }
    val lengthColumn = new TableColumn[FilmTitle, String] {
      text = "Length"
      cellValueFactory = {
        _.value.length
      }
      prefWidth = 180
    }

    titlesTable.columns +=(titleColumn, lengthColumn)

    children = new BorderPane {
      padding = Insets(10, 10, 10, 10)
      top = new Label("Titles")
      center = titlesTable
    }

  }

  def ripTypes(name: String): Seq[Film] = {
    titlesTable.selectionModel.value.selectedItems.map { title =>
      Film(title.id, title.target, name)
    }
  }

  titlesModel.onChange {
    if (titlesModel.nonEmpty) {
      val longestTitle = titlesModel.maxBy { titlesModel =>
        titlesModel.periodLength
      }
      titlesTable.selectionModel().select(longestTitle)
    }
  }

  def generateTitle(titleInfo: TitleInfo): FilmTitle = {
    new FilmTitle(titleInfo.id, titleInfo.trackNumber, titleInfo.duration, titleInfo.target)
  }
}
