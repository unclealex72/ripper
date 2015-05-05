package makemkv

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.io.Source

import duration.Implicits._

/**
 * Created by alex on 05/05/15.
 */
class MakeMkvConInfoImplSpec extends Specification with Mockito {

  "Reading information from makemkvcon info" should {
    "correctly list the titles on the DVD" in {
      val makeMkvConCommand = mock[MakeMkvConCommand]
      makeMkvConCommand.execute(Seq("info", "disc:0")) returns {
        Source.fromInputStream(classOf[MakeMkvConInfoImplSpec].getClassLoader.getResourceAsStream("info.txt")).getLines().toStream
      }
      val progressListener = mock[ProgressListener]
      val makeMkvConInfoImpl = new MakeMkvConInfoImpl(makeMkvConCommand)
      val discInfo = makeMkvConInfoImpl.execute(progressListener)
      discInfo.name must be equalTo("Tales from the Darkside S1 D1")
      discInfo.titles must contain(exactly(
        TitleInfo(1, 43, "02:57:33".toPeriod, 7248056320l,8),
        TitleInfo(2, 6, "00:22:13".toPeriod, 952764416l, 1),
        TitleInfo(3, 5, "00:22:10".toPeriod, 917817344l, 1),
        TitleInfo(4, 6, "00:22:12".toPeriod, 885719040l, 1),
        TitleInfo(5, 6, "00:22:15".toPeriod, 886573056l, 1),
        TitleInfo(6, 5, "00:22:11".toPeriod, 884033536l, 1),
        TitleInfo(7, 5, "00:22:11".toPeriod, 884590592l, 1),
        TitleInfo(8, 5, "00:22:11".toPeriod, 918577152l, 1),
        TitleInfo(9, 5, "00:22:10".toPeriod, 917981184l, 1),
        TitleInfo(12, 3, "00:03:00".toPeriod, 97294336l, 1)))
    }
  }
}
