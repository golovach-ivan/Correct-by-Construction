package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

// CM ≡ Coffee Machine
object CCSLibDemo9_ERROR extends App { //todo: error

//  val A = "A0".↑ :: "A1".↑ :: ⟲("AA")
//  val B = "B0".↑ :: "B1".↑ :: ⟲("AA")
//  val C = "C0".↑ :: "C1".↑ :: ⟲("AA")

  val A = "A0".↑  :: "A1".↑  :: ⟲("AA")
  val B = "B0".↑  :: "B1".↑  :: ⟲("BB")
  val C = "C0".↑  :: "C1".↑  :: ⟲("AA")
  val D = "D0".↑  :: "D1".↑  :: ⟲("BB")

  println(dump(toLTS((A + B) | (C + D))))

//  val AB = A + B
//  val CD = C + D
//  println(dump(toLTS(AB | CD)))
}
