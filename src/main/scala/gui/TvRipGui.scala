package gui

import java.net.URL

import commands.TvEpisode
import suggestions.TvSuggestionAndLookupProvider

import scala.xml.XML

/**
 * Created by alex on 14/05/15.
 */
class TvRipGui extends AbstractDvdGui("TV DVD Ripper") with TvDvdGui with RipGui[TvTitle, TvEpisode] with TvSuggestionAndLookupProvider