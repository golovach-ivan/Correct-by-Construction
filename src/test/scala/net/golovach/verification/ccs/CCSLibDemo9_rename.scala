package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
// CS ≡ Computer Scientist
object CCSLibDemo9_rename extends App {

  val $ = "$"
  val ☕ = "☕"

  val CM0 = ($.↑ :: ☕.↓ :: ⟲) ∘ ()
  val CM1 = ($.↑ :: ☕.↓ :: ⟲) ∘ ($ -> "A")
  val CM2 = ($.↑ :: ☕.↓ :: ⟲) ∘ ($ -> "A", ☕ -> "B")

  println(dump(toLTS(CM1)))
}
