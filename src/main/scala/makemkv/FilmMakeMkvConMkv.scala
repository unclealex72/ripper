package makemkv

import java.nio.file.Path

import file.Implicits._

/**
 * Created by alex on 07/05/15.
 */
class FilmMakeMkvConMkv(override val makeMkvConCommand: MakeMkvConCommand) extends MakeMkvConParser[Unit](makeMkvConCommand) with MakeMkvConMkv[Film] with FilenameSanitiser {

  override def rip(dvdSource: DvdSource, film: Film, targetDirectory: Path, progressListener: ProgressListener): Unit = {
    val commandInfo = CommandInfo(targetDirectory / "Films" / film.name, dvdSource, film)
    commandInfo.outputDirectory.mkdirs
    produceOutput(progressListener, commandInfo.commandArguments)
    commandInfo.producedFile.renameTo(commandInfo.requiredFile)
  }

  override def script(dvdSource: ISO, film: Film, targetDirectory: Path): Seq[String] = {
    val commandInfo = CommandInfo(dvdSource.argument.getParent, dvdSource, film)
    val makeOutputDirectoryCommand = Seq("mkdir", "-p", commandInfo.outputDirectory)
    val makemkvCommand = Seq("makemkvcon") ++ commandInfo.commandArguments
    val moveCommand = Seq("mv", commandInfo.producedFile, commandInfo.requiredFile)
    val allQuoted: Seq[Any] => String = { parts =>
      parts.map(part => s""""$part"""").mkString(" ")
    }
    Seq(makeOutputDirectoryCommand, makemkvCommand, moveCommand).map(allQuoted)
  }

  override def parse(str: String): Option[Unit] = None

  case class CommandInfo(outputDirectory: Path, producedFile: Path, requiredFile: Path, commandArguments: Seq[String])
  object CommandInfo {
    def apply(outputDirectory: Path, dvdSource: DvdSource, film: Film): CommandInfo = {
      val producedFile = outputDirectory / film.targetFilename
      val requiredFile = outputDirectory / s"${sanitise(film.name)}.mkv"
      val commandArguments = Seq("mkv") ++ dvdSource.toCommandLineArguments ++ Seq(film.titleId, outputDirectory).map(_.toString)
      CommandInfo(outputDirectory, producedFile, requiredFile, commandArguments)
    }
  }
}

