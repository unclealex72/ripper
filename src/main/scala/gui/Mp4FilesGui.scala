package gui

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{BorderPane, Pane}

/**
 * Created by alex on 13/05/15.
 */
trait Mp4FilesGui extends Mp4FilesModel {

    lazy val filesNode: Node = new Pane {
      // Define the table columns
      val fileColumn = new TableColumn[Mp4File, String] {
        text = "File"
        cellValueFactory = {
          _.value.file
        }
        prefWidth = 900
      }

      // Compose the table
      val filesTable = new TableView[Mp4File](filesModel) {
        columns += fileColumn
        editable = false
      }

      children = new BorderPane {
        padding = Insets(10, 10, 10, 10)
        top = new Label("Files")
        center = filesTable
      }
    }
}
