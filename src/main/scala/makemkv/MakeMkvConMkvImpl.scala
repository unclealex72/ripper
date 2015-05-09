package makemkv

import java.io.File

/**
 * Created by alex on 07/05/15.
 */
class MakeMkvConMkvImpl(override val makeMkvConCommand: MakeMkvConCommand) extends MakeMkvConParser[Unit](makeMkvConCommand) with MakeMkvConMkv with FilenameSanitiser {

  implicit class FileImplicits(file: File) {
    def /(filename: String) = new File(file, filename)
  }

  override def rip(
                    titleId: Int,
                    targetDirectory: File,
                    targetFilename: String,
                    name: String,
                    season: Int,
                    episode: Int,
                    progressListener: ProgressListener): Unit = {
    val outputDirectory = targetDirectory / "TV" / name / f"season_$season%02d"
    outputDirectory.mkdirs
    produceOutput(progressListener, "mkv", "disc:0", titleId.toString, outputDirectory.toString)
    val producedFile = outputDirectory / targetFilename
    val requiredFile = outputDirectory / f"${sanitise(name)}%s_s$season%02de$episode%02d.mkv"
    producedFile.renameTo(requiredFile)
  }

  override def parse(str: String): Option[Unit] = None
}
