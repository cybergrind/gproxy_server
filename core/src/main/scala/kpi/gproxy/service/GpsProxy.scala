package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging }
import spray.json._
import fommil.sjs.FamilyFormats._

import kpi.grpoxy.{ RpcMessage, Update }

class GpsProxy extends Actor with ActorLogging {
  import context.system

  def receive = {
    case RpcMessage("locationUpdate", location) => {
      val loc = location.get
      log.info(s"Got location update: $loc")
      val target = loc.copy(
        lat = loc.lat + 0.001,
        lng = loc.lng + 0.001)

      val m = RpcMessage("targetUpdate", Some(target))
      log.debug(s"Send resp: ${m.toJson.compactPrint}")
      sender() ! Out(m.toJson.compactPrint)
    }
    case msg => log.info(s"Got message: $msg")
  }
}
