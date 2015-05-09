package di

import makemkv._
import scaldi.Module

/**
 * Created by alex on 06/05/15.
 */
class CommonModule extends Module {

  bind [MakeMkvConCommand] to injected [MakeMkvConCommandImpl]
  bind [MakeMkvConInfo] to injected [MakeMkvConInfoImpl]
  bind [MakeMkvConMkv] to injected [MakeMkvConMkvImpl]

}
