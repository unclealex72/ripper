package makemkv

/**
 * Created by alex on 07/05/15.
 */
trait FilenameSanitiser {

  def sanitise(filename: String): String = {
    filename.toLowerCase.filter(ch => Character.isLetterOrDigit(ch) || Character.isWhitespace(ch)).replaceAll("""\s+""", "_")
  }
}
