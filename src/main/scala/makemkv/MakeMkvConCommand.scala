package makemkv

/**
 * A trait that, given a list of command line arguments will produce a stream of strings. This is used to
 * abstract away running makemkvcon as a native process.
 * Created by alex on 05/05/15.
 */
trait MakeMkvConCommand {

  def execute(arguments: Seq[String]): Stream[String]
}
