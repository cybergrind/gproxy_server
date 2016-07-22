package kpi.grpoxy

import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Update(lat: Double, lng: Double, alt: Double, acc: Float = 0, speed: Float = 0, bearing: Float = 0)

object Update {
  implicit val updateW = new Writes[Update] {
    def writes(u: Update) = Json.obj(
      "lat" -> u.lat,
      "lng" -> u.lng,
      "alt" -> u.alt,
      "acc" -> u.acc,
      "speed" -> u.speed,
      "bearing" -> u.bearing
    )
  }

  implicit val updateR: Reads[Update] = (
    (JsPath \ "lat").read[Double] and
      (JsPath \ "lng").read[Double] and
      (JsPath \ "alt").read[Double] and
      (JsPath \ "acc").read[Float] and
      (JsPath \ "speed").read[Float] and
      (JsPath \ "bearing").read[Float]
  )(Update.apply _)
}

/***
 * server msgType: ack | targetUpdate
 * client msgType: locationUpdate
 */
case class RpcMessage(msgType: String, location: Option[Update])

object RpcMessage {
  implicit val rpcMessageW = new Writes[RpcMessage] {
    def writes(m: RpcMessage) = Json.obj(
      "msgType" -> m.msgType,
      "location" -> m.location
    )
  }

  implicit val rpcMessageR: Reads[RpcMessage] = (
    (JsPath \ "msgType").read[String] and
      (JsPath \ "location").readNullable[Update]
  )(RpcMessage.apply _)

}
