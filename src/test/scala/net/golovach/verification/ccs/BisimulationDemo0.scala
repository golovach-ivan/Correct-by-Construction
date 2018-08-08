package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.BisimulationLib._
import net.golovach.verification.ccs.CCSLib._

object BisimulationDemo0 extends App {

  val ltsA = toLTS("a".↓ :: ∅("X"))
  val ltsB = toLTS(("a".↓ :: ∅("Y")) + ("a".↓ :: ∅("Y")))
  val ltsC = toLTS("a".↓ :: "a".↓ :: ∅("Z"))

//  println(dump(ltsA))
//  println(dump(ltsB))

  println(strongBisimilarity(ltsA, ltsA))
  println(strongBisimilarity(ltsA, ltsB))
  println(strongBisimilarity(ltsB, ltsB))

  println(strongBisimilarity(ltsA, ltsC))
  println(strongBisimilarity(ltsB, ltsC))
  println(strongBisimilarity(ltsC, ltsC))

//  val ltsA = LTS(
//    ports = Set("a"),
//    actions = Set("a".↓),
//    init = 's^0,
//    states = Set('s^0, 's^1),
//    edges = Set(Edge('s^0, "a".↓, 's^1))
//  )
}
