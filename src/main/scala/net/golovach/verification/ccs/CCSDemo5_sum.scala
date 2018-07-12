package net.golovach.verification.ccs

import CCSLib._
import LTSLib._

// CM ≡ Coffee Machine
object CCSDemo5_sum extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  val CS = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CS")) + (☕.↑ :: ✉.↓ :: ⟲("CS"))

  println(dump(CS.toLTS()))
}
