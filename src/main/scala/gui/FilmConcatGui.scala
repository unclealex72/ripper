package gui

import commands.{Film, FilmTargetFileProvider, TvEpisode, TvTargetFileProvider}
import commands.mp4box.{AbstractMp4Box, Mp4Box}
import suggestions.FilmSuggestionAndLookupProvider

/**
  * Created by alex on 14/05/15.
  */
class FilmConcatGui extends AbstractConcatGui("Film Concatenator") with Mp4FilesGui with ConcatGui[FilmTitle, Film] with FilmSuggestionAndLookupProvider {

  val mp4Box: Mp4Box[Film] = new AbstractMp4Box[Film] with FilmTargetFileProvider
  def showEpisodeFields: Boolean = false

  override def ripType(name: String): Option[Film] =
    Some(name.trim).filterNot(_.isEmpty).map(new Film(0, "", _))
}
