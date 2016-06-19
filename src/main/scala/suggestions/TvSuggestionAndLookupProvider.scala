package suggestions

import java.net.URL

import scala.xml.XML

/**
  * Created by alex on 19/06/16.
  */
trait TvSuggestionAndLookupProvider extends SuggestionProvider with LookupProvider {

  def series(text: String) = {
    val response = XML.load(s"http://thetvdb.com/api/GetSeries.php?seriesname=$text")
    response \\ "Series"
  }

  override def suggestions(text: String): Seq[String] = {
    (series(text) \ "SeriesName").map(_.text)
  }

  override def lookupUrl(name: String): Option[URL] = {
    val seriesId = (series(name) \ "seriesid").map(_.text).headOption
    seriesId.map(id => new URL(s"http://thetvdb.com/?tab=series&id=$id"))
  }

}
