package suggestions

import java.net.URL

/**
 * Created by alex on 17/05/15.
 */
trait LookupProvider {

  def lookupUrl(name: String): Option[URL]
}
