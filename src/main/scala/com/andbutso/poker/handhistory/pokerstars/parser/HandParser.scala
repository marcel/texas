package com.andbutso.poker.handhistory.pokerstars.parser

import java.io.File

import com.andbutso.poker.Street
import com.andbutso.poker.handhistory._
import com.andbutso.poker.handhistory.pokerstars.{Seat, CardShortHandTranslator}

import scala.io.{Source, BufferedSource}

object HandParser {
  def apply(lines: Seq[String]): HandHistory = {
    try {
      val parser = new HandParser

      lines foreach { line =>
        parser.parseLine(line)
      }

      parser.builder.build()
    } catch {
      case e: Exception =>
        System.err.println("\n\n*** ERROR *** Hand:", lines)
        System.err.println("\n\n")
        throw e
    }
  }

  def apply(file: File): IndexedSeq[HandHistory] = {
    if (file.isDirectory) {
      val files = file.listFiles() filterNot { _.isHidden }
      apply(files)
    } else {
      apply(Source.fromFile(file))
    }
  }

  def apply(files: Array[File]): IndexedSeq[HandHistory] = {
    files.toIndexedSeq flatMap { apply(_) }
  }

  def apply(bufferedSource: BufferedSource): IndexedSeq[HandHistory] = {
    bufferedSource.getLines().foldLeft(IndexedSeq[HandParser]()) { case (handParsers, line) =>
      if (handParsers.isEmpty || handParsers.last.isDone) {
        if (line.trim.isEmpty) {
          handParsers
        } else {
          val newParser = new HandParser
          newParser.parseLine(line)
          handParsers :+ newParser
        }
      } else {
        handParsers.last.parseLine(line)
        handParsers
      }
    } map { parser =>
      try {
        parser.builder.build()
      } catch {
        case e: Exception =>
          import org.kiama.output.PrettyPrinter._

          System.err.println("\n\n**** ERROR ***\n")
          System.err.println("Debug:")
          System.err.println(pretty(any(parser.builder), w = 1))
          System.err.println("\n")
          throw e
      }
    }
  }
}

class HandParser(@volatile var builder: HandHistoryBuilder = HandHistoryBuilder()) {
  @volatile var seatsAreAssigned = false
  @volatile var handIsOver       = false
  @volatile var done             = false
  @volatile var currentStreet    = Street.PreFlop

  def isDone = done

  def parseLine(line: String) {
    line.trim match {
      case "" =>
        done = handIsOver
      case "*** HOLE CARDS ***" =>
        seatsAreAssigned = true
      case "*** SHOW DOWN ***" =>
        seatsAreAssigned = true
      case "*** SUMMARY ***" =>
        update(builder.copy(lastStreet = currentStreet))
        handIsOver = true
      case TableLineParser(tableName, capacity, button) =>
        update(
          builder.copy(
            table = PokerStarsTable(tableName, capacity),
            seatAssignment = builder.seatAssignment.copy(
              onTheButton = Some(button)
            )
          )
        )
      case HandLineParser(handId, gameType, stakes, date) =>
        update(
          builder.copy(
            game = Game(
              id       = handId,
              gameType = gameType,
              stakes   = stakes
            ),
            date = date
          )
        )
      case StreetLineParser(street, board, nextCard) =>
        currentStreet = street

        update(
          street match {
            case Street.Flop =>
              builder.copy(board = builder.board.copy(flop = board), lastStreet = street)
            case Street.Turn =>
              builder.copy(board = builder.board.copy(turn = nextCard), lastStreet = street)
            case Street.River =>
              builder.copy(board = builder.board.copy(river = nextCard), lastStreet = street)
          }
        )
      case BlindPostingLineParser(userName, role, amount) =>
        builder.seatAssignment(userName) foreach { seat =>
          role match {
            case Seat.Role.SmallBlind =>
              update(
                builder.copy(
                  seatAssignment = builder.seatAssignment + seat.copy(role = Some(role)),
                  smallBlind = amount
                )
              )
            case Seat.Role.BigBlind =>
              update(
                builder.copy(
                  seatAssignment = builder.seatAssignment + seat.copy(role = Some(role)),
                  bigBlind = amount
                )
              )
          }
        }
      case SeatAssignmentLineParser(seat) =>
        update(
          builder.copy(
            seatAssignment = builder.seatAssignment + seat
          )
        )
      case PlayerCollectedLineParser(userName, chipAmount) =>
        builder.seatAssignment(userName) foreach { seat => // TODO This should fail rather than do nothing silently
          update(
            builder.copy(
              actions = builder.actions :+ seat.player.collects(chipAmount)(currentStreet)
            )
          )
        }
      case trimmedLine =>


        if (seatsAreAssigned) {
          PlayerActionLineParser(builder.seatAssignment)(trimmedLine) {
            case (seat, actionName, actionString) =>
              val playerAction = PlayerAction(seat.player, currentStreet)

              val action = actionName match {
                case Action.Name.folds  => Action.Fold
                case Action.Name.mucks  => Action.Muck
                case Action.Name.checks => Action.Check
                case Action.Name.raises =>
                  val pattern = """(?<from>\S+)\s+to\s+(?<to>\S+)""".r

                  pattern.findFirstMatchIn(actionString) collect {
                    case matchData =>
                      Action.Raise(
                        ChipAmount.fromString(matchData.group(1)),
                        ChipAmount.fromString(matchData.group(2))
                      )
                  } get // TODO Deal with this case so there is no Option
                case Action.Name.calls =>
                  val pattern = """\S+""".r
                  val callAmount = pattern.findFirstIn(actionString).get // TODO Deal with this case
                  Action.Call(ChipAmount.fromString(callAmount))
                case Action.Name.bets =>
                  val pattern = """\S+""".r
                  val callAmount = pattern.findFirstIn(actionString).get // TODO Deal with this case
                  Action.Bet(ChipAmount.fromString(callAmount))
                case Action.Name.shows =>
                  val pattern = """\[([^\]]+)\]""".r
                  val cardString = pattern.findFirstIn(actionString).get // TODO Deal with this case

                  Action.Show(
                    CardShortHandTranslator.translateAll(cardString)
                  )
                case Action.Name.sits =>
                  Action.SitOut
//                case Action.Name.posts =>

              }

              update(
                builder.copy(
                  actions = builder.actions :+ playerAction(action)
                )
              )
          }
        }
    }

    def update(updatedBuilder: HandHistoryBuilder): Unit = {
      this.builder = updatedBuilder
    }
  }
}