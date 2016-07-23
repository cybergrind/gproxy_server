package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging }
import akka.util.ByteString
import java.net.InetSocketAddress
import scala.util.Try

import akka.actor.{ActorRef, Props}
import akka.io.{IO, Tcp}

import spray.json._
import fommil.sjs.FamilyFormats._

import kpi.grpoxy.{ RpcMessage, Update }


case class Out(msg: String)

class LineReceiver (out: ActorRef, sock: ActorRef) extends Actor with ActorLogging {
  import Tcp._

  def receive = {
    case Received(data) => {
      val resp = parseLine(data.utf8String)
      log.debug(s"Send string: $resp")
      sock ! Write(ByteString(s"$resp\n"))
    }
    case Out(msg) => {
      log.debug(s"Out string: $msg")
      sock ! Write(ByteString(s"$msg\n"))
    }
    case PeerClosed => context stop self
    case ErrorClosed(cause) => context stop self
  }

  def parseLine(line:String):String = {
    line.split("\n").foreach( part => {
      val j = part.parseJson
      val msg = j.convertTo[RpcMessage]
      out ! msg
    })
    RpcMessage("ack", None).toJson.compactPrint
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
      val conn = sender()
      val handler = context.actorOf(Props(classOf[LineReceiver], out, conn))
      conn ! Register(handler)
    case CommandFailed(msg: Bind) =>
      log.warning(s"Bind failed: $msg")
      context stop self
  }
}
