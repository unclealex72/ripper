package gui

import java.nio.file.Path

import commands.TvEpisode
import file.Implicits._
import commands.makemkv._

/**
 * Created by alex on 13/05/15.
 */
class TvConvertGui extends AbstractDvdGui("TV DVD Converter") with TvDvdGui with ConvertGui[TvTitle, TvEpisode] {

  override def targetMkvName(iso: Path): String = iso.<.<.name
}
