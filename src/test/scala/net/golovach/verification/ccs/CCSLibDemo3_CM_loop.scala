package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// todo: back refs
// CM ≡ Coffee Machine
object CCSLibDemo3_CM_loop extends App {

  val LOOP = "$".↑ :: "☕".↓ :: ⟲
  val CM = "init".↑ |: LOOP

  println(dump(toLTS(CM)))
}
