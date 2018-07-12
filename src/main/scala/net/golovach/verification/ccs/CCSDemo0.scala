package net.golovach.verification.ccs

import LTSLib._
import CCSLib._

// CM ≡ Coffee Machine
object CCSDemo0 extends App {

  val CM = "coin".↑ :: "coffee".↓ :: ∅("CM")
  println(dump(CM.toLTS()))
//
//  println

//  val state0 = LTSState("CM", 0)
//  val state1 = LTSState("CM", 1)
//  val state2 = LTSState("CM", 2)
//  val lts = LTS(
//    ports = Set("coin", "coffee"),
//    actions = Set("coin".↑, "coffee".↓),
//    states = Set(state2, state1, state0),
//    init = state2,
//    edges = Set(
//      Edge(state2, "coin".↑, state1),
//      Edge(state1, "coffee".↓, state0)))
//
//  println(dump(lts))
}