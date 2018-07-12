package net.golovach.verification.ccs

import CCSLib._
import LTSLib._

// CM ≡ Coffee Machine
// CS ≡ Computer Scientist
object CCSDemo7_par extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  val CM = $.↑ :: ☕.↓ :: ⟲("CM")
  val CS = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CS")

  println(dump((CM | CS).toLTS()))
}
