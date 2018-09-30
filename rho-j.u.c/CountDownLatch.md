```
new CountDownLatch in {
  contract CountDownLatch(@initSize, countDown, await) = {
    new countRef, awaitQueueRef in {    
      countRef!(initSize) |
      awaitQueueRef!(Nil) |
  
      contract await(ack) = {
        for (@count <- countRef) {
          countRef!(count) |
          if (count > 0) {
            for (@oldAwaitQueue <- awaitQueueRef) {
              awaitQueueRef!((*ack, oldAwaitQueue))
            }          
          } else { ack!(Nil) }
        }
      } |  
  
      contract countDown(_) = {
        for (@count <- countRef) {
          if (count == 0) {
            countRef!(0)
          } else if (count == 1) {
              countRef!(0) |
              new wakeUp in {
                for (@awaitQueue <- awaitQueueRef) { wakeUp!(awaitQueue) } |
                contract wakeUp(list) = {
                  match *list { (ack, next) => { @ack!(Nil) | wakeUp!(next) } }
                }            
              }          
          } else {
            countRef!(count - 1)
          }        
        }
      }                  
    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(2, *countDown, *await) |
    
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(0) } } |
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(1) } } |
    new ack in { await!(*ack) | for (_ <- ack) { stdout!(2) } } |
    
    countDown!(Nil) |
    countDown!(Nil)
  }
}
```
