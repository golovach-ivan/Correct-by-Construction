package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
object CCSLibDemo5_sum extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  val CS = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲) + (☕.↑ :: ✉.↓ :: ⟲)
//  val CS = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CS")) + (☕.↑ :: ✉.↓ :: ⟲("CS"))
//  val CS = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("A")) + (☕.↑ :: ✉.↓ :: ⟲("B"))

  println(dump(toLTS(CS)))
}
