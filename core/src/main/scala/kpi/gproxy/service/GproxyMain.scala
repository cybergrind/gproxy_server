package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging, Props }


class GproxyMain extends Actor with ActorLogging {
  import context.system

  val host = "192.168.88.33"
  val port = 16888
  val GpsProxy = system.actorOf(Props(classOf[GpsProxy]), "GpsProxy")
  val listener = system.actorOf(Props(classOf[TCPListener], host, port, GpsProxy))

  def receive = {
    case msg => log.info(s"MAIN: Got message: $msg")
  }
}
