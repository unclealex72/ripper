package makemkv

import org.joda.time.Period

/**
 * Created by alex on 05/05/15.
 */
trait MakeMkvConInfo {

  def execute(progressListener: ProgressListener): DiscInfo
}

case class DiscInfo(name: String, titles: Seq[TitleInfo])

case class TitleInfo(id: Int, chapterCount: Int, duration: Period, size: Long, segmentCount: Int)

