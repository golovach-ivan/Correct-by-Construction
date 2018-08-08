package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._

import scala.collection.immutable
import scala.collection.immutable.Stream.iterate

/**
  * - Kanellakis and Smolka (Kanellakis and Smolka, 1983)
  * (see also the journal version (Kanellakis and Smolka, 1990))
  * - Paige and Tarjan (Paige and Tarjan, 1987)
  */
object BisimulationLib {

  type BinRel = Map[(LTSState, LTSState), Boolean]

  object BinRel {
    def apply(): BinRel = Map[(LTSState, LTSState), Boolean]()
  }

  def strongBisimilarity(xLTS: LTS, yLTS: LTS): Boolean = {
    if (xLTS.actions != yLTS.actions)
      return false

    def generateAll(xss: Set[LTSState], yss: Set[LTSState]): Seq[BinRel] =
      for (k <- 0 until BigInt(2).pow(xss.size * yss.size).toInt)
        yield (for {
          (xs, xk) <- xss.zipWithIndex
          (ys, yk) <- yss.zipWithIndex
        } yield (xs, ys) -> (((k >> (xk * yss.size + yk)) & 1) > 0)).toMap

    def isBisimulation(r: BinRel): Boolean = {
      if (!r(xLTS.init, yLTS.init)) return false

      // ∀ (xSrc, ySrc): (xSrc ~ ySrc) ⇒
      for (((xSrc, ySrc), true) <- r) {
        // ∀ xAct ∈ xLTS.edges(xSrc, _, _).act
        for (Edge(_, xAct, xDst) <- xLTS.edgesFrom(xSrc)) {
          // ∃ yAct ∈ yLTS.edges(ySrc, ?, _)
          if (!(yLTS.edgesFromWithAct(ySrc, xAct) exists { case Edge(_, _, yDst) => r(xDst, yDst) }))
          // xDst ~ yDst
            return false
        }
        for (Edge(_, yAct, yDst) <- yLTS.edgesFrom(ySrc)) {
          if (!(xLTS.edgesFromWithAct(xSrc, yAct) exists { case Edge(_, _, xDst) => r(xDst, yDst) }))
            return false
        }
      }

      true
    }

    generateAll(xLTS.states, yLTS.states) exists isBisimulation
  }

  def weakBisimilarity(xLTS: LTS, yLTS: LTS): Boolean = {
    ???
  }

  implicit class opsBisimilarity(self: LTS) {
    /**
      * Strong Bisimilarity
      */
    def ~(that: LTS): Boolean = strongBisimilarity(self, that)

    /**
      * Weak Bisimilarity
      */
    def ≈(that: LTS): Boolean = weakBisimilarity(self, that)
  }

  def strong2Bisimilarity(xLTS: LTS, yLTS: LTS): Boolean = {

    val xGroups: Map[Set[LTSAction], Set[LTSState]] =
      xLTS.states.groupBy(xLTS.edgesFrom(_).map(_.act))
    val yGroups: Map[Set[LTSAction], Set[LTSState]] =
      yLTS.states.groupBy(yLTS.edgesFrom(_).map(_.act))

    if (xGroups.keys != yGroups.keys) return false

    //    val sss: Seq[Seq[BinRel]] = for {
    //      key <- xGroups.keys.toSeq
    //    } yield generateAll(xGroups(key), yGroups(key))

    def prod[A](xss: Seq[Seq[A]]): Seq[Seq[A]] = xss match {
      case head :: Nil => head.map(Seq(_))
      case head :: tail =>
        for {
          h <- head
          t <- prod(tail)
        } yield h +: t
    }

    def generateAll(xss: Set[LTSState], yss: Set[LTSState]): Seq[BinRel] = {
      val xxx: Seq[Map[(LTSState, LTSState), Boolean]] =
        for (k <- (0 until BigInt(2).pow(xss.size * yss.size).toInt).toSeq)
          yield (for {
            (xs, xk) <- xss.zipWithIndex
            (ys, yk) <- yss.zipWithIndex
          } yield (xs, ys) -> (((k >> (xk * yss.size + yk)) & 1) > 0)).toMap

      xxx
    }

    //    val v: Seq[Seq[BinRel]] = prod(sss)

    //    def foldd(x: Seq[BinRel]): BinRel = {
    //      (x /: BinRel())(_ ++ _)
    //      ???
    //    }
    //
    //    (v /: ???)
    //
    ???
  }

  def strong3Bisimilarity(xLTS: LTS, yLTS: LTS): Boolean = {

    def oneWay(aSrc: LTSState, aLTS: LTS, bSrc: LTSState, bLTS: LTS, rel: BinRel): Boolean =
      aLTS.edgesFrom(aSrc).forall({
        case Edge(_, aAct, aDst) =>
          bLTS.edgesFromWithAct(bSrc, aAct) exists {
            case Edge(_, _, bDst) => rel(aDst, bDst)
          }
      })

    def recalc(prev: BinRel): BinRel = {
      val saveFalse = prev.collect({ case kv@((_, _), false) => kv })
      val recalcTrue =
        for (((xSrc, ySrc), true) <- prev)
          yield (xSrc, ySrc) ->
            (oneWay(xSrc, xLTS, ySrc, yLTS, prev)
              &&
              oneWay(ySrc, yLTS, xSrc, xLTS, prev))

      saveFalse ++ recalcTrue
    }

    var rel0 = (for {
      xState <- xLTS.states
      yState <- yLTS.states
    } yield (xState, yState) -> true).toMap

    val r = iterate(rel0)(recalc)

    ???
  }
}
