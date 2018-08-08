package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
object CCSLibDemo1_CM_disposable extends App {

  val CM = ↑("$") :: ↓("☕") :: ∅
//  val CM = ↑("$") :: ↓("☕") :: ∅("s")
//  val CM = ↑("$") :: ↓("☕") :: ∅("CM")

//  toLTS(CM)
  println(dump(toLTS(CM)))
}