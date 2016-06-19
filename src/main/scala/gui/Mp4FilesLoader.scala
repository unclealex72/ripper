package gui

import scalafx.application.JFXApp
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

/**
 * Created by alex on 13/05/15.
 */
trait Mp4FilesLoader extends Mp4FilesModel with JFXApp {

  def loadFiles(): Unit = {
    val fileChooser = new FileChooser {
      title = "Select movie files"
      extensionFilters.addAll(new ExtensionFilter("MP4 Files", "*.mp4"))
    }
    val maybeFiles = Option(fileChooser.showOpenMultipleDialog(this.stage))
    maybeFiles.foreach {
      files =>
        val sortedFiles = files.sortBy { file =>
          val name = file.getName
          (name.length, name)
        }
        filesModel.clear
        filesModel ++= sortedFiles.map(Mp4File.apply)
    }
  }
}
