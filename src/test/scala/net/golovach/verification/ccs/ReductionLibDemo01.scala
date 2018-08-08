package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.ReductionLib._

object ReductionLibDemo01 extends App {

  val a = "a"

//  val p = (('a.↑ :: ∅) | ('a.↓ :: ∅)) | ((('a.↑ :: ∅) | ('a.↓ :: ∅)) | ('a.↑ :: ∅))
  val p = ((a.↑ :: ∅) | (a.↓ :: ∅)) | ((∅ | (a.↓ :: ∅)) | ∅)

  println(p)
  println(p \ ∅)
}
