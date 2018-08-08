package net.golovach.verification

import net.golovach.verification.LTSLib._

// CM ≡ Coffee Machine
object LTSLibDemo1_CM extends App {

  val $ = "$"
  val ☕ = "☕"

  val lts = LTS(
    ports = ($, ☕),
    actions = (↑($), ↓(☕)),
    states = ('s0, 's1, 's2),
    init = 's0,
    edges = Set(
      's0 × ↑($) ➝ 's1,
      's1 × ↓(☕) ➝ 's2
    ))

//  val lts = LTS(
//    ports = ($, ☕),
//    actions = (↑($), ↓(☕)),
//    states = ('s^0, 's^1, 's^2),
//    init = 's^0,
//    edges = Set(
//      ('s^0) × ↑($) ➝ ('s^1),
//      ('s^1) × ↓(☕) ➝ ('s^2)
//    ))

  println(dump(lts))
}