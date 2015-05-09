package makemkv

import java.io.File

/**
 * Created by alex on 07/05/15.
 */
trait MakeMkvConMkv {

  def rip(titleId: Int, targetDirectory: File, targetFilename: String, name: String, season: Int, episode: Int, progressListener: ProgressListener): Unit
}
