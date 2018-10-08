## java.util.concurrent.Semaphore in RhoLang

A counting semaphore. Conceptually, a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then takes it. Each release() adds a permit, potentially releasing a blocking acquirer. Semaphores are often used to restrict the number of threads than can access some (physical or logical) resource ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html)).

**java.util.concurrent.Semaphore** (short version)   
```java
public class Semaphore {
  public Semaphore(int permits) {...}  
  public void acquire() {...}  
  public void release() {...}
}
```

<details><summary><b>java.util.concurrent.Semaphore</b> (long version)</summary><p>
  
```java
public class Semaphore {
  // Creates a Semaphore with the given number of permits.
  public Semaphore(int permits) {...}
  
  // Acquires a permit from this semaphore, blocking until one is available.
  public void acquire() {...}
  
  // Acquires the given number of permits from this semaphore, blocking until all are available.
  public void acquire(int permits) {...}
  
  // Acquires a permit from this semaphore, only if one is available at the time of invocation.
  public boolean tryAcquire() {...}
  
  // Acquires and returns all permits that are immediately available, 
  // or if negative permits are available, releases them.
  public int drainPermits() {...}  
  
  // Releases a permit, returning it to the semaphore.
  public void release() {...}
  
  // Releases the given number of permits, returning them to the semaphore.
  public void release(int permits)
  
  // Returns the current number of permits available in this semaphore.
  public int availablePermits() {...}
  
  // Returns an estimate of the number of threads waiting to acquire.
  public final int getQueueLength() {...}  
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

```
  contract Semaphore(@initPermits, acquireOp, releaseOp) = {
    new stateRef in {
    
      stateRef!(initPermits, []) |    
      
      contract acquireOp(ack) = {
        for (@permits, @waitSet <- stateRef) { 
          if (permits > 0) {
            stateRef!(initPermits - 1, waitSet) |
            ack!(Nil)            
          } else {
            stateRef!(initPermits, waitSet ++ [*ack]) } } } |

      contract releaseOp(_) = {
        for (@permits, @waitSet <- stateRef) {
          match waitSet {
            [ack...waitSetTail] => { 
              stateRef!(permits, waitSetTail) |
              @ack!(Nil) }
            [] => 
              stateRef!(permits + 1, waitSet) } } } 
     } 
   } 
```

### Complete source code (with demo)

<details><summary><b>Complete source code for Semaphore (with demo)</summary><p>
```
new Semaphore in {
  contract Semaphore(@initPermits, acquireOp, acquireNOp, releaseOp) = {
    new stateRef in {
    
      stateRef!(initPermits, []) |    
      
      contract acquireOp(ack) = {
        for (@permits, @waitSet <- stateRef) { 
          if (permits > 0) {
            stateRef!(initPermits - 1, waitSet) |
            ack!(Nil)            
          } else {
            stateRef!(initPermits, waitSet ++ [*ack])
          }
        }
      } |

      contract releaseOp(_) = {
        for (@permits, @waitSet <- stateRef) {
          match waitSet {
            [ack...waitSetTail] => { 
              stateRef!(permits, waitSetTail) |
              @ack!(Nil) }
            [] => 
              stateRef!(permits + 1, waitSet)
          }
        }
      }    
    }
   } |
   
   new acquire, acquireN, release in {
     Semaphore!(3, *acquire, *acquireN, *release) |
          
     new ack0, ack1, ack in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdoutAck!("#0: I acquired 2 permits!", *ack) | for (_ <- ack) {
           stdoutAck!("#0: I have 2 out of 3 permits!", *ack) | for (_ <- ack) {
             release!(Nil) | release!(Nil)
           }         
         }                  
       }
     } |
     
     new ack0, ack1, ack in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdoutAck!("#1: I acquired 2 permits!", *ack) | for (_ <- ack) {
           stdoutAck!("#1: I have 2 out of 3 permits!", *ack) | for (_ <- ack) {
             release!(Nil) | release!(Nil)
           }         
         }                  
       }
     }      
   }
}
```
</p></details><br/>

### Exercise
[Semaphore.getQueueLength](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html#getQueueLength--)
