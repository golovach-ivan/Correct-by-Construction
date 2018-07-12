package net.golovach.verification.ccs

object LTSLib {

  type Edge = (LTSState, LTSAction, LTSState)
  object Edge {
    def apply(src: LTSState, act: LTSAction, dst: LTSState): Edge = (src, act, dst)
  }

  // ============ Port, Label, Action ============
//  final case class LTSPort(name: String) {
//    override def toString: String = name
//  }

  /**
    * LTS Actions have two syntaxes:
    * {{{
    *   val coinIn0 = ↑("coin")
    *   val coinIn1 = "coin".↑
    *
    *   val coffeeOut0 = ↑("coffee")
    *   val coffeeOut1 = "coffee".↑
    * }}}
    */
  sealed trait LTSAction/* {
    val port: LTSPort
  }*/

  /** Silent action */
  case object τ extends LTSAction/* {
    val port: LTSPort = ??? // todo
  }*/
  //
  final case class ↑(port: String) extends LTSAction {
    override def toString: String = '↑' + port
  }
  implicit def in(port: String) = new {
    def ↑ = new ↑(port)
  }
  //
  final case class ↓(port: String) extends LTSAction {
    override def toString: String = '↓' + port
  }
  implicit def out(port: String) = new {
    def ↓ = new ↓(port)
  }

  // ============ State ============
  /**
    * {State(A,0), State(A, 1)} | {State(B, 0), State(B, 1)} =>
    * {State(A,0), State(A, 1), State(B, 2), State(B, 3)}
    * @param name subsystem name
    * @param num end-to-end numbering
    */
  case class LTSState(name: String, num: Int) {
    override def toString: String = '#' + name + "_" + num
  }

  // ============ LTS ============
  // todo: edge == Derivatives and derivation trees, a-derivative, (a,b,c)-derivative
  final case class LTS(ports: Set[String] = Set(),
                       actions: Set[LTSAction] = Set(),
                       init: LTSState,
                       states: Set[LTSState] = Set(),
                       edges: Set[Edge] = Set(),
                       backLoops: Set[Edge] = Set() //todo: Set or Option ? (src, act, dst) or (src, act) ?
                      ) {

    def nextFreeId(name: String): Int =
      states.filter(_.name == name).maxBy(_.num).num + 1
  }

  // todo: rename
  def dump(lts: LTS): String = {
    s"ports:   ${lts.ports.mkString("{", ", ", "}")}\n" +
      s"actions: ${lts.actions.mkString("{", ", ", "}")}\n" +
      s"init:    ${lts.init}\n" +
      s"states:  ${lts.states.mkString("{", ", ", "}")}\n" +
      s"edges:   ${if (lts.edges.isEmpty) "{}" else lts.edges
        .map({case (a, b, c) => s"\n    ($a × $b) → $c"})
        .mkString("", "", "")}"

  }

  /**
    * Alias for [[dump() dump]] // todo
    */
  def dumpLTS(lts: LTS): String = dump(lts)
}

