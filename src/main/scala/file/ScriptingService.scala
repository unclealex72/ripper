package file

import java.nio.file.Path

import commands.RipType
import commands.makemkv.ISO

import scala.collection.SortedSet

/**
 * Created by alex on 10/05/15.
 */
trait ScriptingService[RIPTYPE <: RipType] {

  def listUnscriptedISOs(rootDirectory: Path): SortedSet[Path]

  def script(dvdSource: ISO, tvEpisodes: Seq[RIPTYPE], targetDirectory: Path): Unit
}
