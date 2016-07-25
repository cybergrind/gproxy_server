package kpi.gproxy.front

import scala.scalajs.js
import org.scalajs.dom
import dom.document

import js.annotation.JSName

import japgolly.scalajs.react.ReactDOM


@js.native
object Bundle extends js.Object {
  def testFun(): js.Any = js.native
  def log(l: Any): js.Any = js.native

  @JSName("React")
  val React: js.Any = js.native

  @JSName("ReactDOM")
  val ReactDOM: js.Any = js.native
}

object MainApp extends js.JSApp {
  def main(): Unit = {
    println("HELL JS!")
    Bundle.log(Bundle.React)
    Bundle.log(ReactTest())
    Bundle.testFun()

    Bundle.log(document)
    val rootNode = document.getElementById("reactRoot")
    Bundle.log(rootNode)
    ReactDOM.render(ReactTest(), rootNode)
  }
}
