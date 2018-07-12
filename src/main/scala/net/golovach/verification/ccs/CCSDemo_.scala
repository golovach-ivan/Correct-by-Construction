package net.golovach.verification.ccs

import CCSLib._
import LTSLib._

object CCSDemo_ extends App {

  val $ = "coin"
  val ☕ = "coffee"
  val ✉ = "pub"

  def CM: Process = $.↑ :: ☕.↓ :: ⟲("CM")
  def CSh = $.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSh")
  def CSr = ($.↓ :: ☕.↑ :: ✉.↓ :: ⟲("CSr")) + (☕.↑ :: ✉.↓ :: ⟲("CSr"))

  val University = CSh | CM | CSr
}
