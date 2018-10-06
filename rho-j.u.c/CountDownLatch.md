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

#### State
[Atomic state](???) with [two slots](???) *{**count: Int**, **waitSet: [WaitSet](???)**}*.      
[Initialized](???) with *{**count <- [constructor-arg](???)**, **waitSet <- [new WaitSet()](???)**}*.   

#### Operations
await(ack) - [sync void(void)](???).   
countDown(_) - [async void(void)](???).  

#### State Trace example
Operations order
```
CountDownLatch!(2, await, countDown);
await!(ack0);
await!(ack1);
await!(ack2);
countDown(Nil);
countDown(Nil);
```

State Trace
```
count
 :  waitSet
 :  :
(2, {}) -> (2, {ack0}) -> (2, {ack0, ack1}) -> (2, {ack0, ack1, ack2})
                                                          |
                                                          v
                                               (1, {ack0, ack1, ack2})
                                                          |
                                                          v                 ack0!(Nil)
                                               (0, {ack0, ack1, ack2})  =>  ack1!(Nil)  
                                                                            ack2!(Nil)
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
**1** - .    
**2** - [atomic state](???).     
**4-5** - ???.     

**7** - await method/contract: [sync void -> void](oop.md#sync-void---void)     
**8-9** - [read-and-restore](???) atomic state
**10-11** - if gate close - [add to waitSet](???) (modify by reference)
**12-13** - else [notify](???) at the same moment     

**15** - countDown method/contract: [async void -> void](oop.md#async-void---void)     
**16-17** - [read-and-decrement](???) counter in atomic state      
**18** - if counter reach zero     
**19** - then [notifyAll](wait-set.md#notifyAll) blocked waiters in waitSet    

<details><summary>Complete source code for CountDownLatch (with demo)</summary><p>
  
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      new waitSet in {
        stateRef!(initCount, *waitSet) } |
  
      contract awaitOp(ack) = {
        for (@count, waitSet <- stateRef) {
          stateRef!(count, *waitSet) |
          if (count > 0) {
            waitSet!(*ack)            
          } else {             
            ack!(Nil) } } } |  
  
      contract countDownOp(_) = {
        for (@count, waitSet <- stateRef) {
          stateRef!(count - 1, *waitSet) |
          if (count - 1 == 0) {
            for (ack <= waitSet) { ack!(Nil) } } } }                  
    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(3, *await, *countDown) |
    
    new n in {
      n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | for (@i <= n) { 
        new ack in { 
          await!(*ack) | for (_ <- ack) { stdout!([i, "woke up!"]) } } } } |     
    
    new ack in { 
      stdoutAck!("knok", *ack) | for (_ <- ack) {
        stdoutAck!("KNOK", *ack) | for (_ <- ack) {
          stdoutAck!("WAKE UP !!!", *ack) | for (_ <- ack) {
            countDown!(Nil) |
            countDown!(Nil) |
            countDown!(Nil) } } } }    
  }
}
```
```
>> "knok"
>> "KNOK"
>> "WAKE UP !!!"
>> [2, "woke up!"]
>> [0, "woke up!"]
>> [3, "woke up!"]
>> [1, "woke up!"]
>> [4, "woke up!"]
```
</p></details><br/>

### Exercise
Implement method ```int getCount()``` for CountDownLatch.
