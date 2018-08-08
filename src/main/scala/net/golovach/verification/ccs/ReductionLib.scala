package net.golovach.verification.ccs

import CCSLib._

object ReductionLib {

  implicit class ReductionOneOps(p: Process) {

    private[ReductionOneOps] def flatten(q: Process): Seq[Process] = q match {
      case Par(a, b) => flatten(a) ++ flatten(b)
      case _ => Seq(q)
    }

    // todo: how about process ⟲ ???
    def → : Seq[Process] =
      flatten(p) match {
        case one :: Nil =>
          Seq.empty
        //          Seq(one)
        case parComponents =>
          (for {
            r <- parComponents.indices
            l <- 0 until r
          } yield
            (parComponents(l), parComponents(r)) match {
              case (Prefix(_, actL, nextL), Prefix(_, actR, nextR)) if actL ⇅ actR =>
                Seq(parComponents.updated(r, nextR).updated(l, nextL).reduce(Par))
              case _ =>
                Seq.empty
            }).flatten

      }

    def ⇥ : Seq[Process] = {
      var (x0, x1) = (Seq(p), p →)
      while (x1.nonEmpty) {
        x0 = x1
        x1 = x1 →
      }
      x0
    }

    def \(r: Process): Process =
      flatten(p) filter (_ != r) match {
        case Nil => ∅
        case x => x.reduce(Par)
      }

    implicit class ReductionSetOps(p: Seq[Process]) {

      def → : Seq[Process] = p.flatMap(_ →)

      def ⇥ : Seq[Process] = ??? // ⇢

      //    def \(r: Process): Seq[Process] = {
      //      val res = p filter (_ != r)
      //      res
      //    }
      //    }
    }

  }

}
