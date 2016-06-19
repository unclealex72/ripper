package file

import java.io.{FileWriter, PrintWriter}
import java.nio.file.attribute.PosixFilePermission
import java.nio.file.{CopyOption, Files, Paths, Path}

/**
 * Created by alex on 10/05/15.
 */
object Implicits {

  implicit class RichPath(path: Path) {
    def /(filename: String): Path = {
      filename match {
        case "." => path
        case ".." => path.getParent
        case _ => path.resolve(filename)
      }
    }

    def mkdirs: Unit = {
      Files.createDirectories(path)
    }

    def renameTo(target: Path): Unit = {
      Files.move(path, target)
    }

    def executable_=(executable: Boolean): Unit = {
      val permissions = Files.getPosixFilePermissions(path)
      if (executable) {
        permissions.add(PosixFilePermission.OWNER_EXECUTE)
      }
      else {
        permissions.remove(PosixFilePermission.OWNER_EXECUTE)
      }
      Files.setPosixFilePermissions(path, permissions)
    }

    def exists: Boolean = {
      Files.exists(path)
    }

    def isFile: Boolean = {
      Files.isRegularFile(path)
    }

    def delete(): Unit = {
      Files.deleteIfExists(path)
    }

    def children: Seq[Path] = {
      Option(path.toFile.listFiles).toSeq.flatten.map(_.toPath)
    }

    def +(suffix: String): Path = {
      new RichPath(path.getParent) / (name + suffix)
    }

    def < : Path = {
      path.getParent
    }

    def abs: Path = {
      path.toAbsolutePath
    }

    def name: String = {
      path.getFileName.toString
    }

    def writer: PrintWriter = {
      new PrintWriter(new FileWriter(path.toFile))
    }
  }

  implicit class StringToPath(str: String) {
    def /(filename: String): Path = {
      new RichPath(path) / filename
    }
    
    def path: Path = {
      Paths.get(str)
    }
  }
}