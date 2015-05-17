package gui

import scalafx.beans.property.DoubleProperty
import scalafx.scene.control.ProgressBar
import scalafx.scene.layout.StackPane
import scalafx.scene.text.Text

/**
 * Created by alex on 16/05/15.
 */
class ProgressTextBar(val defaultLabelPadding: Int = 5) extends StackPane {

  val _progress = new DoubleProperty(this, "progress", 0)
  def progress = _progress
  def progress_=(v: Double) {
    _progress() = v
  }

  val bar = new ProgressBar
  val text = new Text

  progress onChange syncProgress

  bar.maxWidth = Double.MaxValue
  children = List(bar, text)
  syncProgress

  def updateTo(txt: String, progress: Double) {
    text.text = txt
    bar.progress = progress
  }

  def syncProgress {
    text.text = s"${Math.ceil(progress.value * 100).toInt}%"
    bar.progress = progress.value
    bar.minHeight = text.getBoundsInLocal().getHeight() + defaultLabelPadding * 2
    bar.minWidth = text.getBoundsInLocal().getWidth()  + defaultLabelPadding * 2
  }
}
