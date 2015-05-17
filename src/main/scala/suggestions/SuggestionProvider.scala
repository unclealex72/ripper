package suggestions

/**
 * Created by alex on 17/05/15.
 */
trait SuggestionProvider {

  def suggestions(text: String): Seq[String]
}
