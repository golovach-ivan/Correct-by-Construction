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

### Version #1: base linked-list (unbounded LIFO)
Если нас интересует base (only *put* and *take* methods) unbounded LIFO queue, то мы можем реализовать простейший single-linked stack based on node as 2-elem list (\[elem, next\]) or 2-elem tuple ((elem, next)).

```
contract LinkedBlockingQueue(put, take) = {
  new atomicRef in {
    atomicRef!(Nil) |
    contract put(@newHead, ack) = {
      for (@oldBuf <- atomicRef) {
        atomicRef!([newHead, oldBuf]) | ack!(Nil)
      }
    } |
    contract take(ret) = {
      for (@[head, tail] <- atomicRef) {
        atomicRef!(tail) | ret!(head)  
      }
    } 
  }    
}
```

**init**  
```atomicRef!(Nil)```    
Init *atomicRef* with empty mark (*Nil*).

**put**
```
contract put(@newHead, ack) = {
  for (@oldBuf <- atomicRef) {
    atomicRef!([newHead, oldBuf]) | ack!(Nil)
  }
}
```
Read any value (null mark (*Nil*) too) and update *atomicRef*: ```x -> [elem, x]```, so always non-blocking.

**take**
```
contract take(ret) = {
  for (@[head, tail] <- atomicRef) {
    atomicRef!(tail) | ret!(head)  
  }
} 
```
Read only non empty (not null mark (*Nil*)) and update *atomicRef*: ```[elem, x] -> x```, so blocking on empty.

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
    new atomicRef in {
      atomicRef!(Nil) |
      contract put(@newHead, ack) = {
        for (@oldBuf <- atomicRef) {
          atomicRef!([newHead, oldBuf]) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@[head, tail] <- atomicRef) {
          atomicRef!(tail) | ret!(head)  
        }
      } 
    }    
  }|
  
  new put, take, size in {    
    LinkedBlockingQueue!(*put, *take) |    
    
    // === put(0); put(1); put(2); 
    // === stdout(get()); stdout(get()); stdout(get())
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

### Version #2: linked-list with size (bounded LIFO)

Для того, что бы добавить bounded и методы, знающие размеры (*size*/*remainingCapacity*) можно расширить формат node до Tuple4
```
(canTake, canPut, size, elem, next)
canTake - boolean, can take it?
canPut - boolean, can add one more?
size: Int
elem: Any
next: Tuple5 or Nil
```

```put("A"); put("B"); put("C"); put("D")```

```
               +-----+              +-----+              +-----+
               |     v              |     v              |     v
(T, F, 3, "D", *)    (T, T, 2, "C", *)    (T, T, 1, "B", *)    (F, T, 0, "A", Nil)
```

**init**  
```atomicRef!((false, true, 0, Nil, Nil))```  
???

**put**   
```
contract put(@newElem, ack) = {
  for (@(a, true, oldSize, b, c) <- atomicRef) {
    atomicRef!(
      (true, oldSize + 1 < maxSize, oldSize + 1, newElem, 
      (a, true, oldSize, b, c))) | 
    ack!(Nil)
  }
}
```
Читаем только если второй елемент (canPut) == true.

**take**   
```
contract take(ret) = {
  for (@(true, canPut, oldSize, elem, (a, b, c, d, e)) <- atomicRef) {
    atomicRef!((a, b, c, d, e)) | 
    ret!(elem)  
  }
}
```
Читаем только если первый елемент (cantake) == true.

**size**   
```
contract size(ret) = {
  for (@(a, b, size, c, d) <- atomicRef) {
    atomicRef!((a, b, size, c, d)) | 
    ret!(size)  
  }
} 
```
Читаем, выбираем поле *size* и возвращаем елемент на место.

<details><summary>Сomplete source code</summary>
<p>
  
```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(@maxSize, put, take, size, remainingCapacity) = {
    new atomicRef in {
      atomicRef!((false, true, 0, Nil, Nil)) |
      contract put(@newElem, ack) = {
        for (@(a, true, oldSize, b, c) <- atomicRef) {
          atomicRef!(
            (true, oldSize + 1 < maxSize, oldSize + 1, newElem, 
            (a, true, oldSize, b, c))) | 
          ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@(true, canPut, oldSize, elem, (a, b, c, d, e)) <- atomicRef) {
          atomicRef!((a, b, c, d, e)) | 
          ret!(elem)  
        }
      } |
      contract size(ret) = {
        for (@(a, b, size, c, d) <- atomicRef) {
          atomicRef!((a, b, size, c, d)) | 
          ret!(size)  
        }
      } |
      contract remainingCapacity(ret) = {
        for (@(a, b, size, c, d) <- atomicRef) {
          atomicRef!((a, b, size, c, d)) | 
          ret!(maxSize - size)  
        }
      }        
    }    
  }|
  
  new put, take, size, remainingCapacity in {    
    LinkedBlockingQueue!(10, *put, *take, *size, *remainingCapacity) |    
    
    new ack, ret in { 
      put!("A", *ack) | for (_ <- ack) {
        put!("B", *ack) | for (_ <- ack) {
          put!("C", *ack) | for (_ <- ack) {
            size!(*ret) | for (@sz <- ret) {
              stdoutAck!(["size", sz], *ack) | for (_ <- ack) {              
                remainingCapacity!(*ret) | for (@sz <- ret) {
                  stdoutAck!(["remainingCapacity", sz], *ack) | for (_ <- ack) {         
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
  }
}
```
```
>> @{["size", 3]}
>> @{["remainingCapacity", 7]}
>> @{["take", "C"]}
>> @{["take", "B"]}
>> @{["take", "A"]}
```
</p>
</details><br/>
