package gui

import java.nio.file.Path

import file.Implicits._
import makemkv._

/**
 * Created by alex on 13/05/15.
 */
class TvConvertGui extends AbstractGui("TV DVD Converter") with TvGui with ConvertGui[TvTitle, TvEpisode] {

  override def targetMkvName(iso: Path): String = iso.<.<.name
}
