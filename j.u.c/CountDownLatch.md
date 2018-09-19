```
new CountDownLatch in {
  contract CountDownLatch(@initSize, countDown, await) = {
    new count, awaitQueue, wakeUp in {    
      count!(initSize) |
      awaitQueue!([]) |
      
      contract wakeUp(_) = {
        for (@[head, tail] <- awaitQueue) {
          @head!(Nil) | awaitQueue!(tail) | wakeUp!(Nil)
        }
      } |
      
      contract countDown(_) = {
        for (@x <- count) {
          count!(x - 1) |
          if (x - 1 == 0) {
            wakeUp!(Nil) | count!(0)  
          }
        }
      } |
      
      contract await(newAck) = {
        for (@oldQueue <- awaitQueue) {
          awaitQueue!([*newAck, oldQueue])
        }
      }       
    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(2, *countDown, *await) |
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(0) } } |
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(1) } } |
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(2) } } |
    
//    countDown!(Nil) |
//    countDown!(Nil) |
//    countDown!(Nil) |
    countDown!(Nil)
  }
}
```
