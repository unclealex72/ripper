package gui

import java.nio.file.Path
import file.Implicits._
import makemkv._

/**
 * Created by alex on 13/05/15.
 */
class FilmConvertGui extends AbstractGui("Film DVD Converter") with FilmGui with ConvertGui[FilmTitle, Film] {

  override def targetMkvName(iso: Path): String = iso.<.name

}
