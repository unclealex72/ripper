package makemkv

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import file.Implicits._
/**
 * Created by alex on 10/05/15.
 */
class MakeMkvConMkvSpec extends Specification with Mockito {

  "Creating a script to convert an iso file" should {
    "create the correct commands" in {
      val makeMkvConCommand = mock[MakeMkvConCommand]
      val makeMkvConMkv = new TvMakeMkvConMkv(makeMkvConCommand)
      val script = makeMkvConMkv.script(
        ISO("/somedvd/TV/My Title/season_01/mytitle_s01e010203.iso".path),
        TvEpisode(1, "out.mkv", "My Title", 1, 2),
        "/somedvd/TV/My Title/season_01".path)
      script.mkString("\n") must be equalTo(
        """"mkdir" "-p" "/somedvd/TV/My Title/season_01/TV/My Title/season_01"
          |"makemkvcon" "mkv" "iso:/somedvd/TV/My Title/season_01/mytitle_s01e010203.iso" "1" "/somedvd/TV/My Title/season_01/TV/My Title/season_01"
          |"mv" "/somedvd/TV/My Title/season_01/TV/My Title/season_01/out.mkv" "/somedvd/TV/My Title/season_01/TV/My Title/season_01/my_title_s01e02.mkv"
          |"rm" "/somedvd/TV/My Title/season_01/mytitle_s01e010203.iso"""".stripMargin)
    }
  }
}
