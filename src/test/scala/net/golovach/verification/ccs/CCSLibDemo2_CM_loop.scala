package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
object CCSLibDemo2_CM_loop extends App {

//  val CM = ↑("$") :: ↓("☕") :: ⟲
//  val CM = ↑("$") :: ↓("☕") :: ⟲("s")
  val CM = "$".↑ :: "☕".↓ :: ⟲("CM")

  println(dump(toLTS(CM)))
}
