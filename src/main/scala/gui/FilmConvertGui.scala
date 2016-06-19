package gui

import java.nio.file.Path

import commands.Film
import file.Implicits._
import commands.makemkv._

/**
 * Created by alex on 13/05/15.
 */
class FilmConvertGui extends AbstractDvdGui("Film DVD Converter") with FilmDvdGui with ConvertGui[FilmTitle, Film] {

  override def targetMkvName(iso: Path): String = iso.<.name

}
