package net.golovach.verification.ccs

import CCSLib._
import LTSLib._
import HMLLib._

object Test0 extends App {

  // === Hennessy-Milner Logic ===
  sealed trait HML
  case object tt extends HML
  case object ff extends HML
  case object ↶ extends HML
  final case class or(φ: HML, ψ: HML) extends HML
  final case class and(φ: HML, ψ: HML) extends HML
  final case class exists(action: LTSAction, next: HML) extends HML
  final case class forall(action: LTSAction, next: HML) extends HML

  implicit class OpsOr(φ: HML) {
    def ∧(ψ: HML): HML = and(φ, ψ)
    def ∨(ψ: HML): HML = or(φ, ψ)
  }
  case class ◇(action: LTSAction) {
    def apply(next: HML): HML = exists(action, next)
  }
  case class ⬜(action: LTSAction) {
    def apply(next: HML): HML = exists(action, next)
  }

  implicit class OpsCalculiLogic(p: Process) {
    def ⊨(φ: HML): Boolean = φ match {
      case tt => true
      case ff => false
      case and(a, b) => (p ⊨ a) & (p ⊨ b)
      case or(a, b) => (p ⊨ a) | (p ⊨ b)
    }
  }

  // === Example === ·

  val CM = "coin".↑ :: "coffee".↓ :: ∅("CM")
  val φ = tt ∧ ff
  val ψ = ◇("coin".↑){tt} ∧ ⬜("coin".↑){↶}

  val witnessA = CM ⊨ φ
  val witnessB = CM ⊨ ψ
}
