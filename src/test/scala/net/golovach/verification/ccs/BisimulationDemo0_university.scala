package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._
import net.golovach.verification.ccs.BisimulationLib._

// CM ≡ Cofee Machine
// CSh ≡ Computer Scientist (honest)
// CSr ≡ Computer Scientist (real)
// Univ ≡ University (implementation)
// Spec ≡ Specification
object BisimulationDemo0_university extends App {

  val $ = "$"
  val ☕ = "☕"
  val ✉ = "✉"

  // === impl
  val CM = ↑($) :: ↓(☕) :: ⟲
  val CSh = ↓($) :: ↑(☕) :: ↓(✉) :: ⟲
  val CSr = (↓($) :: ↑(☕) :: ↓(✉) :: ⟲) + (↑(☕) :: ↓(✉) :: ⟲)
  val Univ = (CSh | CM | CSr) \ ($, ☕)

  // === spec
  val Spec = ↓(✉) :: ⟲

  println(Univ ~ Spec)
  println(Univ ≈ Spec)
}
