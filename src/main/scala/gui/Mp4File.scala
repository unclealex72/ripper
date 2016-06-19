package gui

import java.io.File

import scalafx.beans.property.ReadOnlyStringProperty

/**
  * A wrapper for modelling a file inside a table
  * Created by alex on 19/06/16.
  */
case class Mp4File(_file: File) {

  val file = new ReadOnlyStringProperty(this, "file", _file.getName)

}