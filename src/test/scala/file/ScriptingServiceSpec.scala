package file

import java.io.File
import java.nio.file.{Paths, Path}
import file.Implicits._
import makemkv.{TvEpisode, MakeMkvConMkv}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

/**
 * Created by alex on 10/05/15.
 */
class ScriptingServiceSpec extends Specification with Mockito {

  "Searching for unscripted iso files" should {
    val makeMkvConMkv = mock[MakeMkvConMkv[TvEpisode]]
    val scriptingService = new ScriptingServiceImpl[TvEpisode](makeMkvConMkv)
    val resourceUrl = classOf[ScriptingServiceSpec].getResource("root.txt")
    val rootDir = Paths.get(resourceUrl.toURI).getParent
    val unscriptedIsos = scriptingService.listUnscriptedISOs(rootDir).map { path =>
      rootDir.relativize(path)
    }
    "contain all unscripted isos" in {
      unscriptedIsos must contain("a.iso".path, "b" / "bbb.iso", "c" / "cc.iso", "c" / "ccc.iso")
    }
    "not contain any scripted isos" in {
      unscriptedIsos must not(contain("b" / "bb.iso"))
    }
    "not contain any non-iso files" in {
      unscriptedIsos must not(contain("root.txt".path, "b".path, "c".path))

    }
  }
}
