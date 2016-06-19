package commands

import java.nio.file.Path

/**
  * A trait to convert a rip type into a final destination file.
  * Created by alex on 19/06/16.
  */
trait TargetFileProvider[RIPTYPE <: RipType] {

  val extension: String

  def toFile(root: Path)(ripType: RIPTYPE): Path
}
