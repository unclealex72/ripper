package commands.mp4box

import java.nio.file.Path

import com.typesafe.scalalogging.StrictLogging
import commands.makemkv.ProgressListener
import commands.{RipType, TargetFileProvider}

import scala.sys.process._
import file.Implicits._

import scala.util.matching.Regex

/**
  * Created by alex on 19/06/16.
  */
abstract class AbstractMp4Box[RIPTYPE <: RipType] extends Mp4Box[RIPTYPE] with TargetFileProvider[RIPTYPE] with StrictLogging {

  val extension = "mp4"

  override def fileExists(rip: RIPTYPE, targetDirectory: Path): Option[Path] = {
    Some(toFile(targetDirectory)(rip)).filter(_.exists)
  }

  def concatenate(ripTarget: RIPTYPE, sourceFiles: Seq[Path], targetDirectory: Path, progressListener: ProgressListener): Unit = {
    val target: Path = toFile(targetDirectory)(ripTarget)
    target.getParent.mkdirs
    target.delete()
    val cmd =
      Seq("MP4Box", "-tmp", targetDirectory.toString) ++
        sourceFiles.flatMap(sourceFile => Seq("-cat", sourceFile.toString)) :+
        target.toString
    logger.info(s"Executing ${cmd.mkString(" ")}")
    progressListener.onStart
    val procLogger = ProcLogger(progressListener, sourceFiles.length)
    val processLogger = ProcessLogger.apply(procLogger, procLogger)
    cmd.!(processLogger)
    progressListener.onFinish
  }

  class ProcLogger(progressListener: ProgressListener, fileCount: Int, progressRegex: Regex) extends (String => Unit) {

    val steps = fileCount + 1
    val increment = 100d / steps
    var base = -increment
    val total = 100d

    def apply(line: String): Unit = {
      logger.info(line)
      if (line.startsWith("Appending file") || line.startsWith("Saving")) {
        base += increment
      }
      else {
        progressRegex.findFirstMatchIn(line) match {
          case Some(progress) =>
            val percentage = progress.group(1).toInt
            progressListener.onProgressUpdated(percentage.toDouble, base + percentage / steps, 100)
          case _ => progressListener.onMessage(line)
        }
      }
    }
  }

  object ProcLogger {

    private val progressRegex = """\(([0-9]+)/100\)""".r

    def apply(progressListener: ProgressListener, fileCount: Int): ProcLogger = new ProcLogger(progressListener, fileCount, progressRegex)
  }
}
