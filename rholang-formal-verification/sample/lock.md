## Lock primitive

#### Lock is channel

```
new lock in {
  lock!(Nil) |           // init lock
  for (_ <- lock) {      // lock
    // critical section
    lock!(Nil)           // unlock
  } 
}
```

#### Lock is channels pair: lock/unlock, discipline R+W
```
contract Lock(lock, unlock) = {
  // ...
} |

new lock, unlock in {
  Lock(*lock, *unlock) |
  for (_ <- lock) {
    // critical section
    unlock!(Nil)
  }
}
```

#### Lock is channels pair: lock/unlock, discipline WR+W
```
contract Lock(lock, unlock) = {
  // ...
} |

new lock, unlock in {
  Lock(*lock, *unlock) |
  new get in {
    lock!(*get) | for (@key <- get) {
      // critical section
      unlock!(key)
    }
  }
}
```
