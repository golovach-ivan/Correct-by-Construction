## java.util.concurrent.locks.Lock
A [Lock](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html) is a tool for controlling access to a shared resource by multiple threads. Commonly, a lock provides exclusive access to a shared resource: only one thread at a time can acquire the lock and all access to the shared resource requires that the lock be acquired first. 



```
new Cell in {

  contract Cell(@init, get, set) = {  
    new State in {    
      State!(init) |
      contract get(ret) = {
        for (@value <- State) {      
          State!(value) | ret!(value)
        }
      } |
      contract set(@newValue, ack) = {
        for (_ <- State) {      
          State!(newValue) | ack!(Nil)
        }
      }      
    }
  } |
  
  new get, set, ack in {
    Cell!(10, *get, *set) | 
    new ret in {
      get!(*ret) | for (@val <- ret) {set!(val + 1, *ack)}
    } |
    new ret in {
      get!(*ret) | for (@val <- ret) {set!(val * 2, *ack)}
    } |
    for (_ <- ack) {
      for (_ <- ack) {      
        new ret in {        
          get!(*ret) | for (@val <- ret) { stdout!(val) }
        }
      }
    }
  }
}
```

### Attempt 1
```
new Cell, lock in {

  contract Cell(@init, get, set) = {...} |
    
  lock!(Nil) |  
    
  new get, set, ack, x, y in {
    Cell!(10, *get, *set) | 
    
    for (_ <- lock) {
      new ret in {
        get!(*ret) | for (@val <- ret) {
          set!(val + 1, *ack) | for (_ <- ack) {lock!(Nil) | x!(Nil)}
        }
      } 
    }|
    
    for (_ <- lock) {
      new ret in {
        get!(*ret) | for (@val <- ret) {
          set!(val * 2, *ack) | for (_ <- ack) {lock!(Nil) | y!(Nil)}
        }
      } 
    }|
    
    new ret in {        
      for (_ <- x; _ <- y) {
        get!(*ret) | for (@val <- ret) { stdout!(val) }
      }
    }
    
  }    
}
```

#### Problem
Anybody can return any count of locks
```
lock!(Nil) | lock!(Nil) | lock!(Nil)
```

### Attempt 2
```
new Cell, Lock in {

  contract Cell(@init, get, set) = {...} |
    
  contract Lock(lock, unlock) = {
    new State, Key in {
      State!(Nil) |
      contract lock(ret) = {
        for (_ <- State) {
          new lockKey in {
            Key!(*lockKey) | ret!(*lockKey)  
          }
        }
      } |
      contract unlock(@lockKey) = {
        for (@savedLockKey <- Key) {
          match lockKey == savedLockKey {
            true => State!(Nil)
            false => Key!(savedLockKey)
          }
        }
      }      
    }
  } |   
    
 new get, set, lock, unlock, x, y in {
    Cell!(10, *get, *set) | 
    Lock!(*lock, *unlock) |
    
    new retVal, retLock, ack in {
      lock!(*retLock) | for (@lockKey <- retLock) {
        get!(*retVal) | for (@val <- retVal) {
          set!(val + 1, *ack) | for (_ <- ack) {unlock!(lockKey) | x!(Nil)}
        }        
      }
    } |
        
    new retVal, retLock, ack in {
      lock!(*retLock) | for (@lockKey <- retLock) {
        get!(*retVal) | for (@val <- retVal) {
          set!(val * 2, *ack) | for (_ <- ack) {unlock!(lockKey) | y!(Nil)}
        }        
      }
    } |
    
    new ret in {        
      for (_ <- x; _ <- y) {
        get!(*ret) | for (@val <- ret) { stdout!(val) }
      }
    }    
    
  }    
}
```
