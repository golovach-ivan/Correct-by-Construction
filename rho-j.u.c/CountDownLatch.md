## java.util.concurrent.CountDownLatch in RhoLang

A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. 

A *CountDownLatch* is initialized with a given count. The *await()* methods block until the current count reaches zero due to invocations of the *countDown()* method, after which all waiting threads are released and any subsequent invocations of *await()* return immediately ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html)). 

<details><summary><b>java.util.concurrent.CountDownLatch.java</b></summary><p>
  
```java
public class CountDownLatch {
  // Constructs a CountDownLatch initialized with the given count.
  public CountDownLatch(int count) {...}
  
  // Causes the current thread to wait until the latch has counted down to zero.
  public void await() {...}
  
  // Decrements the count of the latch, releasing all waiting threads if the count reaches zero.
  public void countDown() {...}	
  
  // Returns the current count.
  public long getCount() {...}
}
```
</p></details><br/>

### Model

#### State Type
[Atomic state](???) with [two slots](???)      
***count: Int,***   
***waitSet: [WaitSet](???)***          
[Initialized](???) with 
***count <- [constructor-arg](???)***    
***waitSet <- [new WaitSet()](???)***        
await - [sync void(void)](???).   
countDown - [async void(void)](???).  

#### State Trace
Command order
```
CountDownLatch!(2, await, countDown);
await!(ack0);
await!(ack1);
await!(ack2);
countDown(Nil);
countDown(Nil);
```

State machine trace
```
(2, {}) -> (2, {ack0}) -> (2, {ack0, ack1}) -> (2, {ack0, ack1, ack2})
                                                          |
                                                          v
                                               (1, {ack0, ack1, ack2})
                                                          |
                                                          v
                                               (0, {ack0, ack1, ack2})
```

### Impl
```
1  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
2    new stateRef in {    
3    
4      new waitSet in {
5        stateRef!(initCount, *waitSet) } |
6  
7      contract awaitOp(ack) = {
8        for (@count, waitSet <- stateRef) {
9          stateRef!(count, *waitSet) |
10         if (count > 0) {
11           waitSet!(*ack)            
12         } else {             
13           ack!(Nil) } } } |  
14  
15     contract countDownOp(_) = {
16       for (@count, waitSet <- stateRef) {
17         stateRef!(count - 1, *waitSet) |
18         if (count - 1 == 0) {
19           for (ack <= waitSet) { ack!(Nil) } } } }                  
20   }    
21 }
```

<details><summary>public class CountDownLatch {</summary><p>
  
```
```
</p></details><br/>
