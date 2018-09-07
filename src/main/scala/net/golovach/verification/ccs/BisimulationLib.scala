package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.CCSLib._

import scala.collection.Iterator.iterate

trait BisimulationSyntax {

  def isStronglyBisimilar(xLTS: LTS, yLTS: LTS): Boolean

  def isWeaklyBisimilar(xLTS: LTS, yLTS: LTS): Boolean

  implicit class ltsBisimilarityOps(self: LTS) {
    /**
      * Check two LTS Strong Bisimilar
      */
    def ~(that: LTS): Boolean = isStronglyBisimilar(self, that)

    /**
      * Check two LTS not Strong Bisimilar
      */
    def ≁(that: LTS): Boolean = !isStronglyBisimilar(self, that)

    /**
      * Check two LTS Weak Bisimilar
      */
    def ≈(that: LTS): Boolean = isWeaklyBisimilar(self, that)

    /**
      * Check two LTS not Weak Bisimilar
      */
    def ≉(that: LTS): Boolean = !isWeaklyBisimilar(self, that)
  }

  implicit class ccsBisimilarityOps(self: Process) {
    /**
      * Check two CCP Processes Strong Bisimilar
      */
    def ~(that: Process): Boolean = self.asLTS ~ that.asLTS

    /**
      * Check two CCP Processes not Strong Bisimilar
      */
    def ≁(that: Process): Boolean = self.asLTS ≁ that.asLTS

    /**
      * Check two CCP Processes Weak Bisimilar
      */
    def ≈(that: Process): Boolean = self.asLTS ≈ that.asLTS

    /**
      * Check two CCP Processes not Weak Bisimilar
      */
    def ≉(that: Process): Boolean = self.asLTS ≉ that.asLTS
  }

}

/**

  *
  * Weak bisimilarity (observational equivalence)
  *
  * Observational congruence (rooted weak bisimilarity) (Milner, 1989)
  * ===
  * ???
  *
  * Branching bisimilarity (a variation of weak bisimilarity) (Glabbeek and Weijland, 1996)
  * ===
  * ???
  *
  *
  */
object BisimulationLib extends BisimulationSyntax {

  case class State(lts: LTS, st: ⌾) // todo: lts: LTS - ok? ot tag @@? or label {L,R}?
  type Block = Set[State]
  type Partition = Set[Block]

  /**
    * The algorithms for computing bisimilarity due to Kanellakis and Smolka,
    * and Paige and Tarjan, compute successive refinements of
    * an initial partition π-init and converge to the largest strong bisimulation
    * over the input finite labelled transition system. Algorithms that compute
    * strong bisimilarity in this fashion are often called partition-refinement
    * algorithms and reduce the problem of computing bisimilarity to that of
    * solving the so-called relational coarsest partitioning problem
    * (Kanellakis and Smolka, 1990; Paige and Tarjan, 1987).
    *
    * The basic idea underlying the algorithm by Kanellakis and Smolka is
    * to iterate the splitting of some block Bi by some block Bj with respect to
    * some action a until no further refinement of the current partition is possible.
    * The resulting partition is often called the coarsest stable partition
    * and coincides with strong bisimilarity over the input labelled transition
    * system when the initial partition π-init is chosen to be {Pr}.
    *
    * - Kanellakis and Smolka (Kanellakis and Smolka, 1983)
    * (see also the journal version (Kanellakis and Smolka, 1990))
    * - Paige and Tarjan (Paige and Tarjan, 1987)
    */
  def isStronglyBisimilar(xLTS: LTS, yLTS: LTS): Boolean = {

    val π: Partition = {
      val initPartition: Partition = Set(
        xLTS.states.map(State(xLTS, _)) ++ yLTS.states.map(State(yLTS, _))
      )
      unfold(initPartition)(tryRefine(_, xLTS.actions ++ yLTS.actions)).last
    }

    val whereXInit: Block = π.find(_.contains(State(xLTS, xLTS.init))).get
    val whereYInit: Block = π.find(_.contains(State(yLTS, yLTS.init))).get

    whereXInit == whereYInit
  }

  /**
    * The problem of checking weak bisimilarity (observational equivalence)
    * over finite labelled transition systems can be reduced to that
    * of checking strong bisimilarity using a technique called saturation.
    */
  def isWeaklyBisimilar(xLTS: LTS, yLTS: LTS): Boolean =
    isStronglyBisimilar(saturate(xLTS), saturate(yLTS))

  def splitBlock(block: Block, splitByBlock: Block, splitByAct: ⒜): (Block, Block) =
    block.partition({
      case State(lts: LTS, st: ⌾) =>
        val outgoingEdges: Set[➝] = lts.edgesFromWithAct(st, splitByAct)
        val reachableStates: Set[State] = outgoingEdges.map(x => State(lts, x.dst))
        (splitByBlock & reachableStates).nonEmpty
    })

  // todo: optimize, calculate first not all!
  def tryRefine(π: Partition, acts: Set[⒜]): Option[Partition] =
    (for {
      block: Block <- π
      splitByBlock: Block <- π
      splitByAct: ⒜ <- acts
      newPartition: Partition <- splitBlock(block, splitByBlock, splitByAct) match {
        case (reachable, unreachable) =>
          if (reachable.isEmpty || unreachable.isEmpty)
            Set.empty
          else
            Set(π.filter(_ != block) + reachable + unreachable)
      }
    } yield newPartition).headOption


  def unfold[A](a: A)(f: A => Option[A]): Stream[A] =
    a #:: (f(a) map (x => x #:: unfold(x)(f))).getOrElse(Stream.empty)

  /**
    * Strong Transition Relation -> Weak Transition Relation
    * pre-computing the weak transition relation
    */
  def saturate(lts: LTS): LTS = {
    type Arcs = Set[(⌾, ⌾)]

    val initTauArcs: Arcs = {
      val oldTauArcs = lts.edges.filter(_.act == τ).map(e => (e.src, e.dst))
      val selfTauArcs = lts.states.map(s => (s, s))
      oldTauArcs ++ selfTauArcs
    }

    // <src →τ→ *> + <* →τ→ dst> => <src →τ→ dst>
    def connectTauArcs(arcs: Arcs): Arcs =
      for { //todo: rename 'tmp'
        (src, tmp) <- arcs
        (`tmp`, dst) <- arcs
      } yield (src, dst)

    // (→τ→)∗
    val tauArcs: Arcs =
      iterate(initTauArcs)(connectTauArcs).drop(lts.states.size).next // todo: lts.states.size or more?

    // (→τ→)∗ => ➝*
    val newTauEdges: Set[➝] =
      tauArcs.map({ case (src, dst) => src × τ ➝ dst })

    // (→τ→)∗ ◦ →a→ ◦ (→τ→)∗
    val newNotTauEdges: Set[➝] = for {
      Edge(oldSrc, act, oldDts) <- lts.edges // todo: remove tau?
      newSrc <- lts.states.filter(tauArcs.contains(_, oldSrc))
      newDst <- lts.states.filter(tauArcs.contains(oldDts, _))
    } yield Edge(newSrc, act, newDst)

    lts.copy(
      actions = lts.actions + τ,
      edges = lts.edges ++ newTauEdges ++ newNotTauEdges
    )
  }
}