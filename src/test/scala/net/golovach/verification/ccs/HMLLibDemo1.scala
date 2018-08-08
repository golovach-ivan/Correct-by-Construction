package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.HMLLib._

object HMLLibDemo1 extends App {
  val $ = "coin"
  val ☕ = "coffee"

  val CM = $.↑ :: ☕.↓ :: ∅("CM")

  val φ = tt ∧ ff
  val ψ = ◇($.↑){tt} ∧ ⬜($.↑){↶}
  val ω = ◇($.↑, ☕.↓){tt} ∧ ⬜($.↑, ☕.↓){↶}
  val ζ = ◇(Act){tt} ∧ ⬜(Act - ($.↑, ☕.↓)){↶}

  val witnessA: Boolean = CM ⊨ φ
  val witnessB = CM ⊨ ψ

  // ===

  val α = ¬(¬(tt) ∧ ff)
  val β = ◇($.↑){tt} ∧ ⬜($.↑){↶}
  val γ = ◇($.↑, ☕.↓){tt} ∧ ⬜($.↑, ☕.↓){↶}
  val δ = ◇(Act - τ){tt} ∧ ⬜(Act - ($.↑, ☕.↓)){↶}

//  val η = ◇($.↑){tt} ∧ ⬜($.↑){↶}
//  val μ = ◇($.↑, ☕.↓){tt} ∧ ⬜($.↑, ☕.↓){↶}
//  val ν = ◇(Act){tt} ∧ ⬜(Act - ($.↑, ☕.↓)){↶}

//  val τ = ◇($.↑){tt} ∧ ⬜(Act - $.↑){ff} ∧ ⬜($.↑){↶}

  // todo: any finite seq of a-action ?
  // X ::= (1 ∨ <τ> ∨ <τ><τ> ∨ <τ><τ><τ>)
  // X+◇($.↑){tt} ∧ X+⬜(Act - $.↑){ff} ∧ ⬜($.↑){↶}
}
