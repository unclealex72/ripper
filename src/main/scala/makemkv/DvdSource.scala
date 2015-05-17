package makemkv

import java.nio.file.Path
import file.Implicits._

/**
 * The different types of DVD source understood by MakeMKV
 * Created by alex on 10/05/15.
 */
sealed trait DvdSource {

  def toCommandLineArguments: Seq[String]
}

sealed abstract class AbstractDvdSource[T](
                                            val sourceType: String,
                                            val argument: T,
                                            val formatter: T => String = (t:T) => t.toString,
                                            val options: Seq[String] = Seq.empty) extends DvdSource {
  def toCommandLineArguments: Seq[String] = options :+ s"$sourceType:${formatter(argument)}"
}

object DVD extends AbstractDvdSource[Int]("disc", 0)
case class ISO(override val argument: Path) extends AbstractDvdSource[Path]("iso", argument, _.abs.toString, Seq("--noscan"))