package kpi.gproxy


case class Update(lat: Double, lng: Double, alt: Double,
  acc: Float = 0, speed: Float = 0, bearing: Float = 0)

object Update {
}

/***
 * server msgType: ack | targetUpdate
 * client msgType: locationUpdate
 */
case class RpcMessage(msgType: String, location: Option[Update])

object RpcMessage {
}
