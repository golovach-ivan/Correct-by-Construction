## Semaphore

Semaphore is a classic concurrency tool.

A counting semaphore. Conceptually, a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then takes it. Each release() adds a permit, potentially releasing a blocking acquirer. 

Semaphores are often used to restrict the number of threads than can access some (physical or logical) resource.

In both versions structure the same
```
 contract Semaphore(@initPermits, acquire, release) = {
    new permits in {
      /* init permits */ |        
      
      contract acquire(ack) = { /* impl */ } |
      
      contract release(_) = { /* impl */ } 
    }
   } |
```
acquire - sync (with ack)
release - async (without ack)

### Attempt 1
Model permits count as items (Nil's) in channel
```
- {Nil, Nil}  // init with 2 permits
- {Nil}       // acquire()
- {}          // acquire() - empty, any new acquire() block on read
- {Nil}       // release()
- {Nil, Nil}  // release()
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
