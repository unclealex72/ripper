package commands

sealed trait RipType

/**
 * TV episode information.
 * Created by alex on 10/05/15.
 */
case class TvEpisode(titleId: Int, targetFilename: String, name: String, season: Int, episode: Int) extends RipType {
  override def toString = s"$name, season $season, episode $episode"
}

case class Film(titleId: Int, targetFilename: String, name: String) extends RipType {
  override def toString() = name
}