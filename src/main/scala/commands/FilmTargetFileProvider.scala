package commands

import java.nio.file.Path

import file.Implicits._

/**
  * Created by alex on 19/06/16.
  */
trait FilmTargetFileProvider extends TargetFileProvider[Film] with FilenameSanitiser {

  def toFile(root: Path)(film: Film): Path = {
    root / "Films" / film.name / s"${sanitise(film.name)}.$extension"
  }
}
