package makemkv

import scala.sys.process._

/**
 * Created by alex on 06/05/15.
 */
class MakeMkvConCommandImpl extends MakeMkvConCommand {

  //val profile = "-sel:all,+sel:(eng|nolang),-sel:subtitle,-sel:(havemulti|havecore),-sel:mvcvideo,=100:all,-10:eng"

  override def execute(arguments: Seq[String]): Stream[String] = {
    (Seq("makemkvcon", "-r", "--progress=-same") ++ arguments).lineStream
  }
}
