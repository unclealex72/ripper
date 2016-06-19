package commands.makemkv

import com.typesafe.scalalogging.StrictLogging

/**
 * Created by alex on 05/05/15.
 */
abstract class MakeMkvConParser[A](val makeMkvConCommand: MakeMkvConCommand) extends StrictLogging {

  val progressRegexp = """PRGV:([0-9]+),([0-9]+),([0-9]+)""".r
  val messageRegexp = """MSG:[0-9]+,[0-9]+,[0-9]+,"(.+?)",.+""".r

  def produceOutput(progressListener: ProgressListener, arguments: Seq[String]): Seq[A] = {
    progressListener.onStart
    val result = makeMkvConCommand.execute(arguments).foldLeft(Seq.empty[A]){ (seq, str) =>
      str match {
        case progressRegexp(current, total, max) => {
          progressListener.onProgressUpdated(current.toInt, total.toInt, max.toInt)
          seq
        }
        case messageRegexp(msg) => {
          logger info msg
          progressListener.onMessage(msg)
          seq
        }
        case _ => seq ++ parse(str)
      }
    }
    progressListener.onFinish
    result
  }

  def parse(str: String): Option[A]
}
