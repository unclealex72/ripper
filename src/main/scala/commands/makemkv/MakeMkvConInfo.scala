package commands.makemkv

import org.joda.time.Period

/**
 * Created by alex on 05/05/15.
 */
trait MakeMkvConInfo {

  def info(dvdSource: DvdSource, progressListener: ProgressListener): DiscInfo
}

case class DiscInfo(name: String, titles: Seq[TitleInfo])

case class TitleInfo(id: Int, trackNumber: Int, chapterCount: Int, duration: Period, size: Long, segmentCount: Int, target: String)

