package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import CCSLib._

import ReductionLib._

object ReductionLibDemo00 extends App {

  val a = "a"

  //  val p = ('a.↑ :: ∅)
  //  val p = ('a.↑ :: ∅) | ('b.↓ :: ∅)
  //  val p = ('a.↑ :: ∅) | ('a.↓ :: ∅)
  //  val p = ('a.↑ :: ∅) | ('b.↑ :: ∅) | ('c.↑ :: ∅)

  //  val p = ('a.↑ :: ∅) | ('a.↓ :: ∅) | ('a.↑ :: ∅)
  //  val p = ('a.↑ :: ∅) | ('a.↓ :: ∅) | ('b.↑ :: ∅) | ('b.↓ :: ∅)

  val p = (a.↑ :: ∅) | (a.↓ :: ∅) | (a.↑ :: ∅) | (a.↓ :: ∅)
  //  val p = ('a.↑ :: ∅) | ('a.↓ :: ∅) | ('a.↑ :: ∅) | ('a.↓ :: ∅) | ('a.↑ :: ∅)

  demo(p, 0)

  def demo(x: Process, n: Int): Unit = {
    for (_ <- 0 until n) print(" ")
    println(x)
    for (_ <- 0 until (n + 2)) print(" ")
    println(x \ ∅)
    (x →).foreach((s: Process) => demo(s, n + 2))
    ((x \ ∅) →).foreach((s: Process) => demo(s, n + 4))
  }

  //  (p →).foreach(s => println(s"  → $s"))
  //  println()
  //  (p ⇥).foreach(s => println(s"  → $s"))
}
