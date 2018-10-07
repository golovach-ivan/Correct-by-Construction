## Atomic State pattern

- [Concurrency Primitive as FSM](#concurrency-primitive-as-fsm)
- [Multislot State](#multislot-state)  
- [Restore state](atomic-state.md#restore-state)  
- [Non-blocked update](#non-blocked-update)
- [Blocked (conditional) update](#blocked-conditional-update)  
- [Example](#example)  
- [Join State and Operations Channels](#join-state-and-operations-channels)  
- [Join equi-transformations](#join-equi-transformations)  

### Concurrency Primitive as FSM

Шаблон Atomic State требует 
1. хранения всех полей (компонент состояния) в виде одной записи в единственном канале;   
2. вне рамок исполнения методов в этом канале всегда лежит ровно одна запись;   
3. исполнение каждого метода заключается в чтении состояния, потом - восстановлении того же или обновленного.

Что это значит?   
- не использовать каналы-поля как коллекции (но в них можно хранить tuples, arrays or channels-collections)    
- не допускать пустого поля вне рамок метода

Во многих случаях можно реализовать как атомарно обновляемое значение (канал, в котором не более одного значения)
```
contract MyPrimitive(@initState, ...) = {
  new stateRef in {
    stateRef!(initState) |
    ...  
  }
}
```

Многие операции concurrency primitive можно представить как atomic update over state  
state × arg → state × ret    
state × arg → F(state, arg) × G(state, arg)     
```
contract op(@arg, ret) = {
  for (@state <- stateRef) {
    stateRef!(F(state, arg)) | ret!(G(state, arg))
  }
}  
```

#### tryLock
- Lock.[tryLock](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html#tryLock--)
- Semaphore.[tryAcquire](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html#tryAcquire--)
- BlockingQueue.[remove](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html#remove-java.lang.Object-)/[poll](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html#poll-long-java.util.concurrent.TimeUnit-) имеют неблокирующую семантику, [take](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/BlockingQueue.html#take--) - блокирующую.

#### Explicit WaitSet
size, isEmpty   
- ReentrantLock.[getQueueLength](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReentrantLock.html#getQueueLength--)
notifyAll       
size, isEmpty + notifyAll    

### Multislot State

RhoLang is a **polyadic π-calculi** extension so multivalued channel for free    
Read:```for (@slot0, @slot1, ... @slotN <- stateRef) {...}```   
Write:```stateRef!(slot0, slot1, ... slotN)```     
```
contract C(@initSlot0, @initSlot1, fooOp, ...) = {
  new stateRef in {
    stateRef!(initSlot0, initSlot1) |
    
    contract fooOp(_) = {
      for (@slot0, @slot1 <- stateRef) {
        stateRef!(F(slot0), G(slot1)) |
        ...
      }
    } |
    ...
  }
}
```


<details><summary>Example: <a href="CountDownLatch.md">CountDownLatch</a></summary><p>
  
```
???
```
</p></details><br/>

### Restore state

Sometime you read-and-restore stateRef because you need *state*
```
contract foo(ret) = {
  for (@state <- stateRef) {                             // read
    stateRef!(state) | /* do something with 'state' */   // restore
  }
} 
```

Example: read-only two slots Atomic State
```
contract C(_) = {
  new stateRef in {
    stateRef!(0, 0)
  } |
  
  contract getFst(ret) = {
    for (@fst, @snd <- stateRef) {   // read multislot state
      stateRef!(fst, snd) |          // restore multislot state
      ret!(fst)
    }
  } |
  
  contract getSnd(ret) = {
    for (@fst, @snd <- stateRef) {   // read multislot state
      stateRef!(fst, snd) |          // restore multislot state
      ret!(snd)
    }
  } |  
}
```

### Non-blocked update

### Blocked (conditional) update
В случае AtomicInteger у нас состояние - это Int и все операции неблокирующие.

В случае Semaphore у нас состояние - это неотрицательный Int (permission count), операция relese - неблокирующая, но операция acquire - блокирующая на одном значении (на 0, если нет permission - ждем его появления).

```Nil <-> 1 <-> 2 <-> 3 <-> ...```

```
1  new Semaphore in {
2    contract Semaphore(@initPermits, acquireOp, releaseOp) = {
3      new permitsRef in {
4        permitsRef!(initЗermits) |        
5      
6        contract acquireOp(ack) = {
7          for (@(permits /\ Int) <- permitsRef) { 
8            if (permits == 1) { permitsRef!(Nil) } 
9            else { permitsRef!(permits - 1) } |
10           ack!(Nil)
11         }
12       } |
13      
14       contract releaseOp(_) = {
15         for (@permits <- permitsRef) {
16           if (permits == Nil) { permitsRef!(1) }
17           else { permitsRef!(permits + 1) }
18         }
19       } 
20     }
21    }    
22 }
```
**7** - blocking (conditional) read ```for (@(permits /\ Int) <- permitsRef) {...} ```. Wait for *Int* not *Nil*.  
**8-9** - *1-- = Nil* or *k-- = (k-1)* decrement logic.   
**15** - nonblocking (unconditional) read.   
**16-17** - *Nil++ = 1* or *k+ = (k+1)* increment logic.   

### Example
- *state* value saved in channel *stateRef*
- there are two operations: *fooOp* and *barOp*
- each op (*fooOp* and *barOp*) has form: (state, arg) -> (F(state, arg), G(state, arg))

Non-blocking update
```
contract MyPrimitive(@initState, fooOp, barOp) = {
  new stateRef in {
  
    stateRef!(initState) |
    
    contract fooOp(arg, result) = {
      for (@state <- stateRef) {
        stateRef!(F0(state, arg)) | result!(F1(state, arg))
      }
    } |
    
    contract barOp(arg, result) = {
      for (@state <- stateRef) {
        stateRef!(G0(state, arg)) | result!(G1(state, arg))
      }
    }  
  }
}
```

<details><summary>Example: <i>AtomicInteger</i>, *state: Int*, ops = {*set*, *incAndGet*}</summary><p>
  
```  
contract AtomicInteger(@initState, set, incAndGet) = {
  new stateRef in {
  
    stateRef!(initState) |
    
    contract set(arg, ack) = {
      for (_ <- stateRef) {
        stateRef!(arg) | ack!(Nil)
      }
    } |
    
    contract incAndGet(ret) = {
      for (@state <- stateRef) {
        stateRef!(state + 1) | ret!(state + 1)
      }
    }  
  }
}
```
</p></details><br/>

### Join State and Operations Channels

#### Reduction core
???

### Join equi-transformations
```
new stateRef in {
  stateRef!(init) |
  for(arg, result <= foo) = {                          // 'foo' read
    for (@state <- stateRef) {                         
      stateRef!(F0(state, arg)) | result!(F1(state, arg))
    }
  } |
  for (arg, result <= bar) = {                         // 'bar' read
    for (@state <- stateRef) {
      stateRef!(G0(state, arg)) | result!(G1(state, arg))
    }
  }  
}
```

```
new stateRef in {
  stateRef!(init) |
  for (arg, result <= foo; @state <= stateRef) {         // 'foo' read
    stateRef!(F0(state, arg)) | result!(F1(state, arg))
  } |    
  for (arg, result <= bar; @state <= stateRef) {         // 'bar' read
    stateRef!(G0(state, arg)) | result!(G1(state, arg))
  }    
}
```

```
new stateRef in {
  stateRef!(init) |
  for (@state <= stateRef; arg, result <= foo) {         // 'foo' read
    stateRef!(F0(state, arg)) | result!(F1(state, arg))
  } |    
  for (@state <= stateRef; arg, result <= bar) {         // 'bar' read
    stateRef!(G0(state, arg)) | result!(G1(state, arg))
  }    
}
```

Not this!
```
new stateRef in {
  stateRef!(init) |
  for (@state <= stateRef) {                             // for(_ <= stateRef) {for (_ <- foo) {...}}
    for (arg, result <- foo) {
      stateRef!(F0(state, arg)) | result!(F1(state, arg))
    } |    
    for (arg, result <- bar) {                           // for(_ <= stateRef) {for (_ <- foo) {...}}
      stateRef!(G0(state, arg)) | result!(G1(state, arg))
    }    
  }  
}
```
