package makemkv

/**
 * Created by alex on 05/05/15.
 */
abstract class MakeMkvConParser[A](val makeMkvConCommand: MakeMkvConCommand) {

  val progressRegexp = """PRGV:([0-9]+),([0-9]+),([0-9]+)""".r

  def produceOutput(progressListener: ProgressListener, arguments: Seq[String]): Seq[A] = {
    makeMkvConCommand.execute(arguments).foldLeft(Seq.empty[A]){ (seq, str) =>
      str match {
        case progressRegexp(current, total, max) => {
          progressListener.progressUpdated(current.toInt, total.toInt, max.toInt)
          seq
        }
        case _ => seq ++ parse(str)
      }
    }
  }

  def parse(str: String): Option[A]
}
