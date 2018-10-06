## java.util.concurrent.CountDownLatch in RhoLang

A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. 

A *CountDownLatch* is initialized with a given count. The *await()* methods block until the current count reaches zero due to invocations of the *countDown()* method, after which all waiting threads are released and any subsequent invocations of *await()* return immediately ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html)). 

- [Model](#model)
- [Impl](#impl)
- [Complete source code (with demo)](#complete-source-code-with-demo)  
- [Exercise](#exercise)

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

CountDownLatch сделан как [One-off WaitSet](wait-set.md#one-off-waitset) с присоедененным counter.   
await() - блокирует/помещает в waitSet, countDown() - декрементирует counter, когда тот достигает 0 - notifyAll().

#### State
[Atomic state](atomic-state.md) with [two slots](atomic-state.md#multislot-state) *{**count: Int**, **waitSet: [WaitSet](wait-set.md)**}*.      
[Initialized](oop.md#initialization) with *{**count <- [constructor-arg](oop.md#initialization)**, **waitSet <- [new WaitSet()](???)**}*.   
#### State update operations
Каждое обращение к await - увеличивает waitSet на один елемент 
Каждое обращение к countDown - уменьшает counter на еденицу   

#### Model Trace example
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
[Sceleton](oop.md#contract--object) modification   
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
**1** - [contract/object](oop.md#contract--object) with [constructor arg](oop.md#initialization) *initCount*    
**2** - [atomic state](atomic-state.md) ([multifield](atomic-state.md#multislot-state) = Int × [WaitSet](wait-set.md))     
**4-5** - atomic state [initialization](atomic-state.md#initialization) with pair constructor-arg × ([new WaitSet](wait-set.md#initialization))   
**7** - *await* [contract/method](oop.md#contract--method): [sync void -> void](oop.md#sync-void---void)     
**8-9** - [read-and-restore](atomic-state.md#restore-state) atomic state  
**10-11** - if gate closed - [add to waitSet](wait-set.md#wait) (modify waitSet in restored state by reference)  
**12-13** - else [notify](wait-set.md#notify) at the same moment     
**15** - *countDown* [contract/method](oop.md#contract--method): [async void -> void](oop.md#async-void---void)     
**16-17** - [Non-blocked read-and-decrement](atomic-state.md#non-blocked-update) *counter* in atomic state      
**18** - if *counter* reach zero     
**19** - then [notifyall](wait-set.md#notifyAll) blocked waiters in *waitSet*    

### Complete source code (with demo)
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
Implement method [int getCount()](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html#countDown--) for CountDownLatch in RhoLang.
