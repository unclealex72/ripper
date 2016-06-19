package commands.makemkv

import java.nio.file.Path

import commands.RipType

/**
 * Created by alex on 07/05/15.
 */
trait MakeMkvConMkv[RIPTYPE <: RipType] {

  def rip(dvdSource: DvdSource, ripTarget: RIPTYPE, targetDirectory: Path, progressListener: ProgressListener): Unit

  def script(dvdSource: ISO, ripTarget: RIPTYPE, targetDirectory: Path): Seq[String]
}
