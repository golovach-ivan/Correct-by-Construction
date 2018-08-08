package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// todo: back refs
// CM ≡ Coffee Machine
object CCSLibDemo4_CM_loop extends App {

  val LOOP = "$".↑ :: "☕".↓ :: ⟲
  val CM = "initA".↑ :: ("initB".↑ |: LOOP)

  println(dump(toLTS(CM)))
}
