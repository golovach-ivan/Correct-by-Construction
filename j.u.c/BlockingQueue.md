```
new BlockingQueue in {
  contract BlockingQueue(put, take) = {
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
    BlockingQueue!(*put, *take) |    
    
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
