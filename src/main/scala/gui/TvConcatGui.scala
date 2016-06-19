package gui

import com.typesafe.scalalogging.StrictLogging
import commands.{TvEpisode, TvTargetFileProvider}
import commands.mp4box.{AbstractMp4Box, Mp4Box}
import suggestions.TvSuggestionAndLookupProvider

import scala.util.{Failure, Success, Try}
import scalafx.scene.control.TextField

/**
  * Created by alex on 14/05/15.
  */
class TvConcatGui extends AbstractConcatGui("TV Concatenator") with Mp4FilesGui with ConcatGui[TvTitle, TvEpisode] with TvSuggestionAndLookupProvider with StrictLogging {

  val mp4Box: Mp4Box[TvEpisode] = new AbstractMp4Box[TvEpisode] with TvTargetFileProvider

  def showEpisodeFields: Boolean = true

  override def ripType(name: String): Option[TvEpisode] = {
    def trim(str: String): Option[String] = Some(str.trim).filterNot(_.isEmpty)
    def toInt(field: TextField): Option[Int] = {
      val text: String = field.text.value
      Try(text.toInt) match {
        case Success(i) => Some(i)
        case _ =>
          logger.warn(s"""Cannot parse "$text" as an integer.""")
          None
      }
    }
    for {
      tvName <- trim(name)
      season <- toInt(seasonField)
      episode <- toInt(episodeField)
    } yield TvEpisode(0: Int, "", tvName, season, episode)
  }
}
