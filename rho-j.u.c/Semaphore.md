## Semaphore

A counting semaphore. Conceptually, a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then takes it. Each release() adds a permit, potentially releasing a blocking acquirer. Semaphores are often used to restrict the number of threads than can access some (physical or logical) resource.

In both versions structure the same
```
contract Semaphore(@initPermits, acquire, release) = {
  new permits in {
    /* init permits */ |              
    contract acquire(ack) = { /* impl */ } |      
    contract release(_) = { /* impl */ } 
  }
} 
```
acquire - sync (with ack)
release - async (without ack)

Two versions:
  - permits are elems of set, blocking on read empty set: {1,1} -> {1} -> {}
  - permits are Int or Nil, blocking on read set without Int: {2} -> {1} -> {Nil}

### Attempt 1
Model permits count as items (Nil's) in channel
```
wait set:        0           0       0      1      2      1      0       0       0      0 
permits:     {Nil, Nil} -> {Nil} -> { } -> { } -> { } -> { } -> { } -> {Nil} -> {1} -> {2} 
steps:    init------->acq----->acq--->acq--->acq--->rel--->rel--->rel----->rel--->rel
                            ^     ^      |        |
                            |     +------+        |
                            +---------------------+                                 
```

Init *permits* in loop with *initPermits* Nil elems
```
new n in {
  n!(initPermits) |
  for (@i <= n) {
    if (i > 0) { 
      permits!(Nil) | n!(i - 1) 
    }
  }
}        
```

acquire() impl
```
contract acquire(ack) = {
  for (_ <- permits) { ack!(Nil) }
}
```

release() impl
```
contract release(_) = {
  permits!(Nil)
} 
```

<details><summary>Сomplete source code</summary>
<p>
  
```
new Semaphore in {
  contract Semaphore(@initPermits, acquire, release) = {
    new permits in {
      new n in {
        n!(initPermits) |
        for (@i <= n) {
          if (i > 0) {
            permits!(Nil) | n!(i - 1)
          }
        }
      } |        
      
      contract acquire(ack) = {
        for (_ <- permits) { acquire!(Nil) }
      } |
      contract release(_) = {
        permits!(Nil)
      } 
    }
   } |
   
   new acquire, release in {
     Semaphore!(3, *acquire, *release) |
     
     new ack0, ack1 in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdout!("I acquire 2 permits (A)!") | release!(Nil) | release!(Nil)
       }
     } |
     
     new ack0, ack1 in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdout!("I acquire 2 permits (B)!") | release!(Nil) | release!(Nil)
       }
     }     
   }
}
```
</p>
</details><br/>

### Attempt 2
Model permits count as int in channel

```
wait set:       0      0       0        1        2        1        0       0      0   
permits:       {2} -> {1} -> {Nil} -> {Nil} -> {Nil} -> {Nil} -> {Nil} -> {1} -> {2} 
steps:    init-->acq--->acq- --->acq----->acq----->rel----->rel----->rel--->rel
                                 ^        ^         |        |
                                 |        +---------+        |
                                 +---------------------------+                                 
```

Trivial init
```
permits!(initPermits
```

acquire() impl
```
      contract acquire(ack) = {
        for (k /\ Int <- permits) { 
          ack!(Nil) |
          if (k == 1) { permits!(Nil) }
          else { permits!(k - 1) }
        }
      } 
```

release() impl
```
      contract release(_) = {
        for (@p <- permits) {
          if (p == Nil) { permits!(1) }
          else { permits!(p + 1) }
        }
      }
```

BUT other methods trival too

Returns the current number of permits available in this semaphore
```
      contract availablePermits(ret) = {
        for (@p <- permits) {
          if (p == Nil) { ret!(0) }
          else { ret!(p) } |
          permits!(p)
        }
      } 
```      

Acquires and returns all permits that are immediately available, or if negative permits are available, releases them.
```
      contract drainPermits(ret) = {
        for (@p <- permits) {
          if (p == Nil) { ret!(0) }
          else { ret!(p) } |
          permits!(Nil)
        }
      }
```

Acquires the given number of permits from this semaphore, blocking until all are available
```
contract acquire(p /\ Int, ack) = {
  if (p == 1) {
    acquire!(*ack)
  } else {
    new ackL, ackR in {
      acquire!(*ackL) | acquire!(p - 1, *ackR) | 
      for (_ <- aclL; _ <- ackR) {
        ack!(Nil)
      }                      
    }
  }            
}
```

<details><summary>Сomplete source code</summary>
<p>
  
```
new Semaphore in {
  contract Semaphore(@initPermits, acquire, release) = {
    new permits in {
      permits!(initPermits) |        
      
      contract acquire(ack) = {
        for (p /\ Int <- permits) { 
          ack!(Nil) |
          if (p == 1) { permits!(Nil) }
          else { permits!(p - 1) }
        }
      } |
      
      contract release(_) = {
        for (@p <- permits) {
          if (p == Nil) { permits!(1) }
          else { permits!(p + 1) }
        }
      } 
    }
   } |
   
   new acquire, release in {
     Semaphore!(3, *acquire, *release) |
     
     new ack0, ack1 in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdout!("I acquire 2 permits (A)!") | release!(Nil) | release!(Nil)
       }
     } |
     
     new ack0, ack1 in {
       acquire!(*ack0) | acquire!(*ack1) | for (_ <- ack0; _ <- ack1) {
         stdout!("I acquire 2 permits (B)!") | release!(Nil) | release!(Nil)
       }
     }     
   }
}
```
</p>
</details><br/>
