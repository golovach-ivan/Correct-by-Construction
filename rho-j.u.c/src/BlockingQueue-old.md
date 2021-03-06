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
  }
}
```

### Complete source code (with demo)
TBD





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
```
1  atomicRef!(Nil)
```    
1 - Init with empty mark (*Nil*).  

**put**
```
1  contract put(@newElem, ack) = {
2    for (@oldNode <- atomicRef) {
3      atomicRef!([newElem, oldNode]) | 
4      ack!(Nil)
5    }
6  }
```
2 - Non-blocking read anything.  
3 - Insert new node, update *atomicRef*: ```x -> [newElem, x]```.
4 - Возвращаем подтверждение вставки нового елемента.  

**take**
```
1  contract take(ret) = {
2    for (@[elem, next] <- atomicRef) {
3      atomicRef!(next) | 
4      ret!(elem)  
5    }
6  } 
```
2 - Read only non empty (not null mark (*Nil*)).  
3 - Remove top node, update *atomicRef*: ```[elem, next] -> next```, so blocking on empty.  
4 - Возвращаем клиенту *elem*.   

<details><summary>Сomplete source code</summary>
<p>
  
```
new BlockingQueue in {
  contract BlockingQueue(put, take) = {
    new atomicRef in {
      atomicRef!(Nil) |
      contract put(@newElem, ack) = {
        for (@oldNode <- atomicRef) {
          atomicRef!([newElem, oldNode]) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@[elem, next] <- atomicRef) {
          atomicRef!(next) | ret!(elem)  
        }
      } 
    }    
  }|
  
  new put, take, size in {    
    BlockingQueue!(*put, *take) |    
    
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
```
1  atomicRef!((true, false, 0, Nil, Nil))
```   
1 - Устанавливаем елемент-обелиск: ```{"canTake": true, "canPut": false, "size": 0, "elem": Nil, "next": Nil)```

**put**   
```
1  contract put(@newElem, ack) = {
2    for (@(true, a, oldSize, b, c) <- atomicRef) {
3      atomicRef!(
4        (oldSize + 1 < maxSize, true, oldSize + 1, newElem, 
5        (true, a, oldSize, b, c))) | 
6      ack!(Nil)
7    }
8  }
```
2 - Блокирующе читаем только если первый елемент *canPut* is true (queue is not full).   
3-5 - Восстанавливаем состояние *atomicRef*, сохраняем новый node со ссылкой на старый node. Высчитываем новый размер ```oldSize + 1``` и новое значение флага *canPut* = ```oldSize + 1 < maxSize```.   
6 - Подтверждаем клиенту вставку нового елемента.       

**take**   
```
1  contract take(ret) = {
2    for (@(_, true, _, elem, (a, b, c, d, e)) <- atomicRef) {
3      atomicRef!((a, b, c, d, e)) | 
4      ret!(elem)  
5    }
6  }
```
2 - Блокирующе читаем только если второй елемент *canTake* is true (queue is not empty).   
3 - Восстанавливаем состояние *atomicRef*, сохраняем не вычитанный елемент, а предыдущий.   
4 - Возвращаем клиенту елемент node (*elem*, forth elem).   

**size**   
```
1  contract size(ret) = {
2    for (@(a, b, size, c, d) <- atomicRef) {
3      atomicRef!((a, b, size, c, d)) | 
4      ret!(size)  
5    }
6  } 
```
2 - Неблокирующе читаем *atomicRef* выбираем поле *size* и возвращаем node на место.  
3 - Восстанавливаем состояние *atomicRef*, возвращаем прочитанное.   
4 - Возвращаем клиенту елемент node (*size*, third elem).   

<details><summary>Сomplete source code</summary>
<p>
  
```
new BlockingQueue in {
  contract BlockingQueue(@maxSize, put, take, size, remainingCapacity) = {
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
    BlockingQueue!(10, *put, *take, *size, *remainingCapacity) |    
    
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

### Version #3: bounded LIFO/FIFO with backed array

Best variant - atomic ref with array.  
AtomicRef\<Array\<Any\>, []\>  
AtomicRef\<Int, Nil\>  

```
[] -> [0] -> [0, 1] -> [0, 1, 2] -> [1, 2] -> [2] -> []
```

```
put/take/size: Array => Array

put:  arr -> arr ++ [newElem]
take: [elem...next] -> next
size: arr -> arr

put:  arr -> Nil
take: [elem...next] -> elem
size: arr -> Int

put =  AtomicRef<Array<Any>, []>(arr -> arr ++ [newElem], arr -> Nil)  
take = AtomicRef<Array<Any>, []>([elem...next] -> next,   [elem...next] -> elem)  
size = AtomicRef<Array<Any>, []>(arr -> arr,              arr -> arr.length())  
```

Symmetric impls
```
contract BlockingQueue(@maxSize, put, take, size) = {
  new atomicRef in {
    atomicRef!([]) |
    
    contract put(@newElem, ack) = {
      for (@arr <- atomicRef) {
        atomicRef!(arr ++ [newElem]) | ack!(Nil)
      }
    } |
    
    contract take(ret) = {
      for (@[head...tail] <- atomicRef) {
        atomicRef!(tail) | ret!(head)
      }
    } |
    
    contract size(ret) = {
      for (@arr <- atomicRef) {
        atomicRef!(arr) | ret!(arr.length())
      }
    } 
  }    
}
```

**init**  
```
1  atomicRef!([])
```
1 - Init atomicRef with empty mark (\[\]).  

**put**  
```
1  contract put(@newElem, ack) = {
2    for (@arr <- atomicRef) {
3      atomicRef!(arr ++ [newElem]) | 
4      ack!(Nil)
5    }
6  }
```
2 - Unblocked read.   
3 - Restore/update: arr -> arr ++ \[newElem]\  
4 - Подтверждаем клиенту вставку нового елемента.   

**take**  
```
1  contract take(ret) = {
2    for (@[head...tail] <- atomicRef) {
3      atomicRef!(tail) | 
4      ret!(head)
5    }
6  }
```
2 - Blocked read, wait on non-empty array.   
3 - Restore/update: \[head..tail\] -> tail  
4 - Return: \[head..tail\] -> head  

**size**  
```
1  contract size(ret) = {
2    for (@arr <- atomicRef) {
3      atomicRef!(arr) | 
4      ret!(arr.length())
5    }
6  } 
```
2 - Unblocked read.   
3 - Restore/save state.   
4 - Return arr.length.   

<details><summary>Сomplete source code</summary>
<p>
  
```
new BlockingQueue in {
  contract BlockingQueue(@maxSize, put, take, size) = {
    new atomicRef in {
      atomicRef!([]) |
      contract put(@newElem, ack) = {
        for (@arr <- atomicRef) {
          atomicRef!(arr ++ [newElem]) | ack!(Nil)
        }
      } |
      contract take(ret) = {
        for (@[head...tail] <- atomicRef) {
          atomicRef!(tail) | ret!(head)
        }
      } |
      contract size(ret) = {
        for (@arr <- atomicRef) {
          atomicRef!(arr) | ret!(arr.length())
        }
      } 
    }    
  }|
  
  new put, take, size in {    
    BlockingQueue!(2, *put, *take, *size) |    
    
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
>> @{["size", 3]}
>> @{["take", 0]}
>> @{["take", 1]}
>> @{["take", 2]}
```
</p>
</details><br/>

### Exercise
TBD
