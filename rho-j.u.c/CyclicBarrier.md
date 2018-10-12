## java.util.concurrent.CyclicBarrier in RhoLang

A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point. CyclicBarriers are useful in programs involving a fixed sized party of threads that must occasionally wait for each other. The barrier is called cyclic because it can be re-used after the waiting threads are released.

A CyclicBarrier supports an optional Runnable command that is run once per barrier point, after the last thread in the party arrives, but before any threads are released. This barrier action is useful for updating shared-state before any of the parties continue ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CyclicBarrier.html)).

**java.util.concurrent.CyclicBarrier** (short version)   
```java
public class CyclicBarrier {
  CyclicBarrier(int parties) {...}
  int await() {...}
}
```

<details><summary><b>java.util.concurrent.CyclicBarrier</b> (long version)</summary><p>
  
```java
public class CyclicBarrier {
  // A new CyclicBarrier that will trip when the given number of parties are waiting upon it.
  CyclicBarrier(int parties) {...}

  // Waits until all parties have invoked await on this barrier.
  // Returns: the arrival index of the current thread, where index getParties() - 1 
  // indicates the first to arrive and zero indicates the last to arrive
  int await() {...}

  // Returns the number of parties currently waiting at the barrier.
  int getNumberWaiting() {...}

  // Returns the number of parties required to trip this barrier.
  int getParties() {...}
}
```
</p></details><br/>

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

### State / Operations Model
TBD

### Explanation
```
1  new CyclicBarrier in {
2    contract CyclicBarrier(@initCount, @action, ack, awaitOp) = {  
3      new stateRef in {    
4     
5        stateRef!(initCount, []) |
6  
7        contract awaitOp(ret) = {
8          for (@count, @waitSet <- stateRef) {          
9            if (count > 1) {
10             stateRef!(count - 1, waitSet ++ [(*ret, count - 1)])
11           } else {             
12             stateRef!(initCount, []) |
13             action | for (_ <- ack) {
14               ret!(0) |
15               new notifyAll in {                          
16                 notifyAll!(waitSet) |
17                 contract notifyAll(@[(ret, index)...tail]) = { 
18                   @ret!(index) | 
19                   notifyAll!(tail) 
20                 } } } } } 
21       } 
22     }    
23   }
24 }
```

### Complete source code (with demo)
<details><summary><b>CyclicBarrier in Rholang (with demo)</b> (long version)</summary><p>
  
```
new CyclicBarrier in {
  contract CyclicBarrier(@initCount, @action, ack, awaitOp) = {  
    new stateRef in {    
    
      stateRef!(initCount, []) |
  
      contract awaitOp(ret) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1, waitSet ++ [(*ret, count - 1)])
          } else {             
            stateRef!(initCount, []) |
            action | for (_ <- ack) {
              ret!(0) |
              new notifyAll in {                          
                notifyAll!(waitSet) |
                contract notifyAll(@[(ret, index)...tail]) = { 
                  @ret!(index) | 
                  notifyAll!(tail) 
                } } } } } 
      } 
    }    
  } |
  
  new await in {
    new ack in {
      CyclicBarrier!(3, stdoutAck!("---", *ack), *ack, *await)
    } |
    
    new threadId in {
      threadId!(0) | threadId!(1) | threadId!(2) |
      for (@tId <= threadId) {
        new stageId in {
          stageId!([0, 1, 2, 3, 4]) | for (@[sId...tail] <= stageId) { 
            new ret, ack in {
              await!(*ret) | for (@index <- ret) {
                stdoutAck!("thread #${t}, stage = ${s}, index = ${index}" %% {"t":tId, "s" : sId, "index": index}, *ack) | 
                for (_ <- ack) { stageId!(tail) } } } } } } }   
  }
```
```
>> ---
>> thread #0, stage = 0, index = 1
>> thread #2, stage = 0, index = 2
>> thread #1, stage = 0, index = 0
>> ---
>> thread #1, stage = 1, index = 1
>> thread #0, stage = 1, index = 2
>> thread #2, stage = 1, index = 0
>> ---
>> thread #2, stage = 2, index = 1
>> thread #0, stage = 2, index = 0
>> thread #1, stage = 2, index = 2
>> ---
>> thread #1, stage = 3, index = 2
>> thread #2, stage = 3, index = 1
>> thread #0, stage = 3, index = 0
>> ---
>> thread #1, stage = 4, index = 1
>> thread #2, stage = 4, index = 2
>> thread #0, stage = 4, index = 0
```
</p></details><br/>

### Exercise
TBD
