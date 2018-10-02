A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. 

A *CountDownLatch* is initialized with a given count. The *await()* methods block until the current count reaches zero due to invocations of the *countDown()* method, after which all waiting threads are released and any subsequent invocations of *await()* return immediately ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html)). 

```java
public class CountDownLatch {
  // Constructs a CountDownLatch initialized with the given count.
  public CountDownLatch(int count) {...}
  
  // Causes the current thread to wait until the latch has counted down to zero.
  public void await() {...}
  
  // Decrements the count of the latch, releasing all waiting threads if the count reaches zero.
  public void countDown();	
  
  // Returns the current count.
  public long getCount() {...}
}
```

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
