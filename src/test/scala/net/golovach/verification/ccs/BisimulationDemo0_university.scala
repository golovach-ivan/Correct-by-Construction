package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.BisimulationLib._

object BisimulationDemo0_university extends App {

  val $ = "$"
  val ☕ = "☕"
  val ✉ = "✉"

  val CM = ↑($) :: ↓(☕) :: ⟲
  val CSh = ↓($) :: ↑(☕) :: ↓(✉) :: ⟲
  val CSr = (↓($) :: ↑(☕) :: ↓(✉) :: ⟲) + (↑(☕) :: ↓(✉) :: ⟲)
  val Univ = (CSh | CM | CSr) \ ($, ☕)

  val Spec = ↓(✉) :: ⟲

  println(Univ ~ Spec)
  println(Univ ≈ Spec)

  println(Univ.asLTS)
  println(saturate(Univ.asLTS))
}
