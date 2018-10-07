## java.util.concurrent.locks.ReentrantLock in RhoLang

A reentrant mutual exclusion *Lock* with the same basic behavior and semantics as the implicit monitor lock accessed using *synchronized* methods and statements, but with extended capabilities. A ReentrantLock is owned by the thread last successfully locking, but not yet unlocking it. A thread invoking lock will return, successfully acquiring the lock, when the lock is not owned by another thread. The method will return immediately if the current thread already owns the lock ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReentrantLock.html)).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

```java
public class ReentrantLock implements Lock {
  // Acquires the lock.
  public void lock() {...}

  // Returns a new Condition instance that is bound to this Lock instance.
  public Condition newCondition() {...}

  // Acquires the lock only if it is free at the time of invocation.
  public boolean tryLock() {...}

  // Releases the lock.
  public void unlock() {...}
  
  // Queries the number of holds on this lock by the current thread.
  public int getHoldCount() {...}

  // Returns an estimate of the number of threads waiting to acquire this lock.
  public int getQueueLength() {...}

  // Queries if this lock is held by any thread.
  public boolean isLocked() {...}
}
```

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
