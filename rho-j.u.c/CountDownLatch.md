## java.util.concurrent.CountDownLatch in RhoLang

A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. 

A *CountDownLatch* is initialized with a given count. The *await()* methods block until the current count reaches zero due to invocations of the *countDown()* method, after which all waiting threads are released and any subsequent invocations of *await()* return immediately ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html)). 
  
**java.util.concurrent.CountDownLatch** (short version)   
```java
public class CountDownLatch {
  public CountDownLatch(int count) {...}  
  public void await() {...}  
  public void countDown() {...}	  
}

```  

<details><summary><b>java.util.concurrent.CountDownLatch</b> (long version)</summary><p>

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

- [State / Operations Model](#state--operations-model)
- [Implementation with Explanation](#implementation-with-explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Alternative implementations](#alternative-implementations)
  - [Sync *countDown*](#sync-countdown)
  - [Explicit *waitSet*](#explicit-waitset)
- [Exercise](#exercise)

### State / Operations Model
???

### Implementation with Explanation
```
1  new CountDownLatch in {
2    contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
3      new stateRef in {    
4    
5        stateRef!(initCount) |
6  
7        contract awaitOp(ack) = {
8          for (@0 <- stateRef) {          
9            stateRef!(0) | 
10           ack!(Nil) } } |  
11
12       contract countDownOp(_) = {
13         for (@{count /\ ~0} <- stateRef) {          
14           stateRef!(count - 1) } } 
15     }    
16   }
17 }
```
**1-3** - ???.   
**5** - ???.   
**7-10** - ???.   
**12-14** - ???.   

### Complete source code (with demo)
<details><summary>Complete source code for CountDownLatch (with demo)</summary><p>
  
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      stateRef!(initCount) |
  
      contract awaitOp(ack) = {
        for (@0 <- stateRef) {          
          stateRef!(0) | 
          ack!(Nil) } } |  

      contract countDownOp(_) = {
        for (@{count /\ ~0} <- stateRef) {          
          stateRef!(count - 1) } } 
    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(3, *await, *countDown) |
    
    new n in {
      n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | for (@i <= n) { 
        new ack in { 
          await!(*ack) | for (_ <- ack) { stdout!([i, "I woke up!"]) } } } } |     
    
    new ack in { 
      stdoutAck!("knock-knock", *ack) | for (_ <- ack) {
        countDown!(Nil) |
        stdoutAck!("KNOCK-KNOCK", *ack) | for (_ <- ack) {
          countDown!(Nil) |
          stdoutAck!("WAKE UP !!!", *ack) | for (_ <- ack) { 
            countDown!(Nil) } } } }    
  }
}
```
```
>> "knock-knock"
>> "KNOCK-KNOCK"
>> "WAKE UP !!!"
>> [4, "I woke up!"]
>> [1, "I woke up!"]
>> [0, "I woke up!"]
>> [3, "I woke up!"]
>> [2, "I woke up!"]
```
</p></details><br/>

### Alternative implementations

#### Sync *countDown*
???

#### Explicit *waitSet*
???

### Exercise

