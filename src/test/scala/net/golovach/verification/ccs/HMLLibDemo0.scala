package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.HMLLib._

object HMLLibDemo0 extends App {

  val a = "a".↑

  val p = a :: ⟲

  // HML: X = <a>  tt  ∧ [Act - a]   ff  ∧ [a]  X
  val φ     = ◇(a){tt} ∧ ⬜(Act - a){ff} ∧ ⬜(a){↶}
//  val φ = ◇(a){tt} ∧ ⬜(Act - a){ff} ∧ ⬜(Act){↶}

  println(p ⊨ φ)
}
