package kpi.gproxy.front

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._


object ReactTest {
  def apply() = {
    val HelloList = ReactComponentB[Unit]("HelloList")
      .render($ => {
        <.ol(
          <.li("item1"),
          <.li("item2"))
      })
    HelloList.build()
  }
}
