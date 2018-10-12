## java.util.concurrent.BlockingQueue\<E\> in RhoLang

A Queue that additionally supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element. BlockingQueue implementations are designed to be used primarily for producer-consumer queues ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html)). 

BlockingQueue can be optionally-bounded. BlockingQueue can orders elements FIFO (first-in-first-out) or LIFO (least-in-first-out).

TDD
- [Version #1: base linked-list (unbounded LIFO)](#version-1-base-linked-list-unbounded-lifo)
- [Version #2: linked-list with size (bounded LIFO)](#version-2-linked-list-with-size-bounded-lifo)
- [Version #3: bounded LIFO/FIFO with backed array](#version-3-bounded-lifofifo-with-backed-array)

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
- [Exercise](#exercise)

### State / Operations Model
TBD

### Explanation
TBD


### Complete source code (with demo)

<details><summary>Сomplete source code</summary>
<p>
  
```
???
```
```
???
```
</p>
</details><br/>

### Exercise
- add two explicit WaitSets (put-waiters, take-waiter) and two methods: ```int getPutWaitersCount()``` and ```int getTakeWaitersCount()```
- implement BoundedBuffer with Lock/Condition from [???](???) and two methods: ```int getPutWaitersCount()``` and ```int getTakeWaitersCount()```


```
new BlockingQueue in {
  contract BlockingQueue(@maxSize, putOp, takeOp, sizeOp) = {
    new atomicRef in {
    
      atomicRef!([], true) |
      
      contract putOp(@newElem, ack) = {
        for (@arr, @true <- atomicRef) {
          atomicRef!(arr ++ [newElem], arr.length() + 1 < maxSize) | 
          ack!(Nil) } } |
      
      contract takeOp(ret) = {
        for (@[head...tail], @notFull <- atomicRef) {
          atomicRef!(tail, notFull) | 
          ret!(head) } } |
      
      contract sizeOp(ret) = {
        for (@arr, @notFull <- atomicRef) {
          atomicRef!(arr, notFull) | 
          ret!(arr.length()) } } 
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
