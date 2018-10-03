## java.util.concurrent.LinkedBlockingQueue<E>

A Queue that additionally supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element. BlockingQueue implementations are designed to be used primarily for producer-consumer queues ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html)). 

BlockingQueue can be optionally-bounded. BlockingQueue can orders elements FIFO (first-in-first-out) or LIFO (least-in-first-out).

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
}
```

### Model #1: linked-list (unbounded LIFO)
Если нас интересует base (only *put* and *take* methods) unbounded LIFO queue, то мы можем реализовать простейший single-linked stack based on node as 2-elem list (\[elem, next\]) or 2-elem tuple ((elem, next)).

```
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
}
```

So result of ```put(0); put(1); put(2); ``` is
```
    +----+   +----+
    |    v   |    v
[2, *]   [1, *]   [0, Nil]
```

Trick (block on empty): ```for (@[head, tail] <- buffer) {...}```

<details><summary>Сomplete source code</summary>
<p>
  
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
  
  new put, take, size in {    
    LinkedBlockingQueue!(*put, *take) |    
    
    // === put, size, take
    new ack, ret in { 
      put!(0, *ack) | for (_ <- ack) {
        put!(1, *ack) | for (_ <- ack) {
          put!(2, *ack) | for (_ <- ack) {
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
```
```
>> @{["take", 2]}
>> @{["take", 1]}
>> @{["take", 0]}
```
</p>
</details><br/>

### Attempt 2

```
          +-----+
          |     v 
(T, T, 2, *)   (T, T, 1, *) -> (F, T, 0, Nil)
```

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
