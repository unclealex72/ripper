package commands.mp4box

import java.nio.file.Path

import commands.RipType
import commands.makemkv.ProgressListener

/**
  * Created by alex on 19/06/16.
  */
trait Mp4Box[RIPTYPE <: RipType] {

  def fileExists(rip: RIPTYPE, targetDirectory: Path): Option[Path]

  def concatenate(ripTarget: RIPTYPE, sourceFiles: Seq[Path], targetDirectory: Path, progressListener: ProgressListener): Unit

}
