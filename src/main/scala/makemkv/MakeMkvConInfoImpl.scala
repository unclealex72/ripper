package makemkv

import duration.Implicits._
import org.joda.time.Period

/**
 * Created by alex on 05/05/15.
 */
class MakeMkvConInfoImpl(override val makeMkvConCommand: MakeMkvConCommand) extends MakeMkvConParser[InfoLine](makeMkvConCommand) with MakeMkvConInfo {

  val discInfoRegexp = """CINFO:2,[0-9]+,"(.+?)"""".r
  val trackInfoRegexp = """TINFO:([0-9]+),([0-9]+),[0-9]+,"(.+?)"""".r

  override def execute(progressListener: ProgressListener): DiscInfo = {
    val infoLines = produceOutput(progressListener, Seq("info", "disc:0"))
    val titleIndicies = infoLines.flatMap {
      case TrackInfoLine(id) => Some(id)
      case _ => None
    }.toSet.toSeq.sorted
    val name = infoLines.flatMap {
      case DiscNameInfoLine(name) => Some(name)
      case _ => None
    }.headOption

    val titles = titleIndicies.flatMap { titleIndex =>
      val trackInfos = infoLines.filter {
        case TrackInfoLine(id) => id == titleIndex
        case _ => false
      }
      for {
        chapterCount <- ChapterCountInfoLine.from(trackInfos)
        duration <- DurationInfoLine.from(trackInfos)
        size <- SizeInfoLine.from(trackInfos)
        titleId <- TitleIdInfoLine.from(trackInfos)
        segmentCount <- SegmentCountInfoLine.from(trackInfos)
      } yield TitleInfo(titleId, chapterCount, duration, size, segmentCount)
    }
    DiscInfo(name.get, titles)
  }

  override def parse(str: String): Option[InfoLine] = {
    str match {
      case discInfoRegexp(name) => Some(DiscNameInfoLine(name))
      case trackInfoRegexp(id_, commandId, value) => {
        val id = id_.toInt
        commandId.toInt match {
          case 8 => Some(ChapterCountInfoLine(id, value.toInt))
          case 9 => Some(DurationInfoLine(id, value.toPeriod))
          case 11 => Some(SizeInfoLine(id, value.toLong))
          case 24 => Some(TitleIdInfoLine(id, value.toInt))
          case 25 => Some(SegmentCountInfoLine(id, value.toInt))
          case _ => None
        }
      }
      case _ => None
    }
  }

}

sealed trait InfoLine

case class DiscNameInfoLine(name: String) extends InfoLine

sealed abstract class TrackInfoLine[A](val id: Int, val value: A) extends InfoLine
object TrackInfoLine {
  def unapply(trackInfoLine: TrackInfoLine[Any]): Some[Int] = Some(trackInfoLine.id)
}

case class ChapterCountInfoLine(override val id: Int, override val value: Int) extends TrackInfoLine[Int](id, value)
object ChapterCountInfoLine {
  def from(trackInfoLines: Seq[InfoLine]): Option[Int] = trackInfoLines.flatMap {
    case ChapterCountInfoLine(_, value) => Some(value)
    case _ => None
  }.headOption
}
case class DurationInfoLine(override val id: Int, override val value: Period) extends TrackInfoLine[Period](id, value)
object DurationInfoLine {
  def from(trackInfoLines: Seq[InfoLine]): Option[Period] = trackInfoLines.flatMap {
    case DurationInfoLine(_, value) => Some(value)
    case _ => None
  }.headOption
}
case class SizeInfoLine(override val id: Int, override val value: Long) extends TrackInfoLine[Long](id, value)
object SizeInfoLine {
  def from(trackInfoLines: Seq[InfoLine]): Option[Long] = trackInfoLines.flatMap {
    case SizeInfoLine(_, value) => Some(value)
    case _ => None
  }.headOption
}
case class TitleIdInfoLine(override val id: Int, override val value: Int) extends TrackInfoLine[Int](id, value)
object TitleIdInfoLine {
  def from(trackInfoLines: Seq[InfoLine]): Option[Int] = trackInfoLines.flatMap {
    case TitleIdInfoLine(_, value) => Some(value)
    case _ => None
  }.headOption
}
case class SegmentCountInfoLine(override val id: Int, override val value: Int) extends TrackInfoLine[Int](id, value)
object SegmentCountInfoLine {
  def from(trackInfoLines: Seq[InfoLine]): Option[Int] = trackInfoLines.flatMap {
    case SegmentCountInfoLine(_, value) => Some(value)
    case _ => None
  }.headOption
}
