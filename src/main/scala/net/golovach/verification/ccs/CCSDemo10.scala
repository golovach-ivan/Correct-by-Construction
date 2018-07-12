package net.golovach.verification.ccs

import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.LTSLib._

// CM ≡ Coffee Machine
object CCSDemo10 extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  val CM0 = ($.↑ :: ☕.↓ :: ⟲("CM"))
  val CSh = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSh"))
  val CSr = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSr")) + (☕.↑ :: ✉.↓ :: ⟲("CSr"))

  val university = CSh | CM0 | CSr

  println(dump(CSr.toLTS()))
}
