package makemkv

import java.nio.file.Path

import file.Implicits._

/**
 * Created by alex on 07/05/15.
 */
class TvMakeMkvConMkv(override val makeMkvConCommand: MakeMkvConCommand) extends MakeMkvConParser[Unit](makeMkvConCommand) with MakeMkvConMkv[TvEpisode] with FilenameSanitiser {

  override def rip(dvdSource: DvdSource, tvEpisode: TvEpisode, targetDirectory: Path, progressListener: ProgressListener): Unit = {
    val commandInfo = CommandInfo(targetDirectory / "TV" / tvEpisode.name / f"season_${tvEpisode.season}%02d", dvdSource, tvEpisode)
    commandInfo.outputDirectory.mkdirs
    produceOutput(progressListener, commandInfo.commandArguments)
    commandInfo.producedFile.renameTo(commandInfo.requiredFile)
  }

  override def script(dvdSource: ISO, tvEpisode: TvEpisode, targetDirectory: Path): Seq[String] = {
    val commandInfo = CommandInfo(dvdSource.argument.getParent, dvdSource, tvEpisode)
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
    def apply(outputDirectory: Path, dvdSource: DvdSource, tvEpisode: TvEpisode): CommandInfo = {
      val producedFile = outputDirectory / tvEpisode.targetFilename
      val requiredFile = outputDirectory / f"${sanitise(tvEpisode.name)}%s_s${tvEpisode.season}%02de${tvEpisode.episode}%02d.mkv"
      val commandArguments = Seq("mkv") ++ dvdSource.toCommandLineArguments ++ Seq(tvEpisode.titleId, outputDirectory).map(_.toString)
      CommandInfo(outputDirectory, producedFile, requiredFile, commandArguments)
    }
  }
}

