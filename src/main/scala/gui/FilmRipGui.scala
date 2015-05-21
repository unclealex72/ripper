package gui

import java.net.{URLEncoder, URL}
import java.time.Year

import makemkv.Film
import suggestions.SuggestionProvider

import scala.io.Source

import argonaut._, Argonaut._

import scalaz.{Failure, Success}

/**
  * Created by alex on 14/05/15.
  */
class FilmRipGui extends AbstractGui("Film DVD Ripper") with FilmGui with RipGui[FilmTitle, Film] with SuggestionProvider with LookupProvider {

  val tmdbApiKey = "3061ab25346f16fc3bb5f1e7da3a4b68"

  import DecodeImplicits._

  def search(text: String, year: Option[Int] = None): Seq[SearchResult] = {
    val parameters = Seq(
      "api_key" -> tmdbApiKey,
      "search_type" -> "ngram",
      "query" -> text,
      "include_adult" -> true) ++
      year.map(year => "year" -> year)
    val url = "http://api.themoviedb.org/3/search/movie?" +
      parameters.map(p => s"${p._1}=${URLEncoder.encode(p._2.toString)}").mkString("&")
    val response = Source.fromURL(new URL(url)).mkString
    response.decodeValidation[Response].toValidationNel match {
      case Success(response) => response.searchResults
      case Failure(errors) => {
        errors.foreach(println)
        Seq.empty
      }
    }
  }

  override def suggestions(text: String): Seq[String] = {
    search(text).map { result =>
      (Seq(result.title) ++ result.year.map(year => s"($year)")).mkString(" ")
    }
  }

  override def lookupUrl(name: String): Option[URL] = {
    val nameAndYear = """(.+?)(?: \((\d{4})\))?""".r
    val (text, year) = name match {
      case nameAndYear(title, year) => (title, Option(year).map(_.toInt))
    }
    val searchResults = search(text, year)
    searchResults.headOption.map(searchResult => new URL(s"https://www.themoviedb.org/movie/${searchResult.id}"))
  }
}

case class Response(page: Int, searchResults: Seq[SearchResult])
case class SearchResult(title: String, year: Option[Year], id: Int)

object DecodeImplicits {

  implicit def ResponseDecodeJson: DecodeJson[Response] = jdecode2L(Response.apply)("page", "results")
  implicit def SearchResultDecodeJson: DecodeJson[SearchResult] = jdecode3L(SearchResult.apply)("title", "release_date", "id")
  implicit def YearDecodeJson: DecodeJson[Option[Year]] = DecodeJson { c =>
    val dateDecoder = for {
      date <- c.as[String]
    } yield date
    dateDecoder.map { date =>
      val releaseDate = """(\d{4})-\d{2}-\d{2}""".r
      date match {
        case releaseDate(year) => Some(Year.of(year.toInt))
        case _ => None
      }
    }
  }
}