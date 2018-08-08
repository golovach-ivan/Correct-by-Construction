package net.golovach.verification

import LTSLib._

object LTSLibDemo0_syntax extends App {

  // *********************************** State
  val s0  = LTSState("s", 0)
  val s1: LTSState = 's1


  // *********************************** Action
  val a = ↑("a")
  val b = "$".↑
//  val c = ↑('d)
//  val d = '$.↑
  //
  val tau = τ
  //
  val x = a ⇅ b

  // *********************************** Edge
  val e0 = 's0 × ↑("$") ➝ 's1
  val e1 = 's0 × τ ➝ 's1
  val e2 = Edge('s0, ↑("$"), 's1)
  val e3 = Edge('s0, τ, 's1)


  // *********************************** Bisimilarity
//  println(ltsA ~ ltsA)
//  println(ltsA ≈ ltsA)

  // *********************************** Reduction
}
