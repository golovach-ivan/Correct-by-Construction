package net.golovach.verification.ccs

import net.golovach.verification.ccs.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.HMLLib._

object Demo extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  val CM = $.↑ :: ☕.↓ :: ⟲("CM")
  val CSh = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSh")
  val CSr = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSr")) + (☕.↑ :: ✉.↓ :: ⟲("CSr"))

  val university = CSh | CM | CSr

  val ☠ = Act - ($.↓, τ)

//  val ☠ = ""

  val α =
    ◇($.↓){↶} ∨
      ◇(τ){◇($.↓){↶}} ∨
      ◇(τ){◇(τ){◇($.↓){↶}}}

  // does not exist: {τ,τ}-⊥-$-{τ,τ}-⊥
  val β =
    ⬜(☠){ff} ∧
      ⬜(τ){⬜(☠){ff}} ∧
      ⬜(τ){⬜(τ){⬜(☠){ff}}} ∧
      ⬜($.↓){⬜(☠){ff}} ∧
      ⬜($.↓){⬜(τ){⬜(☠){ff}}} ∧
      ⬜($.↓){⬜(τ){⬜(τ){⬜(☠){ff}}}} ∧
      ⬜(τ){⬜($.↓){⬜(☠){ff}}} ∧
      ⬜(τ){⬜($.↓){⬜(τ){⬜(☠){ff}}}} ∧
      ⬜(τ){⬜($.↓){⬜(τ){⬜(τ){⬜(☠){ff}}}}} ∧
      ⬜(τ){⬜(τ){⬜($.↓){⬜(☠){ff}}}} ∧
      ⬜(τ){⬜(τ){⬜($.↓){⬜(τ){⬜(☠){ff}}}}} ∧
      ⬜(τ){⬜(τ){⬜($.↓){⬜(τ){⬜(τ){⬜(☠){ff}}}}}}

  val φ = α ∧ β

  println(university ⊨ φ)
}
