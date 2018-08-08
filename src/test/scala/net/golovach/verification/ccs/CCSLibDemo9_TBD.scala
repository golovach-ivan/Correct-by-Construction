package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

object CCSLibDemo9_TBD extends App {

  val $ = "$"
  val ☕ = "☕"
  val ✉ = "✉"

  def CM: Process = $.↑ :: ☕.↓ :: ⟲("CM")
  def CSh = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSh")
  def CSr = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSr")) + (☕.↑ :: ✉.↓ :: ⟲("CSr"))

  val University = CSh | CM | CSr
}
