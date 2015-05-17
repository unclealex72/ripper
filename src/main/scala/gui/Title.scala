package gui

import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

import scalafx.beans.property.{ObjectProperty, ReadOnlyStringProperty}

import duration.Implicits._

/**
 * A class to model a title on a DVD
 * Created by alex on 04/05/15.
 */
sealed class Title(val id: Int, _titleNumber: Int, _length: Period, val target: String) extends Ordered[Title] {


  val trackNumber = new ReadOnlyStringProperty(this, "trackNumber", _titleNumber.toString)
  val length = new ReadOnlyStringProperty(this, "length", _length.format)
  val periodLength = _length.toStandardDuration.getMillis

  override def compare(that: Title): Int = id - that.id
}

/**
 * A class to model a title on a movie DVD
 * Created by alex on 04/05/15.
 */
class FilmTitle(override val id: Int, _titleNumber: Int, _length: Period, override val target: String) extends Title(id, _titleNumber, _length, target)

/**
 * A class to model a title on a DVD with added TV episode information
 * Created by alex on 04/05/15.
 */
class TvTitle(override val id: Int, _titleNumber: Int, _length: Period, override val target: String, _season: Option[Int] = None, _episode: Option[Int] = None) extends Title(id, _titleNumber, _length, target) {


  val season = new ObjectProperty(this, "season", _season)
  val episode = new ObjectProperty(this, "episode", _episode)
}
