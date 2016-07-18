package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging, Props }


class GproxyMain extends Actor with ActorLogging {
  import context.system

  val host = "192.168.88.33"
  val port = 16888
  val listener = system.actorOf(Props(classOf[TCPListener], host, port, self))

  def receive = {
    case msg => log.info(s"Got message: $msg")
  }
}
