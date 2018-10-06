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
```
contract op(@arg, out) = {
  for (@state <- stateRef) {
    stateRef!(F(state, arg)) | out!(G(state, arg))
  }
}  
```
**arg** - аргумент операции, может отсутствовать (как у *get*), быть один (как у *set*) или будет ???множество (как у *???*).   
**out** - обычно называется *ret* у возвращающих значение (```get(ret)```), *ack* у синхронных void (```set(arg, ack)```), отсутсвуют у асинхронных void (```set(arg)```).   
**state** - ???.   
**F** - ???.   
**G** - ???.   

### Multislot State

RhoLang is a **polyadic π-calculi** extension so multivalued channel for free ```stateRef!(slot0, slot1)``` + ```for (@slot0, @slot1 <- stateRef)```
```
contract C(@initSlot0, @initSlot1, fooOp, ...) = {
  new stateRef in {
    stateRef!(initSlot0, initSlot1) |
    
    contract fooOp(_) = {
      for (@slot0, @slot1 <- stateRef) {
        stateRef!(F0::(slot0), F1::(slot1)) |
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
