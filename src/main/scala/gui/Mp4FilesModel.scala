package gui

import scalafx.collections.ObservableBuffer

/**
 * Created by alex on 13/05/15.
 */
trait Mp4FilesModel {

  val filesModel = ObservableBuffer[Mp4File]()

}
