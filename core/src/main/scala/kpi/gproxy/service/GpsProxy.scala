package kpi.gproxy.service

import akka.actor.{ Actor, ActorLogging, ActorRef }
import spray.json._
import fommil.sjs.FamilyFormats._

import kpi.gproxy.{ RpcMessage, Update, NewTarget, Teleport }

class GpsProxy extends Actor with ActorLogging {
  import context.system

  var target:Option[Update] = None
  var curr:Option[Update] = None
  var out:Option[ActorRef] = None

  def updateTarget() = {
    target.foreach { loc => {
      val m = RpcMessage("targetUpdate", Some(loc))
      out.foreach { l => {
        l ! Out(m.toJson.compactPrint)}}}
    }
  }

  def receive = {
    case RpcMessage("locationUpdate", Some(loc)) => {
      log.info(s"Got location update: $loc")
      curr = Some(loc)
      out = Some(sender())
      updateTarget()
    }

    case NewTarget(lat, lng) => {
      val loc = Update(lat, lng, 240, 0, 0, 0)
      log.info(s"Got new target: $lat $lng")
      target = Some(loc)
      updateTarget()
    }
    case Teleport(lat, lng) => {
      val loc = Update(lat, lng, 240, 0, 0, 0)
      out.foreach { o => {
        val m = RpcMessage("forceUpdate", Some(loc))
        o ! Out(m.toJson.compactPrint)}
      }
    }
    case msg => log.info(s"Got message: $msg")
  }
}
