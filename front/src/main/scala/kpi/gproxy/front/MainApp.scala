package kpi.gproxy.front

import scala.scalajs.js
import js.annotation.JSName


@js.native
object Bundle extends js.Object {
  def testFun(): js.Any = js.native
}


object MainApp extends js.JSApp {
  def main(): Unit = {
    println("HELL JS!")
    Bundle.testFun()
  }
}
