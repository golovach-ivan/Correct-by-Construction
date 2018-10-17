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

AtomicState = Int with one one-off implicit WaitSet.

```
await         ack    block   block   block
countDown    ignore   <<=     <<=     <<=

             +---+   +---+   +---+   +---+
             | 0 |<--| 1 |<--| 2 |<--| 3 |<--  ...
             +---+   +---+   +---+   +---+
```

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
Возможно было бы реализовать не асинхронный *countDown*
```
contract countDownOp(_) = {
  for (@{count /\ ~0} <- stateRef) {          
    stateRef!(count - 1) } }
```
а синхронный (с подтверждением)
```
contract countDownOp(ack) = {
  for (@count <- stateRef) {          
    ack!(Nil) |
    if (count == 0) {
      stateRef!(0)                       
    } else {
      stateRef!(count - 1) } } }
```
давайте посмотрим - есть ли в этом смысл?   
???

#### Explicit *waitSet*
Для реализации Java API у нас не было необходимости в explicit WaitSet, но мы можем его реализовать

```
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      stateRef!(initCount, []) |
  
      contract awaitOp(ack) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 0) {
            stateRef!(count, waitSet ++ [*ack])
          } else {             
            stateRef!(count, waitSet) |
            ack!(Nil) } } } |  
  
      contract countDownOp(_) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1, waitSet)          
          } else {
            stateRef!(0, []) |            
            new notifyAll in {            
              notifyAll!(waitSet) |
              contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) }
              } } } }
    }    
  }
}
```

<details><summary>CountDownLatch with explicit WaitSet</summary><p>
  
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      stateRef!(initCount, []) |
  
      contract awaitOp(ack) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 0) {
            stateRef!(count, waitSet ++ [*ack])
          } else {             
            stateRef!(count, waitSet) |
            ack!(Nil) } } } |  
  
      contract countDownOp(_) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1, waitSet)          
          } else {
            stateRef!(0, []) |            
            new notifyAll in {            
              notifyAll!(waitSet) |
              contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) }
              } } } }
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
</p></details><br/>
???

#### Explicit *waitSet* as Condition
???
<details><summary>CountDownLatch with explicit WaitSet as Condition</summary><p>
  
```
new CountDownLatch in {

  contract Condition(awaitOp, signalOp, signalAllOp) = { ... } |

  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef, await, signal, signalAll in {
    
      Condition!(*await,  *signal, *signalAll) |    
      stateRef!(initCount) |
  
      contract awaitOp(ack) = {
        for (@count <- stateRef) {                    
          if (count == 0) {
            stateRef!(count) | ack!(Nil)             
          } else {
            new ackCall, ackWakeUp in {
              await!(*ackCall, *ackWakeUp) | for (_ <- ackCall) {
                stateRef!(count) | 
                for (_ <- ackWakeUp) {
                  awaitOp!(*ack) } } }                            
          }
        } 
      } |  

      contract countDownOp(_) = {
        for (@count <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1)         // k  ->  k - 1
          } else {
            if (count == 1) {    
              new ack in {              
                signalAll!(*ack) | for (_ <- ack) {
                  stateRef!(count - 1)   // 1  ->  0 
                }
              }
            } else {
              stateRef!(count)           // 0  ->  0
            }     
          }          
        } 
      } 
 
    }    
  }
}
```
</p></details><br/>
???

### Exercise
???
