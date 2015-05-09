package makemkv

/**
 * Listen to makemkvcon progress events.
 * Created by alex on 05/05/15.
 */
trait ProgressListener {

  def onStart: Unit
  
  def onMessage(message: String): Unit
  
  def onProgressUpdated(current: Int, total: Int, max: Int)

  def onFinish: Unit
}
