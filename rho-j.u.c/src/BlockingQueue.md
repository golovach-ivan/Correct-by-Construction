## java.util.concurrent.BlockingQueue\<E\> in RhoLang

A Queue that additionally supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element. BlockingQueue implementations are designed to be used primarily for producer-consumer queues ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html)). 

BlockingQueue can be optionally-bounded. BlockingQueue can orders elements FIFO (first-in-first-out) or LIFO (least-in-first-out).

**java.util.concurrent.BlockingQueue** (short version)   
```java
public interface BlockingQueue<E> {  
  void put(E e);
  E take();
}
```

<details><summary><b>java.util.concurrent.BlockingQueue</b> (long version)</summary><p>
  
```java
public interface BlockingQueue<E> {
  // Inserts the specified element into this queue, waiting if necessary for space to become available.
  void put(E e);

  // Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
  E take();

  // Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
  void forEach(Consumer<? super E> action);

  // Returns the number of elements in this queue.
  int size();
  
  // Returns the number of additional elements that this queue can accept without blocking.  
  int remainingCapacity();
  
  // Returns an array containing all of the elements in this collection.
  Object[] toArray();
  
  // Returns a sequential Stream with this collection as its source.
  Stream<E> stream();
  
  // Returns an iterator over the elements in this collection.
  Iterator<E> iterator();
}
```
</p></details><br/>

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Alternative implementations](#alternative-implementations)
  - [notFull:bool not size:int](#notfullbool-not-sizeint)
  - [Only blocking take not put](#only-blocking-take-not-put)
  - [Two explicit WaitSets](#two-explicit-waitsets)
  - [Two explicit WaitSets as Condition](#two-explicit-waitsets-as-condition)  
- [Exercises](#exercises)

### State / Operations Model
TBD

### Explanation
```
1  new BlockingQueue in {
2    contract BlockingQueue(@maxSize, putOp, takeOp, sizeOp) = {
3      new atomicRef in {
4    
5        atomicRef!([], 0) |
6      
7        contract putOp(@newElem, ack) = {
8          for (@arr, @{size /\ ~maxSize} <- atomicRef) {   // ??? 'maxSize' - free var not previously bounded
9            atomicRef!(arr ++ [newElem], size + 1) | 
10           ack!(Nil) } } |
11      
12       contract takeOp(ret) = {
13         for (@[head...tail], @size <- atomicRef) {
14           atomicRef!(tail, size - 1) | 
15           ret!(head) } } |
16      
17       contract sizeOp(ret) = {
18         for (@arr, @size <- atomicRef) {
19           atomicRef!(arr, size) | 
20           ret!(size) } } 
21     }    
22   }
23 }
```
**1-3** - ???.    
**5** - ???.    
**7-10** - ???.    
**12-15** - ???.    
**17-20** - ???.    

### Complete source code (with demo)

<details><summary>Сomplete source code</summary>
<p>
  
```
new BlockingQueue in {
  contract BlockingQueue(@maxSize, putOp, takeOp, sizeOp) = {
    new atomicRef in {
    
      atomicRef!([], 0) |
        
      contract putOp(@newElem, ack) = {
        for (@arr, @{size /\ ~maxSize} <- atomicRef) {   
          atomicRef!(arr ++ [newElem], size + 1) | 
          ack!(Nil) } } |
      
      contract takeOp(ret) = {
        for (@[head...tail], @size <- atomicRef) {
          atomicRef!(tail, size - 1) | 
          ret!(head) } } |
      
      contract sizeOp(ret) = {
        for (@arr, @size <- atomicRef) {
          atomicRef!(arr, size) | 
          ret!(size) } } 
    }    
  }|
  
  new put, take, size in {    
    BlockingQueue!(3, *put, *take, *size) |    
    
    // === put, size, take
    new ack, ret in { 
      put!(0, *ack) | for (_ <- ack) {
        put!(1, *ack) | for (_ <- ack) {
          put!(2, *ack) | for (_ <- ack) {
            size!(*ret) | for (@sz <- ret) {
              stdoutAck!(["size", sz], *ack) | for (_ <- ack) {
                take!(*ret) | for (@elem <- ret) {
                  stdoutAck!(["take", elem], *ack) | for (_ <- ack) {
                    take!(*ret) | for (@elem <- ret) {
                      stdoutAck!(["take", elem], *ack) | for (_ <- ack) {
                        take!(*ret) | for (@elem <- ret) {
                          stdoutAck!(["take", elem], *ack) | for (_ <- ack) {
                            Nil
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          } 
        }
      } 
    }     
  }
}
```
```
???
```
</p>
</details><br/>

### Alternative implementations

#### notFull:bool not size:int
```
1  new BlockingQueue in {
2    contract BlockingQueue(@maxSize, putOp, takeOp) = {
3      new atomicRef in {
4    
5        atomicRef!([], true) |
6      
7        contract putOp(@newElem, ack) = {
8          for (@arr, @true <- atomicRef) {
9            atomicRef!(arr ++ [newElem], arr.length() + 1 < maxSize) | 
10           ack!(Nil) } } |
11      
12       contract takeOp(ret) = {
13         for (@[head...tail], @notFull <- atomicRef) {
14           atomicRef!(tail, true) | 
15           ret!(head) } }
16     }    
17   }
18 }
```
**5** - ???.   
**8** - ???.   
**9** - ???.   
**10** - ???.   
**13** - ???.   
**14** - ???.   
**15** - ???.    

#### Only blocking take not put
```
1  new BlockingQueue in {
2    contract BlockingQueue(@maxSize, putOp, takeOp, sizeOp) = {
3      new stateRef in {
4    
5        stateRef!([]) |
6       
7        contract putOp(@newElem, ack) = {
8          for (@arr <- stateRef) {
9            stateRef!(arr ++ [newElem]) | 
10           ack!(Nil) } } |
11      
12       contract takeOp(ret) = {
13         for (@[head...tail] <- stateRef) {
14           stateRef!(tail) | 
15           ret!(head) } }
16     }    
17   }
18 }
```
**5** - Set initial state (empty array).    
**8** - State non-blocking read.    
**9** - Update state with new elem.    
**10** - Сonfirm the completion of the operation.
**13** - ???.    
**14** - ???.    
**15** - ???.    

#### Two explicit WaitSets
???

#### Two explicit WaitSets as Condition
???

### Exercises
**BQ.1**    
implement BoundedBuffer with state = data array + two explicit handmade WaitSets (put-waiters, take-waiter) and two methods: ```int getPutWaitersCount()``` and ```int getTakeWaitersCount()```   
**BQ.2**      
implement BoundedBuffer with Lock/Condition from [???](???) and two methods: ```int getPutWaitersCount()``` and ```int getTakeWaitersCount()```

