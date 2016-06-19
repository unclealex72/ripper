package gui

import commands.makemkv.{MakeMkvConCommandImpl, MakeMkvConCommand}

/**
 * Created by alex on 14/05/15.
 */
trait MakeMkvConCommandProvider {

  val makeMkvConCommand: MakeMkvConCommand = new MakeMkvConCommandImpl
}
