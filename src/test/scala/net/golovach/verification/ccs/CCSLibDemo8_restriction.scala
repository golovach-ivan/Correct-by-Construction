package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
// CS ≡ Computer Scientist
object CCSLibDemo8_restriction extends App {

  val $ = "$"
  val ☕ = "☕"
  val ✉ = "✉"

  val CM = $.↑ :: ☕.↓ :: ⟲

//  println(dump(toLTS(CM \ ✉)))
  println(dump(toLTS(CM \ (✉, $))))
//  println(dump(toLTS(CM \ ($, ☕))))
//  println(dump(toLTS("init".↑ :: (($.↑ :: ☕.↓ :: ⟲) \ $ \ ☕))))
}
