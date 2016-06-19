package gui

import scalafx.application.AppRunner

object GuiRunner extends App {

  val modes: Map[String, AppRunner] = Map(
    "tv" -> new TvRipGuiRunner,
    "tvconcat" -> new TvConcatGuiRunner,
    "tvconv" -> new TvConvertGuiRunner,
    "film" -> new FilmRipGuiRunner,
    "filmconv" -> new FilmConvertGuiRunner,
    "filmconcat" -> new FilmConcatGuiRunner
    )

  val optMode = for {
    arg <- args.headOption.map(_.toLowerCase)
    mode <- modes.get(arg)
  } yield mode
  optMode match {
    case Some(appRunner) => {
      appRunner.run
    }
    case _ => {
      println(s"Please provide a valid run mode: ${modes.keys.mkString(", ")}")
    }
  }
}

class TvRipGuiRunner extends AppRunner(() => new TvRipGui)
class TvConvertGuiRunner extends AppRunner(() => new TvConvertGui)
class TvConcatGuiRunner extends AppRunner(() => new TvConcatGui)
class FilmRipGuiRunner extends AppRunner(() => new FilmRipGui)
class FilmConvertGuiRunner extends AppRunner(() => new FilmConvertGui)
class FilmConcatGuiRunner extends AppRunner(() => new FilmConcatGui)
