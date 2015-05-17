package gui

import makemkv.{DvdSource, MakeMkvConInfo, TitleInfo}

/**
 * Created by alex on 13/05/15.
 */
trait DvdLoader[TITLE <: Title] extends ModalProvider with TitlesModel[TITLE] {

  val makeMkvConInfo: MakeMkvConInfo

  def loadDvdInfo(dvdSource: DvdSource): Unit = {
    modal { progressListener => titleUpdater =>
      titleUpdater("Scanning DVD")
      titlesModel.clear
      titlesModel ++= makeMkvConInfo.info(dvdSource, progressListener).titles.map(generateTitle)
    }
  }

  def generateTitle(titleInfo: TitleInfo): TITLE
}
