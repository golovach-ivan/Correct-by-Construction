## java.util.concurrent.Phaser in RhoLang

A reusable synchronization barrier, similar in functionality to CyclicBarrier and CountDownLatch but supporting more flexible usage.

**Registration**. Unlike the case for other barriers, the number of parties registered to synchronize on a phaser may vary over time. Tasks may be registered at any time (using methods register(), bulkRegister(int), or forms of constructors establishing initial numbers of parties), and optionally deregistered upon any arrival (using arriveAndDeregister()).

**Synchronization**. Like a CyclicBarrier, a Phaser may be repeatedly awaited. Method arriveAndAwaitAdvance() has effect analogous to CyclicBarrier.await. Each generation of a phaser has an associated phase number. The phase number starts at zero, and advances when all parties arrive at the phaser [javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Phaser.html).

### Exercise
