package kpi.gproxy.service

import akka.actor.{Actor, ActorRef, ActorLogging}
import spray.routing._
import spray.http._

import kpi.gproxy.{ NewTarget, Teleport }


trait HttpBase extends HttpService {
  val myRoute = {
    (path("") & get) {
      parameter('q) { query => {
        handleQuery(query)
        complete(s"Ok with: $query")
      }}
    } ~
    (path("force") & get) {
      parameter('f) { query =>
        handleTeleport(query)
        complete(s"Teleport: $query")
        }
    } ~
    (pathPrefix("bundle") & get) {
      getFromResourceDirectory("")
    } ~
    (pathPrefix("dev") & get) {
      getFromDirectory("target/scala-2.11/classes/")
    }
  }

  def handleQuery(q: String):Unit
  def handleTeleport(q: String):Unit

}

class HttpApi (map: ActorRef) extends Actor with ActorLogging with HttpBase {

  def handleTeleport(q:String) = {
    val s = q.split('/')
    val lat:Double = s(0).toDouble
    val lng:Double = s(1).toDouble
    map ! Teleport(lat, lng)
  }

  def handleQuery(q:String) = {
    val s = q.split('/')
    val lat:Double = s(0).toDouble
    val lng:Double = s(1).toDouble
    map ! NewTarget(lat, lng)
  }

  def actorRefFactory = context
  def receive = runRoute(myRoute)
}
