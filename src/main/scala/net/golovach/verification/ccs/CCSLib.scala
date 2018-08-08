package net.golovach.verification.ccs

import net.golovach.verification.LTSLib.{Edge, _}

/**
  * todo: name and combinators
  * todo: recursion tail and combinators
  * Combinators and recursion:
  * - Prefix - save
  * - Sum - remove
  * - Par - remove
  * - Restriction - remove
  * - Renaming - remove
  */
object CCSLib {

  // todo: add bisimilarity relation (weak and strong)
  // todo: add trace equivalence
  sealed trait Process {
    val name: String

    def ::(act: LTSAction): Process = Prefix(name, act, this)

    def |:(act: LTSAction): Process = Prefix(this.name, act, this)

    def |:(act_newName: (LTSAction, String)): Process = Prefix(act_newName._2, act_newName._1, this)

    def +(that: Process): Process = Sum(this, that)

    def |(that: Process): Process = Par(this, that)

    // todo: constrained
    def \(ports: String*): Process = Restriction(this, ports)

    def ∘(m: (String, String)*): Process = Renaming(this, m.toMap)
  }

  // todo: default value?
  sealed class ∅(override val name: String) extends Process {
    override def toString = "∅"
  }

  object ∅ extends ∅("s") {
    def apply(name: String): ∅ = new ∅(name)
    def unapply(o: ∅): Option[String] = Some(o.name)
  }

  // todo: choose = ⤺↶↺⟲⥀
  sealed class ⟲(override val name: String) extends Process

  object ⟲ extends ⟲("s") {
    def apply(name: String): ⟲ = new ⟲(name)
    def unapply(o: ⟲): Option[String] = Some(o.name)
  }

  final case class Prefix(name: String, action: LTSAction, next: Process) extends Process {
    override def toString: String = s"$action.($next)"
  }

  final case class Sum(p: Process, q: Process) extends Process {

    override val name = if (p.name == q.name) p.name else s"(${p.name}+${q.name})"

    override def toString: String = (p, q) match {
      case (∅(_), ∅(_)) => s"∅ + ∅"
      case (∅(_), _) => s"∅ + ($q)"
      case (_, ∅(_)) => s"($p) + ∅"
      case _ => s"($p) + ($q)"
    }
  }

  final case class Par(p: Process, q: Process/*, name: String*/) extends Process {

    override val name = s"(${p.name}|${q.name})"

    override def toString: String = (p, q) match {
      case (∅(_), ∅(_)) => s"∅ | ∅"
      case (∅(_), _) => s"∅ | ($q)"
      case (_, ∅(_)) => s"($p) | ∅"
      case _ => s"($p) | ($q)"
    }
  }
//  final case class Par(p: Process, q: Process) extends Process {
//    override val name = s"(${p.name}|${q.name})"
//  }

  final case class Restriction(p: Process, ports: Seq[String]) extends Process { // todo
    override val name = p.name
  }

  final case class Renaming(p: Process, m: Map[String, String]) extends Process { // todo
    override val name = p.name
  }

