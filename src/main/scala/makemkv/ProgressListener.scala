package makemkv

/**
 * Listen to makemkvcon progress events.
 * Created by alex on 05/05/15.
 */
trait ProgressListener {

  def progressUpdated(current: Int, total: Int, max: Int)

}
