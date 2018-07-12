package net.golovach.verification.ccs

import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.LTSLib._

// CM ≡ Coffee Machine
object CCSDemo2 extends App {

  val LOOP = "coin".↑ :: "coffee".↓ :: ⟲("LOOP")

//  val CM = ("init".↑, "CM") |: LOOP
  val CM = "initA".↑ :: (("initB".↑, "CM") |: LOOP)

  println(dump(CM.toLTS()))
}
