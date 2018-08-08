package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.ReductionLib._

object ReductionLibDemo0 extends App {

  val a = "a"

//  val p = ('a.↑ :: ∅) | ('a.↓ :: ∅) | ('a.↑ :: ∅) | ('a.↓ :: ∅)
  val p = (a.↑ :: ∅) | (a.↓ :: ∅) | (a.↑ :: ∅) | (a.↓ :: ∅) | (a.↑ :: ∅)

//  (p →).foreach(s => println(s"  → $s"))
//  println()
  (p ⇥).foreach(s => println(s"  → $s"))
}
