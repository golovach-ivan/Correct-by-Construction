## java.util.concurrent.locks.ReadWriteLock

A *ReadWriteLock* maintains a pair of associated locks, one for read-only operations and one for writing. 
The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.

All *ReadWriteLock* implementations must guarantee that the memory synchronization effects of *writeLock* operations (as specified in the *Lock* interface) also hold with respect to the associated *readLock*. 
That is, a thread successfully acquiring the read lock will see all updates made upon previous release of the write lock ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReadWriteLock.html)).

Although the basic operation of a read-write lock is straight-forward, there are many policy decisions that an implementation must make, which may affect the effectiveness of the read-write lock in a given application. Examples of these policies include:
- Determining whether to grant the read lock or the write lock, when both readers and writers are waiting, at the time that a writer releases the write lock. 
- Determining whether readers that request the read lock while a reader is active and a writer is waiting, are granted the read lock. 
- Determining whether the locks are reentrant: can a thread with the write lock reacquire it? Can it acquire a read lock while holding the write lock? Is the read lock itself reentrant?
- Can the write lock be downgraded to a read lock without allowing an intervening writer? Can a read lock be upgraded to a write lock, in preference to other waiting readers or writers?

You should consider all of these things when evaluating the suitability of a given implementation for your application.

```java
public interface ReadWriteLock {
  // Returns the lock used for reading.
  Lock readLock();

  // Returns the lock used for writing.
  Lock writeLock();
}
```

An implementation of ReadWriteLock supporting similar semantics to ReentrantLock. This class has the following properties:
- Acquisition order. This class does not impose a reader or writer preference ordering for lock access.

```java
public interface ReentrantReadWriteLock {
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

AtomicState = "W_LOCK" + Int\[N\] without explicit WaitSets.     
- \[0\] - free
- \[count > 0\] - read lock held count
- "W_LOCK" - write lock held
```
"W_LOCK" <-> [0] <-> [1] <-> [2] <-> [3]  <-> ...

R.lock        B            =>>          =>>        =>>         =>>
R.unlock      ?              ?          <<=        <<=         <<=
W.lock        B            <<=           B          B           B
W.unlock     =>>             ?           ?          ?           ?

           "W_LOCK"   <->   [0]   <->   [1]   <->   [2]   <->   [3]    <->   ...
```

#### Strategies
- block
- ignore / ignore + alert
- remove = key

### Explanation
<details><summary>???src</summary><p>
  
```
1  new ReadWriteLock in {
2    contract ReadWriteLock(readLockOp, writeLockOp) = {
3  
4      new readLock, writeLock in {
5        contract readLockOp(ret) = {ret!(readLock)} |
6        contract writeLockOp(ret) = {ret!(writeLock)} |
7       
8        new stateRef in {      
9          stateRef!([0]) |
10        
11         contract readLock(lockOp, unlockOp, tryLockOp) = {
12        
13           contract lockOp(ack) = {          
14             for (@([count]) <- stateRef) {
15               stateRef!([count + 1]) | ack!(Nil) } } |                          
16                
17           contract unlockOp(ack) = {
18             for (@[count /\ ~0] <- stateRef) {
19               stateRef!([count - 1]) | ack!(Nil) } } |   
20          
21           contract tryLockOp(ret) = {
22             for (@state <- stateRef) {
23               match state {
24                 "W_LOCK" => stareRef!(state) | ret!(false)                
25                 [count] => stateRef!([count + 1]) | ret!(true) } } }
26         } |
27        
28         contract writeLock(lockOp, unlockOp, tryLockOp) = {
29        
30           contract lockOp(ack) = {
31             for (@[0] <- stateRef) { 
32               stateRef!("W_LOCK") | ack!(Nil) } } |
33            
34           contract unlockOp(ack) = {
35             for (@"W_LOCK" <- stateRef) { 
36               stateRef!([0]) | ack!(Nil) } } |
37            
38           contract tryLockOp(ret) = {
39             for (@state <- stateRef) {
40               match state {
41                 [0] => stateRef!("W_LOCK") | ret!(true)
42                 ~[0] /\ other  => stateRef!(other) | ret!(false) } } }
43         }
44       }
45     }
46   }
47 }
```
</p></details><br/>   

**1-2** - ???.    
**4-6** - ???.   
**8-9** - ???.   
**11** - ???.   
**13-15** - ???.   
**17-19** - ???.   
**21-25** - ???.   
**28** - ???.   
**30-32** - ???.   
**34-36** - ???.   
**38-42** - ???.   

#### ??? Non-blocked/ignore unlock operations

- [ReentrantReadWriteLock.ReadLock.unlock()](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.ReadLock.html#unlock--) - "If the current thread does not hold this lock then IllegalMonitorStateException is thrown".
- [ReentrantReadWriteLock.WriteLock.unlock()](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.WriteLock.html#unlock--) - "If the current thread is not the holder of this lock then IllegalMonitorStateException is thrown".


<details><summary>???src</summary><p>

```
contract readLock(lockOp, unlockOp, tryLockOp) = {
  ... |
  contract unlockOp(ack) = {
    for (@state <- stateRef) {
      ack!(Nil) |
      match state {
        [count] /\ ~[0] => stateRef!([count - 1]) // UPDATE STATE
        other => stateRef!(other) }               // RESTORE STATE + IGNORE  
    } 
  } 
} |        
contract writeLock(lockOp, unlockOp, tryLockOp) = {        
  ... |
  contract unlockOp(ack) = {
    for (@state <- stateRef) { 
      ack!(Nil) |
      match state {
        "W_LOCK" => stateRef!([0])                // UDPATE STATE
        other => stateRef!(other) }               // RESTORE STATE + IGNORE
    } 
  } 
}
```
</p></details><br/>

or you can add **alert**-functionality: ```other => stateRef!(other) | stdout!("Alert!")```

#### ??? Write has highest priority over Read
???

#### ??? Add lock keys
???

### Complete source code (with demo)

<details><summary>???src</summary><p>
  
```
new ReadWriteLock in {
  contract ReadWriteLock(readLockOp, writeLockOp) = {
  
    new readLock, writeLock in {
      contract readLockOp(ret) = {ret!(readLock)} |
      contract writeLockOp(ret) = {ret!(writeLock)} |
      
      new stateRef in {      
        stateRef!([0]) |
        
        contract readLock(lockOp, unlockOp, tryLockOp) = {
        
          contract lockOp(ack) = {          
            for (@([count]) <- stateRef) {
                stateRef!([count + 1]) | ack!(Nil) } } |                          
                
          contract unlockOp(ack) = {
            for (@[count /\ ~0] <- stateRef) {
              stateRef!([count - 1]) | ack!(Nil) } } |   
          
          contract tryLockOp(ret) = {
            for (@state <- stateRef) {
              match state {
                "W_LOCK" => stareRef!(state) | ret!(false)                
                [count] => stateRef!([count + 1]) | ret!(true) } } }
        } |
        
        contract writeLock(lockOp, unlockOp, tryLockOp) = {
        
          contract lockOp(ack) = {
            for (@[0] <- stateRef) { 
              stateRef!("W_LOCK") | ack!(Nil) } } |
            
          contract unlockOp(ack) = {
            for (@"W_LOCK" <- stateRef) { 
              stateRef!([0]) | ack!(Nil) } } |
            
          contract tryLockOp(ret) = {
            for (@state <- stateRef) {
              match state {
                [0] => stateRef!("W_LOCK") | ret!(true)
                ~[0] /\ other  => stateRef!(other) | ret!(false) } } }
        }
      }
    }
  }
}
```
</p></details><br/>

### Exercise
TBD