  // todo: rename startIndex?
  def toLTS(ppp: Process, startIndex: Int = 0): LTS = ppp match {
    case ∅(name) =>
      val newState = LTSState(name, startIndex)
      LTS(init = newState, states = Set(newState))

    case ⟲(name) =>
      val newState = LTSState(name, startIndex)
      LTS(init = newState, states = Set(newState))

    case Prefix(newName, newAct, nextProcess) =>
      val prevLTS = toLTS(nextProcess, startIndex)

      // === ports, actions
      val newPorts = prevLTS.ports ++ newAct.portOpt.toSet
      val newActs = prevLTS.actions + newAct

      // === states, init
      val newState = nextProcess match {
        case ⟲(_) => prevLTS.init
        case _ => LTSState(newName, prevLTS.nextFreeId(ppp.name)) //todo: rename 'ppp'
      }
      val newStates = prevLTS.states + newState
      val newInit = newState

      // === edges, backLoops ===
      val newBackLoops = nextProcess match {
        case ⟲(_) =>
          Set((prevLTS.init, newAct) ➝ prevLTS.init)
        case _ => prevLTS.backLoops
          .map({ case Edge(src, act, _) => (src, act) ➝ newState })
      }
      val newEdge = (newState, newAct) ➝ prevLTS.init
      val newEdges = prevLTS.edges -- prevLTS.backLoops ++ newBackLoops + newEdge

      // ===
      LTS(newPorts, newActs, newStates, newInit, newEdges, newBackLoops)

    case Sum(p, q) =>
      val eqNames = p.name == q.name
      val pLTS = toLTS(p, startIndex)
      val qLTS = toLTS(q, startIndex + (if (eqNames) pLTS.states.size else 0))

      // == ports, actions
      val newPorts = pLTS.ports ++ qLTS.ports
      val newActions = pLTS.actions ++ qLTS.actions

      // == init, states
      val (newInit, newStates) =
        if (eqNames) {
          (pLTS.init, pLTS.states ++ qLTS.states - qLTS.init + pLTS.init)
        } else {
          val newInit = LTSState(s"(${p.name}+${q.name})", 0)
          val newStates = pLTS.states ++ qLTS.states -
            qLTS.init - pLTS.init + newInit
          (newInit, newStates)
        }

      // == edges, edgesBackLoops
      val mapping = Map(
        pLTS.init -> newInit,
        qLTS.init -> newInit
      )
      val newEdges = (pLTS.edges ++ qLTS.edges).map({
        case Edge(src, act, dst) =>
          (mapping.getOrElse(src, src), act) ➝ mapping.getOrElse(dst, dst)
      })

      //
      LTS(newPorts, newActions, newStates, newInit, newEdges, backLoops = Set())

    case Par(p, q) =>
      val pLTS = toLTS(p)
      val qLTS = toLTS(q)
      implicit class ops(pSt: LTSState) {
        def ⊗(qSt: LTSState) = {
            // todo: restore
          val r = LTSState(s"(${p.name}|${q.name})", pSt.num * qLTS.states.size + qSt.num + startIndex)
//          println(s"$pSt * $qSt -> $r")
          r
        }
      }

      // === ports, actions
      val newPorts = pLTS.ports ++ qLTS.ports
      val newTauActions = for {
        pa <- pLTS.actions
        qa <- qLTS.actions
        tau <- if (pa ⇅ qa) Set[LTSAction](τ) else Set[LTSAction]()
      } yield tau
      val newActions = pLTS.actions ++ qLTS.actions ++ newTauActions

      // == init, states
      val newInit = pLTS.init ⊗ qLTS.init
      val newStates = for {
        pState <- pLTS.states
        qState <- qLTS.states
      } yield pState ⊗ qState

      // == edges, edgesBackLoops
      val newTauEdges = for {
        Edge(pSrc, pAct, pDst) <- pLTS.edges
        Edge(qSrc, qAct, qDst) <- qLTS.edges
        tauEdge <- if (pAct ⇅ qAct) Set((pSrc ⊗ qSrc, τ) ➝ (pDst ⊗ qDst)) else Set[➝]()
      } yield tauEdge

      val newEdges = pLTS.edges.flatMap({
        case Edge(pSrc, pAct, pDst) =>
          qLTS.states.map(qState => (pSrc ⊗ qState, pAct) ➝ (pDst ⊗ qState))
      }) ++ qLTS.edges.flatMap({
        case Edge(qSrc, qAct, qDst) =>
          pLTS.states.map(pState => (pState ⊗ qSrc, qAct) ➝ (pState ⊗ qDst))
      }) ++ newTauEdges

      // ===
      LTS(newPorts, newActions, newStates, newInit, newEdges, backLoops = Set())

    case Restriction(p, closedPorts) =>
      val lts = toLTS(p) //todo: startIndex?

      // === ports, actions
      val newPorts = lts.ports -- closedPorts
      val newActions = lts.actions -- closedPorts.map(_.↑) -- closedPorts.map(_.↓)

      // == init, states
      val newInit = lts.init
      val newStates = lts.states // todo: remove unreachable states

      // == edges, edgesBackLoops
      val newEdges = lts.edges.filterNot({
        case Edge(_, act, _) => act.portOpt.exists(closedPorts.contains(_))
      })
      //
      LTS(newPorts, newActions, newStates, newInit, newEdges, backLoops = Set())

    case Renaming(p, mapping) =>

      def mapAct(act: LTSAction, f: String => String): LTSAction = act match {
        case ↑(x) => ↑(f(x))
        case ↓(x) => ↓(f(x))
        case `τ` => τ
      }

      val lts = toLTS(p) //todo: startIndex?

      // === ports, actions
      val newPorts = lts.ports.map(port => mapping.getOrElse(port, port))
      val newActions = lts.actions.map(mapAct(_, port => mapping.getOrElse(port, port)))

      // == init, states
      val newInit = lts.init
      val newStates = lts.states

      val newEdges = lts.edges.map({
        case Edge(src, act, dst) =>
          (src, mapAct(act, port => mapping.getOrElse(port, port))) ➝ dst
      })
      // todo: backLoops?
      //
      LTS(newPorts, newActions, newStates, newInit, newEdges, backLoops = Set())
  }
}
