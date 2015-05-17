package gui

import java.util
import java.util.Collection

import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest
import org.controlsfx.control.textfield.{AutoCompletionBinding, TextFields}

import scalafx.scene.control.TextField
import javafx.util.Callback
import scala.collection.JavaConversions._

/**
 * Created by alex on 17/05/15.
 */
object AutoComplete {

  implicit class AutoCompleteTextField(val textField: TextField) {

    def autoComplete(minLength: Int, suggestionProvider: String => Seq[String]): TextField = {
      val callback = new Callback[AutoCompletionBinding.ISuggestionRequest, Collection[String]] {
        override def call(param: ISuggestionRequest): Collection[String] = {
          val text = param.getUserText
          if (text.length >= minLength && !param.isCancelled) {
            suggestionProvider(text)
          }
          else {
            Seq.empty[String]
          }
        }
      }
      TextFields.bindAutoCompletion(textField, callback)
      textField
    }
  }
}
