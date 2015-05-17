package gui

import scalafx.collections.ObservableBuffer

/**
 * Created by alex on 13/05/15.
 */
trait TitlesModel[TITLE <: Title] {

  val titlesModel = ObservableBuffer[TITLE]()

}
