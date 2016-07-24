package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging, Props }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import spray.can.Http


class GproxyMain extends Actor with ActorLogging {
  import context.system

  val host = "0.0.0.0"
  val port = 16888
  
  val GpsProxy = system.actorOf(Props(classOf[GpsProxy]), "GpsProxy")
  val listener = system.actorOf(Props(classOf[TCPListener], host, port, GpsProxy))

  val httpPort = 8182
  val http = system.actorOf(Props(classOf[HttpApi], GpsProxy), "HttpApi")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(http, interface=host, port=httpPort)

  def receive = {
    case msg => log.info(s"MAIN: Got message: $msg")
  }
}
