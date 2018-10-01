### Attempt 1

```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(put, take) = {
    new buffer in {
      buffer!([]) |
      contract put(@newHead, ack) = {
        for (@oldBuf <- buffer) {
          buffer!([newHead, oldBuf]) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@[head, tail] <- buffer) {
          buffer!(tail) | ret!(head)  
        }
      } 
    }    
  }|
  
  new put, take in {    
    LinkedBlockingQueue!(*put, *take) |    
    
    // === PUT
    new ack in { put!(0, *ack) } |
    new ack in { put!(1, *ack) } |
    new ack in { put!(2, *ack) } |    
    
    // === TAKE
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } }    
  }
}
```

Trick (block on empty): ```for (@[head, tail] <- buffer) {...}```

### Attempt 2
```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(@maxSize, put, take) = {
    new buffer in {
      buffer!((false, true, 0, [])) |
      contract put(@newHead, ack) = {
        for (@(_, true, oldSize, oldBuf) <- buffer) {
          buffer!((true, oldSize + 1 < maxSize, oldSize + 1, [newHead, oldBuf])) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@(true, _, oldSize, [head, tail]) <- buffer) {
          buffer!((oldSize - 1 > 0, true, oldSize - 1, tail)) | ret!(head)  
        }
      }       
    }    
  }|
  
  new put, take in {    
    LinkedBlockingQueue!(2, *put, *take) |    
    
    // === PUT
    new ackA in { 
      put!(0, *ackA) | for (_ <- ackA) {
        stdout!("A") | new ackB in { 
          put!(1, *ackB) | for (_ <- ackB) {
            stdout!("B") | new ackC in { 
              put!(2, *ackC) | for (_ <- ackC) {
                stdout!("C")
              } 
            }
          } 
        }
      } 
    }
    
    // === TAKE
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } }    
  }
}
```