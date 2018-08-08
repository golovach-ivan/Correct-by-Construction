package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
// CS ≡ Computer Scientist
object CCSLibDemo7_par extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

    val CM = $.↑ :: ☕.↓ :: ⟲("CM")
    val CS = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CS")
  // todo: (s|s)0
//  val CM = $.↑ :: ☕.↓ :: ⟲
//  val CS = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲

  println(dump(toLTS(CM | CS)))
}
