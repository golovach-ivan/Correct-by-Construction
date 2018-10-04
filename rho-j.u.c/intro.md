## Intro
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

Рассмотрим пример - *AtomicInteger*, *state: Int*, *foo* == set, *bar* = incAndGet
```
contract AtomicInteger(@initState, set, incAndGet) = {
  new stateRef in {
    stateRef!(initState) |
    contract set(arg, ack) = {
      for (_ <- stateRef) {
        stateRef!(arg) | ack!(Nil)
      }
    } |
    contract incAndGet(ret) = {'
      for (@state <- stateRef) {
        stateRef!(state + 1) | ret!(state + 1)
      }
    }  
  }
}
```

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
