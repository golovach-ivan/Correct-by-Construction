package net.golovach.verification.ccs

import net.golovach.verification.LTSLib._
import net.golovach.verification.ccs.BisimulationLib._

object BisimulationDemo2 extends App {

  val A = "a".↓
  val B = "b".↓

  val (p0, p1, p2, p3, p4, p5) =
    ('p ^ 0, 'p ^ 1, 'p ^ 2, 'p ^ 3, 'p ^ 4, 'p ^ 5)
  val ltsA = LTS(
    ports = Set("A", "B"),
    actions = Set(A, B),
    init = p0,
    states = Set(p0, p1, p2, p3, p4, p5),
    edges = Set(
      (p0, A) ➝ p1,
      (p1, B) ➝ p2,
      (p2, A) ➝ p3,
      (p3, B) ➝ p4,
      (p4, A) ➝ p5,
      (p5, B) ➝ p0,
      //
      (p0, A) ➝ p3,
      (p1, B) ➝ p4,
      (p2, A) ➝ p5,
      (p3, B) ➝ p0,
      (p4, A) ➝ p1,
      (p5, B) ➝ p2,
    )
  )

  val (q0, q1, q2, q3, q4, q5, q6, q7) =
    ('q ^ 0, 'q ^ 1, 'q ^ 2, 'q ^ 3, 'q ^ 4, 'q ^ 5, 'q ^ 6, 'q ^ 7)
  val ltsB = LTS(
    ports = Set("A", "B"),
    actions = Set(A, B),
    init = q0,
    states = Set(q0, q1, q2, q3, q4, q5, q6, q7),
    edges = Set(
      (q0, A) ➝ q1,
      (q1, B) ➝ q2,
      (q2, A) ➝ q3,
      (q3, B) ➝ q4,
      (q4, A) ➝ q5,
      (q5, B) ➝ q6,
      (q6, A) ➝ q7,
      (q7, B) ➝ q0,
      //
      (q0, A) ➝ q3,
      (q1, B) ➝ q4,
      (q2, A) ➝ q5,
      (q3, B) ➝ q6,
      (q4, A) ➝ q7,
      (q5, B) ➝ q0,
      (q6, A) ➝ q1,
      (q7, B) ➝ q2,
      //
      (q0, A) ➝ q5,
      (q1, B) ➝ q6,
      (q2, A) ➝ q7,
      (q3, B) ➝ q0,
      (q4, A) ➝ q1,
      (q5, B) ➝ q2,
      (q6, A) ➝ q3,
      (q7, B) ➝ q4,
    )
  )

  println(ltsA ~ ltsA)
  println(ltsA ≈ ltsA)
  println(strongBisimilarity(ltsA, ltsA))
  println(strongBisimilarity(ltsA, ltsB))
  println(strongBisimilarity(ltsB, ltsB))
}
