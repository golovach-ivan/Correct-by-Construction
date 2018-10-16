## java.util.concurrent.locks.ReadWriteLock

A *ReadWriteLock* maintains a pair of associated locks, one for read-only operations and one for writing. 
The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.

All *ReadWriteLock* implementations must guarantee that the memory synchronization effects of *writeLock* operations (as specified in the *Lock* interface) also hold with respect to the associated *readLock*. 
That is, a thread successfully acquiring the read lock will see all updates made upon previous release of the write lock ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReadWriteLock.html)).

```java
public interface ReadWriteLock {
  // Returns the lock used for reading.
  Lock readLock();

  // Returns the lock used for writing.
  Lock writeLock();
}
```

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

### State / Operations Model
- \[0\] - free
- \[count > 0\] - read lock held count
- "W_LOCK" - write lock held
```
"W_LOCK" <-> [0] <-> [1] <-> [2] <-> [3]  <-> ...
```

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
