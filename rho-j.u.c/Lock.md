## java.util.concurrent.locks.Lock in RhoLang

A *Lock* is a tool for controlling access to a shared resource by multiple threads. Commonly, a lock provides exclusive access to a shared resource: only one thread at a time can acquire the lock and all access to the shared resource requires that the lock be acquired first ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html)). 

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

```java
public interface Lock {
  // Acquires the lock.
  void lock();

  // Releases the lock.
  void unlock();

  // Returns a new Condition instance that is bound to this Lock instance.
  Condition newCondition();

  // Acquires the lock only if it is free at the time of invocation.
  boolean tryLock();
}
```
### State / Operations Model

### Version #1: lock/unlock

```
         UNLOCKED
      +---> {*}-----+   
      |             |
unlock|             |lock
      |             |
      +-----{ } <---+   
          LOCKED
```

```
new Lock in {

  contract Lock(lock, unlock) = {
    new stateRef in {
      stateRef!(Nil) |
      
      contract lock(ack) = {
        for (_ <- stateRef) { 
          ack!(Nil) 
        }
      } |
      
      contract unlock(_) = {
        stateRef!(Nil)
      }      
    }
  } |   
    
  new lock, unlock in {
    Lock!(*lock, *unlock) |
    
    new ack in {
      lock!(*ack) | for (_ <- ack) {
        stdoutAck!("locked", *ack) | for (_ <- ack) {
          unlock!(Nil) | stdout!("unlock")
        }
      }
    }
  }    
}
```
```
>> "locked"
>> "unlock"
```

### Version #2: lock/unlock/tryLock

```
         UNLOCKED
      +---> {1}-----+   
      |             |
unlock|             |lock
      |             |
      +-----{0} <---+   
          LOCKED
```

```
new Lock in {

  contract Lock(lock, tryLock, unlock) = {
    new stateRef in {
      stateRef!(1) |
      
      contract lock(ack) = {
        for (@1 <- stateRef) { 
          stateRef!(0) | ack!(Nil) 
        }
      } |
      
      contract tryLock(ret) = {
        for (@state <- stateRef) { 
          match state {
            0 => { stateRef!(0) | ret!(false) }
            1 => { stateRef!(0) | ret!(true) }
          }          
        }
      } |      
      
      contract unlock(_) = {
        stateRef!(1)
      }      
    }
  } |   
    
 new lock, tryLock, unlock in {
    Lock!(*lock, *tryLock, *unlock) |
    
    new ack, ret in {
      lock!(*ack) | for (_ <- ack) {
        stdoutAck!("locked", *ack) | for (_ <- ack) {
          tryLock!(*ret) | for (@locked <- ret) {
            stdoutAck!(["tryLock", locked], *ack) | for (_ <- ack) {
              unlock!(Nil) | stdout!("unlock")
            }
          }                  
        }
      }
    }
  }    
}
```
```
>> "locked"
>> ["tryLock", false]
>> "unlock"
```

### Version #3: stable lock/unlock/tryLock 

#### Problem
Anybody can return any count of locks
```
unlock!(Nil) | unlock!(Nil) | unlock!(Nil)
```
```
           UNLOCKED
      +---> { Nil }-----+   
      |                 |
unlock|                 |lock
      |                 |
      +-----{[key]} <---+   
            LOCKED
```

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
