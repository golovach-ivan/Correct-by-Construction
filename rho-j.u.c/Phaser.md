## java.util.concurrent.Phaser in RhoLang

A reusable synchronization barrier, similar in functionality to CyclicBarrier and CountDownLatch but supporting more flexible usage.

**Registration**. Unlike the case for other barriers, the number of parties registered to synchronize on a phaser may vary over time. Tasks may be registered at any time (using methods register(), bulkRegister(int), or forms of constructors establishing initial numbers of parties), and optionally deregistered upon any arrival (using arriveAndDeregister()).

**Synchronization**. Like a CyclicBarrier, a Phaser may be repeatedly awaited. Method arriveAndAwaitAdvance() has effect analogous to CyclicBarrier.await. Each generation of a phaser has an associated phase number. The phase number starts at zero, and advances when all parties arrive at the phaser [javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Phaser.html).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

<details><summary><b>java.util.concurrent.Phaser.java</b></summary><p>
  
```
TBD
```
</p></details><br/>

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
