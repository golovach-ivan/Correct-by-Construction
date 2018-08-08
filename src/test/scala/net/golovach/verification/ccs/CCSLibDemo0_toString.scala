package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

object CCSLibDemo0_toString extends App {

  val a = "a"

  val p = (a.↑ :: ∅) | (a.↓ :: ∅) | (a.↑ :: ∅)
  val q = (a.↑ :: ∅) + (a.↑ :: ∅)

  println(p)
  println(q)

  println(p + p)
  println(q | q)
}
