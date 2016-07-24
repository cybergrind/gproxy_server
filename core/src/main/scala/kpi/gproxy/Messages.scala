package kpi.gproxy

sealed abstract class Msg
case class NewTarget(lat: Double, lng: Double)
case class Teleport(lat: Double, lng: Double)
