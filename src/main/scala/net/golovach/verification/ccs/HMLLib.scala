package net.golovach.verification.ccs

import LTSLib._
import CCSLib._

object HMLLib extends App {

  // Standard Hennessy-Milner Logic
  sealed trait HML {
    self =>
    def ∧(φ: HML): HML = and(self, φ)
    def ∨(φ: HML): HML = or(self, φ)
  }
  case object tt extends HML
  case object ff extends HML
  final case class ¬(φ: HML) extends HML
  final case class or(φ: HML, ψ: HML) extends HML
  final case class and(φ: HML, ψ: HML) extends HML
  final case class possible(action: LTSAction, next: HML) extends HML
  final case class necessary(action: LTSAction, next: HML) extends HML
  // Recursion tail
  case object ↶ extends HML

  // todo: add \neg
  // todo: add [-]

  sealed trait LTSActionExt
  case object Act extends LTSActionExt {
    def -(actions: LTSAction*): LTSActionExt = ActMinus(actions:_*)
  }
  case class ActMinus(actions: LTSAction*) extends LTSActionExt

  // === Syntax sugar
  def ⬜(actions: LTSAction*) = new {
    def apply(next: HML): HML =
      ((ff: HML) /: actions) (_ ∧ necessary(_, next))
  }

  def ◇(actions: LTSAction*) = new {
    def apply(next: HML): HML =
      ((tt: HML) /: actions) (_ ∨ possible(_, next))
  }

  def ⬜(actions: LTSActionExt) = new {
    def apply(next: HML): HML = ???
  }

  def ◇(actions: LTSActionExt) = new {
    def apply(next: HML): HML = ???
  }

  implicit class OpsSatisfaction(p: Process) {
    def ⊨(φ: HML): Boolean = φ match {
      case `tt` => true
      case `ff` => false
      case and(a, b) => (p ⊨ a) & (p ⊨ b)
      case or(a, b) => (p ⊨ a) | (p ⊨ b)
      case possible(action, next) => ???
      case necessary(action, next) => ???
    }
  }
}
