package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.BisimulationLib._
import net.golovach.verification.ccs.CCSLib._

object BisimulationDemo1 extends App {

  val ltsA = toLTS("a".↓ :: (("b".↓ :: ∅("X")) + ("c".↓ :: ∅("X"))))
  val ltsB = toLTS(("a".↓ :: "b".↓ :: ∅("Y")) + ("a".↓ :: "c".↓ :: ∅("Y")))

  println(strongBisimilarity(ltsA, ltsA))
  println(strongBisimilarity(ltsA, ltsB))
  println(strongBisimilarity(ltsB, ltsB))
}
