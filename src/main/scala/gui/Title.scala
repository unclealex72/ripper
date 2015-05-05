package gui

import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

import scalafx.beans.property.{ObjectProperty, ReadOnlyStringProperty}

import duration.Implicits._

/**
 * A class to model a title on a DVD
 * Created by alex on 04/05/15.
 */
class Title(_id: String, _length: Period, _season: Option[Int] = None, _episode: Option[Int] = None) {


  val id = new ReadOnlyStringProperty(this, "id", _id)
  val length = new ReadOnlyStringProperty(this, "length", _length.format)
  val season = new ObjectProperty(this, "season", _season)
  val episode = new ObjectProperty(this, "episode", _episode)
}
