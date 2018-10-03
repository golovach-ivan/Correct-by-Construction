## java.util.concurrent.LinkedBlockingQueue<E>

An optionally-bounded blocking queue based on linked nodes. This queue orders elements FIFO (first-in-first-out). The head of the queue is that element that has been on the queue the longest time. The tail of the queue is that element that has been on the queue the shortest time. New elements are inserted at the tail of the queue, and the queue retrieval operations obtain elements at the head of the queue.

https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html

[javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/LinkedBlockingQueue.html)

```java
public class LinkedBlockingQueue<E> implements BlockingQueue<E> {
  void	clear​()	
Atomically removes all of the elements from this queue.

void	forEach​(Consumer<? super E> action)	
Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.

boolean	offer​(E e)	
Inserts the specified element at the tail of this queue if it is possible to do so immediately without exceeding the queue's capacity, returning true upon success and false if this queue is full.

int	size​()	
Returns the number of elements in this queue.
}
```

```java
BlockingQueue queue = new LinkedBlockingQueue();
queue.put(0);
queue.put(1);
queue.put(2);
queue.put(3);
```
```
Nil -> [0, Nil] -> [1, [0, Nil]] -> [2, [1, [0, Nil]]] -> [3, [2, [1, [0, Nil]]]]

Nil

[0, Nil]

    +----+
    |    v
[1, *]   [0, Nil]

    +----+   +----+
    |    v   |    v
[2, *]   [0, *]   [0, Nil]
```

```
(false, true, 0, Nil) -> (true, true, 1, Nil) -> (true, true, 2, Nil) -> (true, false, 3, Nil)
```

```
                                      (F, T, 0, Nil)
                                                ^
                                                |
                    (F, T, 0, Nil)    (T, T, 1, *)
                              ^                 ^
                              |                 |
(F, T, 0, Nil)  ->  (T, T, 1, *)  ->  (T, T, 2, *)  ->  (T, F, 3, Nil)

          +-----+
          |     v 
(T, T, 2, *)   (T, T, 1, *) -> (F, T, 0, Nil)
```

### Model #1: unbounded queue



```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(put, take) = {
    new buffer in {
      buffer!(Nil) |
      contract put(@newHead, ack) = {
        for (@oldBuf <- buffer) {
          buffer!([newHead, oldBuf]) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@[head, tail] <- buffer) {
          buffer!(tail) | ret!(head)  
        }
      } 
    }    
  }|
  
  new put, take in {    
    LinkedBlockingQueue!(*put, *take) |    
    
    // === PUT
    put!(0, Nil) |
    put!(1, Nil) |
    put!(2, Nil) |
    
    // === TAKE
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } }    
  }
}
```

Trick (block on empty): ```for (@[head, tail] <- buffer) {...}```

### Attempt 2
```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(@maxSize, put, take) = {
    new buffer in {
      buffer!((false, true, 0, [])) |
      contract put(@newHead, ack) = {
        for (@(_, true, oldSize, oldBuf) <- buffer) {
          buffer!((true, oldSize + 1 < maxSize, oldSize + 1, [newHead, oldBuf])) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@(true, _, oldSize, [head, tail]) <- buffer) {
          buffer!((oldSize - 1 > 0, true, oldSize - 1, tail)) | ret!(head)  
        }
      }       
    }    
  }|
  
  new put, take in {    
    LinkedBlockingQueue!(2, *put, *take) |    
    
    // === PUT
    new ackA in { 
      put!(0, *ackA) | for (_ <- ackA) {
        stdout!("A") | new ackB in { 
          put!(1, *ackB) | for (_ <- ackB) {
            stdout!("B") | new ackC in { 
              put!(2, *ackC) | for (_ <- ackC) {
                stdout!("C")
              } 
            }
          } 
        }
      } 
    }
    
    // === TAKE
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } } |
//    new ret in { take!(*ret) | for (@val <- ret) { stdout!(val) } }    
  }
}
```
