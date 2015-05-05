package duration

import org.joda.time.{Period, Duration}
import org.joda.time.format.PeriodFormatterBuilder

/**
 * Created by alex on 05/05/15.
 */
object Implicits {

  private val periodFormatter = new PeriodFormatterBuilder()
    .printZeroAlways()
    .minimumPrintedDigits(2)
    .appendHours()
    .appendSeparator(":")
    .appendMinutes()
    .appendSeparator(":")
    .appendSeconds()
    .toFormatter()

  implicit class StringToPeriod(str: String) {

    def toPeriod: Period = periodFormatter.parsePeriod(str).toPeriod
  }

  implicit class PeriodToString(period: Period) {

    def format: String = periodFormatter.print(period)
  }
}
