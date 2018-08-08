package net.golovach.verification

import net.golovach.verification.LTSLib.{Edge, LTSAction, LTSState, ↑, ↓, ⌾, ⒜, ➝}

// todo: more arrows
//    def → = ???
//    def ⟶ = ???
//    def ➤ = ???
//    def ⇁ = ???
//    def ⥤ = ???
//    def ⇉ = ???
//    def ⟹ = ???
//    def ↦ = ???
//    def ↝ = ???
//    def ➡ = ???
//    def ➛ = ???
//    def ↠ = ???
//    def ⤞ = ???
//    def ✓ = ???

//type ○ = LTSState
//type Ⅳ = LTSState
//type ◇ = LTSState

trait LTSImplicits {
  implicit def stringTuple2toSet(s: (String, String)): Set[String] =
    Set(s._1, s._2)

  implicit def stringTuple3toSet(s: (String, String, String)): Set[String] =
    Set(s._1, s._2, s._3)

  implicit def stringTuple4toSet(s: (String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4)

  implicit def stringTuple5toSet(s: (String, String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4, s._5)

  implicit def stringTuple6toSet(s: (String, String, String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6)

  implicit def stringTuple7toSet(s: (String, String, String, String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7)

  implicit def stringTuple8toSet(s: (String, String, String, String, String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8)

  implicit def stringTuple9toSet(s: (String, String, String, String, String, String, String, String, String)): Set[String] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8, s._9)

  implicit def symbolToState(s: Symbol): ⌾ =
    LTSState(s.name, -1)

  implicit def symbolTuple2toSet(s: (Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2)

  implicit def symbolTuple3toSet(s: (Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3)

  implicit def symbolTuple4toSet(s: (Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4)

  implicit def symbolTuple5toSet(s: (Symbol, Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5)

  implicit def symbolTuple6toSet(s: (Symbol, Symbol, Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6)

  implicit def symbolTuple7toSet(s: (Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7)

  implicit def symbolTuple8toSet(s: (Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8)

  implicit def symbolTuple9toSet(s: (Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol, Symbol)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8, s._9)

  implicit def actTuple2toSet(s: (LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2)

  implicit def actTuple3toSet(s: (LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3)

  implicit def actTuple4toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3, s._4)

  implicit def actTuple5toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3, s._4, s._5)

  implicit def actTuple6toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6)

  implicit def actTuple7toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7)

  implicit def actTuple8toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8)

  implicit def actTuple9toSet(s: (LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction, LTSAction)): Set[LTSAction] = Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8, s._9)


  implicit def stateTuple3toSet(s: (⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3)

  implicit def stateTuple4toSet(s: (⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4)

  implicit def stateTuple5toSet(s: (⌾, ⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5)

  implicit def stateTuple6toSet(s: (⌾, ⌾, ⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6)

  implicit def stateTuple7toSet(s: (⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7)

  implicit def stateTuple8toSet(s: (⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8)

  implicit def stateTuple9toSet(s: (⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾, ⌾)): Set[⌾] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8, s._9)

  implicit def edgeTuple2toSet(s: (➝, ➝)): Set[➝] =
    Set(s._1, s._2)

  implicit def edgeTuple3toSet(s: (➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3)

  implicit def edgeTuple4toSet(s: (➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4)

  implicit def edgeTuple5toSet(s: (➝, ➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4, s._5)

  implicit def edgeTuple6toSet(s: (➝, ➝, ➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6)

  implicit def edgeTuple7toSet(s: (➝, ➝, ➝, ➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7)

  implicit def edgeTuple8toSet(s: (➝, ➝, ➝, ➝, ➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8)

  implicit def edgeTuple9toSet(s: (➝, ➝, ➝, ➝, ➝, ➝, ➝, ➝, ➝)): Set[➝] =
    Set(s._1, s._2, s._3, s._4, s._5, s._6, s._7, s._8, s._9)
}

trait LTSStateImplicits {

  // todo: remove / rename
  implicit class xxx(name: Symbol) {
    def ^(num: Int): LTSState = LTSState(name.name, num)

    def ⌾(num: Int): LTSState = LTSState(name.name, num)
  }

}

trait LTSActionImplicits {
  implicit def inOps(port: String) = new {
    def ↑ = new ↑(port)
  }

  // todo: ?
  //  implicit def inOpsS(port: Symbol) = new {
  //    def ↑ = new ↑(port.name)
  //  }


  implicit def outOps(port: String) = new {
    def ↓ = new ↓(port)
  }

  // todo: ?
  //  implicit def outOpsS(port: Symbol) = new {
  //    def ↓ = new ↓(port.name)
  //  }
}

trait LTSEdgeImplicits {


  // todo: remove? 4 version?
  implicit class EdgeOpsA(from: (LTSState, ⒜)) {
    def ➝(dst: LTSState): ➝ = Edge(from._1, from._2, dst)
  }

  implicit class EdgeOpsB(from: (Symbol, ⒜)) {
    def ➝(dst: Symbol): ➝ = Edge(from._1, from._2, dst)
  }

  implicit class EdgeOpsX(src: LTSState) {
    def ×(act: LTSAction) = new {
      def ➝(dst: LTSState): ➝ = Edge(src, act, dst)

      def ➝(dst: Symbol): ➝ = Edge(src, act, dst)
    }
  }

  implicit class EdgeOpsY(src: Symbol) {
    def ×(act: LTSAction) = new {
      def ➝(dst: LTSState): ➝ = Edge(src, act, dst)

      def ➝(dst: Symbol): ➝ = Edge(src, act, dst)
    }
  }

}

object LTSLib extends LTSImplicits with LTSStateImplicits with LTSActionImplicits with LTSEdgeImplicits {

  type ⌾ = LTSState
  type ⒜ = LTSAction
  type ➝ = Edge

  // ============ State ============
  /**
    * @param name subsystem name
    * @param num  end-to-end numbering
    */
  case class LTSState(name: String, num: Int) {
    override def toString: String = num match {
      case -1 => '\'' + name
      case _ => '\'' + name + num.toString
    }
  }


  // ============ Action ============
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
  sealed trait LTSAction {
    self =>
    val portOpt: Option[String] = this match {
      case ↑(p) => Some(p)
      case ↓(p) => Some(p)
      case `τ` => None
    }

    def ⇅(that: LTSAction): Boolean = (self, that) match {
      case (↑(x), ↓(y)) => x == y
      case (↓(x), ↑(y)) => x == y
      case _ => false
    }
  }

  /** Silent action */
  case object τ extends LTSAction

  final case class ↑(port: String) extends LTSAction {
    override def toString: String = '↑' + port
  }

  final case class ↓(port: String) extends LTSAction {
    override def toString: String = '↓' + port
  }


  // ============ Edge ============
  case class Edge(src: LTSState, act: ⒜, dst: LTSState)

  // ============ LTS ============

  // todo: edge == Derivatives and derivation trees, a-derivative, (a,b,c)-derivative
  // todo: add bisimilarity relation (weak and strong)
  final case class LTS(ports: Set[String] = Set(),
                       actions: Set[⒜] = Set(),
                       states: Set[⌾] = Set(),
                       init: ⌾,
                       edges: Set[➝] = Set(),
                       backLoops: Set[➝] = Set(), //todo: Set or Option ? (src, act, dst) or (src, act) ? move backLoops to Process
                      ) {
    assert(ports == actions.flatMap(_.portOpt.toSet))
    assert(states.contains(init))
    assert(edges.flatMap(e => Set(e.src, e.dst)).subsetOf(states))
    assert(edges.map(_.act) == actions)
    //todo: edges uniques?

    // todo: rewrite / remove
    def nextFreeId(name: String): Int =
      states.filter(_.name == name).maxBy(_.num).num + 1

    def edgesFrom(src: LTSState): Set[➝] =
    //      edges.collect({ case e@((`src`, _) ➝ _) => e })
      edges.collect({ case e@Edge(`src`, _, _) => e })

    def edgesFromWithAct(src: LTSState, act: LTSAction): Set[➝] =
      edges.collect({ case e@Edge(`src`, `act`, _) => e })

    def actionsFrom(src: LTSState): Set[LTSAction] =
      edges.map({ case Edge(`src`, act, _) => act })
  }

  // todo: rename
  def dump(lts: LTS): String = {
    s"ports:   ${lts.ports.mkString("{", ", ", "}")}\n" +
      s"actions: ${lts.actions.mkString("{", ", ", "}")}\n" +
      s"states:  ${lts.states.mkString("{", ", ", "}")}\n" +
      s"init:    ${lts.init}\n" +
      s"edges:   ${
        if (lts.edges.isEmpty) "{}" else lts.edges
          .map({ case Edge(a, b, c) => s"\n    $a × $b → $c" })
          //          .map({ case Edge(a, b, c) => s"\n    $a → $b → $c" })
          .mkString("", "", "")
      }"

  }

  /**
    * Alias for [[dump() dump]] // todo
    */
  def dumpLTS(lts: LTS): String = dump(lts)
}

