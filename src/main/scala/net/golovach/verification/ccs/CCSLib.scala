package net.golovach.verification.ccs

import LTSLib.{LTSAction, _}

object CCSLib {

  sealed trait Process {
    val name: String

    //
    def ::(act: LTSAction): Process = Prefix(name, act, this)

    def |:(act_newName: (LTSAction, String)): Process = Prefix(act_newName._2, act_newName._1, this)

    def +(that: Process): Process = Sum(this, that)

    def |(that: Process): Process = Par(this, that)

    def toLTS(startIndex: Int = 0): LTS = this match {
      case ∅(_) =>
        val state = LTSState(name, startIndex)
        LTS(init = state, states = Set(state))

      case ⟲(_) =>
        val state = LTSState(name, startIndex)
        LTS(init = state, states = Set(state))

      case Prefix(newName, newAct, nextProcess) =>
        val prevLTS = nextProcess.toLTS(startIndex)

        // === ports, actions
        val newPorts = newAct match {
          case ↑(port) => prevLTS.ports + port
          case ↓(port) => prevLTS.ports + port
          case `τ` => prevLTS.ports
        }
        val newActs = prevLTS.actions + newAct

        // === states, init
        val newState = nextProcess match {
          case ⟲(_) => prevLTS.init
          case _ => LTSState(newName, prevLTS.nextFreeId(name))
        }
        val newStates = prevLTS.states + newState
        val newInit = newState

        // === EDGES ===

        val newBackLoops = nextProcess match {
          case ⟲(_) =>
            Set((prevLTS.init, newAct, prevLTS.init))
          case _ => prevLTS.backLoops
            .map({ case (src, act, _) => (src, act, newState) })
        }
        val newEdge = (newState, newAct, prevLTS.init)
        val newEdges = prevLTS.edges -- prevLTS.backLoops ++ newBackLoops + newEdge

        //
        LTS(newPorts, newActs, newInit, newStates, newEdges, newBackLoops)

      case Sum(p, q) =>
        val pLTS = p.toLTS()
        val qLTS = q.toLTS(if (p.name == q.name) pLTS.states.size else 0)

        // == ports, actions
        val newPorts = pLTS.ports ++ qLTS.ports
        val newActions = pLTS.actions ++ qLTS.actions

        // == startState, states
        val (newStartState, newStates) = if (p.name == q.name) {
          val newStartState = pLTS.init
          val newStates = pLTS.states ++ qLTS.states - qLTS.init
          (newStartState, newStates)
        } else {
          val newStartState = LTSState(s"(${p.name}+${q.name})", 0)
          val newStates = pLTS.states ++ qLTS.states - qLTS.init -
            pLTS.init + newStartState
          (newStartState, newStates)
        }

        // == edges, edgesBackLoops
        val map = Map(
          pLTS.init -> newStartState,
          qLTS.init -> newStartState
        )
        val mapF: Edge => Edge = {
          case (src, act, dst) =>
            (map.getOrElse(src, src), act, map.getOrElse(dst, dst))
        }
        val newEdges: Set[Edge] = (pLTS.edges ++ qLTS.edges).map(mapF)
        val newEdgesBackLoops = Set[(LTSState, LTSAction, LTSState)]()
        //
        LTS(newPorts, newActions, newStartState, newStates, newEdges, newEdgesBackLoops)

      case Par(p, q) =>
        val pLTS = p.toLTS()
        val qLTS = q.toLTS()
        implicit class ops(pSt: LTSState) {
          def ⊗(qSt: LTSState) =
            LTSState(s"(${p.name}|${q.name})", pSt.num * qLTS.states.size + qSt.num)
        }

        // === ports, actions
        val newPorts = pLTS.ports ++ qLTS.ports
        val newActions = pLTS.actions ++ qLTS.actions // todo: add \tau?

        // == startState, states
        val newStartState = pLTS.init ⊗ qLTS.init
        val newStates = for {
          pState <- pLTS.states
          qState <- qLTS.states
        } yield pState ⊗ qState

        // == edges, edgesBackLoops
        val newEdges =
          pLTS.edges.flatMap({
            case (pSrc, pAct, pDst) =>
              qLTS.states.map(qState => (pSrc ⊗ qState, pAct, pDst ⊗ qState))
          }) ++ qLTS.edges.flatMap({
            case (qSrc, qAct, qDst) =>
              pLTS.states.map(pState => (pState ⊗ qSrc, qAct, pState ⊗ qDst))
          })

        val newEdgesBackLoops = Set[(LTSState, LTSAction, LTSState)]()
        //
        LTS(newPorts, newActions, newStartState, newStates, newEdges, newEdgesBackLoops)

      case Renaming(x) => ??? // todo
      case Restriction(x) => ??? // todo
    }
  }

  final case class ∅(name: String) extends Process

  final case class ⟲(name: String) extends Process

  final case class Prefix(name: String, action: LTSAction, next: Process) extends Process

  final case class Sum(p: Process, q: Process) extends Process {
    val name = if (p.name == q.name) name else s"${p.name}+${q.name}"
  }

  final case class Par(p: Process, q: Process) extends Process {
    val name = p.name + "|" + q.name
  }

  final case class Renaming(? : Nothing) extends Process { // todo
    val name = ??? // todo
  }

  final case class Restriction(? : Nothing) extends Process { // todo
    val name = ??? // todo
  }

}
