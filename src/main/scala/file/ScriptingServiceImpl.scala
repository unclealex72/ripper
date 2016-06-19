package file

import java.nio.file.Path

import commands.RipType
import file.Implicits._
import commands.makemkv._

import scala.collection.SortedSet

/**
 * Created by alex on 10/05/15.
 */
class ScriptingServiceImpl[RIPTYPE <: RipType](val makeMkvConMkv: MakeMkvConMkv[RIPTYPE]) extends ScriptingService[RIPTYPE] {

  override def listUnscriptedISOs(path: Path): SortedSet[Path] = {
    if (!path.exists) {
      SortedSet.empty[Path]
    }
    else if (path.isFile) {
      SortedSet.empty[Path] ++ Some(path).filter(isUnscriptedIso)
    }
    else {
      path.children.foldLeft(SortedSet.empty[Path]){ case (files, file) =>
        files ++ listUnscriptedISOs(file)
      }
    }
  }

  def isUnscriptedIso(path: Path): Boolean = {
    path.name.endsWith(".iso") && !toScriptFile(path).exists
  }

  def toScriptFile(path: Path) = {
    path + ".sh"
  }

  override def script(dvdSource: ISO, ripTypes: Seq[RIPTYPE], targetDirectory: Path): Unit = {
    val isoPath = dvdSource.argument.abs
    val topLines = Seq("#!/bin/bash", "", s"""if [ -e "$isoPath" ]; then """)
    val scriptLines = ripTypes.foldLeft(topLines) { case (lines, ripType) =>
      lines :+ "  " + makeMkvConMkv.script(dvdSource, ripType, targetDirectory).mkString(" && \\\n")
    } ++ Seq(s"""  rm "${dvdSource.argument.abs}"""", "fi")
    val scriptFile = toScriptFile(isoPath)
    val writer = scriptFile.writer
    try {
      scriptLines.foreach { line => writer.println(line) }
    }
    finally {
      writer.close
    }
    scriptFile.executable_=(true)
  }

}
