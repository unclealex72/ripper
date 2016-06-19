package commands

import java.nio.file.Path

import file.Implicits._

/**
  * Created by alex on 19/06/16.
  */
trait TvTargetFileProvider extends TargetFileProvider[TvEpisode] with FilenameSanitiser {

  def toFile(root: Path)(tvEpisode: TvEpisode): Path = {
    root / "TV" / tvEpisode.name / f"season_${tvEpisode.season}%02d" / f"${sanitise(tvEpisode.name)}%s_s${tvEpisode.season}%02de${tvEpisode.episode}%02d.$extension"
  }
}
