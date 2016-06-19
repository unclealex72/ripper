package gui

import java.net.{URL, URLEncoder}
import java.time.Year

import suggestions.{FilmSuggestionAndLookupProvider, LookupProvider, SuggestionProvider}

import scala.io.Source
import argonaut._
import Argonaut._
import commands.Film

import scalaz.{Failure, Success}

/**
  * Created by alex on 14/05/15.
  */
class FilmRipGui extends AbstractDvdGui("Film DVD Ripper") with FilmDvdGui with RipGui[FilmTitle, Film] with FilmSuggestionAndLookupProvider
