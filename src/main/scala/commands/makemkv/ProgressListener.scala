package commands.makemkv

/**
 * Listen to makemkvcon progress events.
 * Created by alex on 05/05/15.
 */
trait ProgressListener {

  def onStart: Unit
  
  def onMessage(message: String): Unit
  
  def onProgressUpdated(current: Int, total: Int, max: Int): Unit = {
    onProgressUpdated(current.toDouble, total.toDouble, max.toDouble)
  }

  def onProgressUpdated(current: Double, total: Double, max: Double)

  def onFinish: Unit
}
