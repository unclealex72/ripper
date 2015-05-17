package makemkv

import com.typesafe.scalalogging.StrictLogging

import scala.sys.process._

/**
 * Created by alex on 06/05/15.
 */
class MakeMkvConCommandImpl extends MakeMkvConCommand with StrictLogging {

  //val profile = "-sel:all,+sel:(eng|nolang),-sel:subtitle,-sel:(havemulti|havecore),-sel:mvcvideo,=100:all,-10:eng"

  override def execute(arguments: Seq[String]): Stream[String] = {
    val cmd = Seq("makemkvcon", "-r", "--progress=-same") ++ arguments
    logger info (Seq("Executing") ++ cmd).mkString(" ")
    cmd.lineStream
  }
}
