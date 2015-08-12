package com.andbutso.poker.handhistory.pokerstars

case class SeatAssignment(
  seatsByNumber: Map[Int, Seat] = Map.empty[Int, Seat],
  onTheButton: Option[Int] = None
) {
  def seats = {
    seatsByNumber.values map { seat =>
      onTheButton match {
        case Some(seatNumber) if seat.number == seatNumber && seat.role.isEmpty =>
          seat.copy(role = Some(Seat.Role.Button))
        case _ =>
          seat
      }
    }
  }

  def byUserName = seats map { seat => seat.player.userName -> seat } toMap
  def byRole     = seats flatMap { seat => seat.role map { _ -> seat } } toMap

  def +(seat: Seat) = {
    val seatToAdd = onTheButton match {
      case Some(seatNumber) if seat.number == seatNumber =>
        seat.copy(role = Some(Seat.Role.Button))
      case _ =>
        seat
    }

    copy(seatsByNumber = seatsByNumber + (seatToAdd.number -> seatToAdd))
  }

  def apply(role: Seat.Role.Value): Option[Seat] = {
    byRole.get(role)
  }

  def apply(number: Int): Option[Seat] = {
    seatsByNumber.get(number)
  }

  def apply(userName: String): Option[Seat] = {
    byUserName.get(userName)
  }
}
