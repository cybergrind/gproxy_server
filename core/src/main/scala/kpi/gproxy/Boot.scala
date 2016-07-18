package kpi.gproxy

import akka.actor.{ ActorSystem, Props }
import kpi.gproxy.service.GproxyMain


object Boot extends App {
  println("Boot")

  implicit val system = ActorSystem("gproxy")
  val gproxy = system.actorOf(Props(classOf[GproxyMain]))
}
