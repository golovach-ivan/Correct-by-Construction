## java.util.concurrent.CyclicBarrier in RhoLang

A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point. CyclicBarriers are useful in programs involving a fixed sized party of threads that must occasionally wait for each other. The barrier is called cyclic because it can be re-used after the waiting threads are released.

A CyclicBarrier supports an optional Runnable command that is run once per barrier point, after the last thread in the party arrives, but before any threads are released. This barrier action is useful for updating shared-state before any of the parties continue ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CyclicBarrier.html)).

**java.util.concurrent.CyclicBarrier** (short version)   
```java
public class CyclicBarrier {
  CyclicBarrier(int parties) {...}
  int await() {...}
}
```

<details><summary><b>java.util.concurrent.CyclicBarrier</b> (long version)</summary><p>
  
```java
public class CyclicBarrier {
  // A new CyclicBarrier that will trip when the given number of parties are waiting upon it.
  CyclicBarrier(int parties) {...}

  // Waits until all parties have invoked await on this barrier.
  // Returns: the arrival index of the current thread, where index getParties() - 1 
  // indicates the first to arrive and zero indicates the last to arrive
  int await() {...}

  // Returns the number of parties currently waiting at the barrier.
  int getNumberWaiting() {...}

  // Returns the number of parties required to trip this barrier.
  int getParties() {...}
}
```
</p></details><br/>

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
