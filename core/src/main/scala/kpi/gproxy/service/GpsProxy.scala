package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging }
import play.api.libs.json.Json

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
      sender() ! Out(Json.toJson(m).toString())
    }
    case msg => log.info(s"Got message: $msg")
  }
}
