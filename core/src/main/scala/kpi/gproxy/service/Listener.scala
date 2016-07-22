package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging }
import akka.util.ByteString
import java.net.InetSocketAddress
import scala.util.Try

import akka.actor.{ActorRef, Props}
import akka.io.{IO, Tcp}
import play.api.libs.json.Json


class LineReceiver (out: ActorRef) extends Actor with ActorLogging {
  import Tcp._

  def receive = {
    case Received(data) => {
      val resp = parseLine(data.utf8String)
      log.debug(s"Send string: $resp")
      sender() ! Write(ByteString(resp))
    }

    case PeerClosed => context stop self
    case ErrorClosed(cause) => context stop self
  }

  def parseLine(line:String):String = {
    Try(Json.parse(line))
      .map( s => {
        log.debug(s"Got msg: $s")
        out ! s
      })
    Json.toJson(Map("type" -> "ack")).toString()
  }
}

class TCPListener(host:String, port:Int, out:ActorRef) extends Actor with ActorLogging {
  import context.system
  import Tcp._

  log.info(s"Bind to $host:$port")
  IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))

  def receive = {
    case bound@Bound(localAddress) => out ! "ready"
    case connected@Connected(remote, local) =>
      val handler = context.actorOf(Props(classOf[LineReceiver], out))
      val conn = sender()
      conn ! Register(handler)
    case CommandFailed(msg: Bind) =>
      log.warning(s"Bind failed: $msg")
      context stop self
  }
}
