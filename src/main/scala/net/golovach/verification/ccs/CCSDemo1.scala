package net.golovach.verification.ccs

import CCSLib._
import LTSLib._

// CM ≡ Coffee Machine
object CCSDemo1 extends App {

//  val CM = "coin".↑ :: "coffee".↓ :: ⟲("CM")

   val LOOP = "coin".↑ :: "coffee".↓ :: ⟲("LOOP")
   val CM = "initA".↑ :: (("initB".↑, "CM") |: LOOP)

  println(dump(CM.toLTS()))
}
