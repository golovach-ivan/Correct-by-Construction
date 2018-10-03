## java.util.concurrent.BlockingQueue<E>

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

Empty queue (empty mark)  
```Nil``` 

Result of ```put(0); put(1); put(2); ``` is
```
    +----+   +----+
    |    v   |    v
[2, *]   [1, *]   [0, Nil]
```

Trick (block on empty): ```for (@[head, tail] <- buffer) {...}```

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

Для того, что бы добавить bounded и методы, знающие размеры (*size*/*remainingCapacity*) можно расширить формат node до 5-elem tuple
```(canTake: bool, canPut: bool, size: int, elem: any, next: 5-elem-tuple or Nil)```

```put("A"); put("B"); put("C")```

```
               +-----+              +-----+              +-----+
               |     v              |     v              |     v
(F, T, 3, "C", *)    (T, T, 2, "B", *)    (T, T, 1, "A", *)    (T, F, 0, Nil, Nil)
```

**init**  
```atomicRef!((true, false, 0, Nil, Nil))```   
```{"canTake": true, "canPut": false, "size": 0, "elem": Nil, "next": Nil)```

**put**   
```
contract put(@newElem, ack) = {
  for (@(true, a, oldSize, b, c) <- atomicRef) {
    atomicRef!(
      (oldSize + 1 < maxSize, true, oldSize + 1, newElem, 
      (true, a, oldSize, b, c))) | 
    ack!(Nil)
  }
}
```
Читаем только если первый елемент *canPut* is true  
```for ((true, _, _, _, _) <- atomicRef) {...}```  

**take**   
```
contract take(ret) = {
  for (@(_, true, _, elem, (a, b, c, d, e)) <- atomicRef) {
    atomicRef!((a, b, c, d, e)) | 
    ret!(elem)  
  }
}
```
Читаем только если второй елемент *cantake* is true  
```for ((_, true, _, _, _) <- atomicRef) {...}```  

**size**   
```
1 contract size(ret) = {
2   for (@(a, b, size, c, d) <- atomicRef) {
3     atomicRef!((a, b, size, c, d)) | 
4     ret!(size)  
5   }
6 } 
```
2 - Неблокирующе читаем *atomicRef* выбираем поле *size* и возвращаем елемент на место.  
3 - Сохраняем предыдущее состояние *atomicRef*.   
4 - Возвращаем елемент node (*size*, third elem).   

<details><summary>Сomplete source code</summary>
<p>
  
```
new LinkedBlockingQueue in {
  contract LinkedBlockingQueue(@maxSize, put, take, size, remainingCapacity) = {
    new atomicRef in {
      atomicRef!((false, true, 0, Nil, Nil)) |
      contract put(@newElem, ack) = {
        for (@(true, a, oldSize, b, c) <- atomicRef) {
          atomicRef!(
            (oldSize + 1 < maxSize, true, oldSize + 1, newElem, 
            (true, a, oldSize, b, c))) | 
          ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@(_, true, _, elem, (a, b, c, d, e)) <- atomicRef) {
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
