## java.util.concurrent.locks.ReadWriteLock

A *ReadWriteLock* maintains a pair of associated locks, one for read-only operations and one for writing. 
The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.

All *ReadWriteLock* implementations must guarantee that the memory synchronization effects of *writeLock* operations (as specified in the *Lock* interface) also hold with respect to the associated *readLock*. 
That is, a thread successfully acquiring the read lock will see all updates made upon previous release of the write lock ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReadWriteLock.html)).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

```java
public interface ReadWriteLock {
  // Returns the lock used for reading.
  Lock readLock();

  // Returns the lock used for writing.
  Lock writeLock();
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
