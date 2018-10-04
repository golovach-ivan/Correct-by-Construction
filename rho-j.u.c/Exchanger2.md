## java.util.concurrent.Exchanger\<V\> in RhoLang

A synchronization point at which threads can pair and swap elements within pairs. Each thread presents some object on entry to the exchange method, matches with a partner thread, and receives its partner's object on return ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Exchanger.html)).

```java
public class Exchanger<V> {
  /** 
   * Waits for another thread to arrive at this exchange point, and then 
   * transfers the given object to it, receiving its object in return. 
   */  
  public V exchange(V x) {...}
}
```

- [Version #1: one process do both transition](#version-1-one-process-do-both-transition)
- [Version #2: two concurrent processes do one transition each](#version-2-two-concurrent-processes-do-one-transition-each)

```
+-----> Nil--------+
|                  |
|                  |
+--[itemA, retA] <-+
```

```Nil --> [itemA, retA] --> Nil --> [itemA, retA] --> Nil -->  ...```   

```
for (@arg <= input; @state <= stateRef) {
  f!(arg, state)
}

for (@state <= stateRef; @arg <= input) {
  f!(arg, state)
}

for (@arg <= input) {
  for (@state <- stateRef) {
    f!(arg, state)
  }
}

for (@state <= stateRef) {
  for (@arg <- input) {
    f!(arg, state)
  }
}
```

### Version #1: one process do both transition

**Общая идея**:  
```storage``` реализует шаблон [```atomic channel```](???). В качестве пустого нейтрального значения используется пустой список ```[]```, в качестве рабочего значения - ```[item, ret]```.
```empty --> [] --> empty --> [xItem, xRet] --> empty --> [] --> empty -->  [xItem, xRet] --> empty --> [] -->  ...```   


#### Model
```
storage:  [] --------------------> [itemA, retA] --------------------> [] --------------------> [itemС, retС]
API calls:   exchange(itemA, retA)               exchange(itemB, retB)    exchange(itemС, retС)
                                                                  retA!(itemB)
                                                                  retB!(itemA)
```

### Реализация на RhoLang 
```Exchanger``` на RhoLang может быть реализован следующим образом
```
1  contract Exchanger(input) = {
2    new stateRef in {
3    
4      stateRef!(Nil) |                         
5      
6      for (@state <= stateRef; @itemA, @retA <= input) {
7        match state {
8           Nil => stateRef!([itemA, retA])        
9           [itemB, retB] => { stateRef!(Nil) |                 
10            @retB!(itemA) | @retA!(itemB) 
11          }
12        }
13      }     
14    }
15  } 
```  
**4** - init state to *Nil*.   
**6** - zip *state* and *input* steams.   
**7** - match on currect state.   
**8** - (Nil, (itemA, retA)) -> ((itemA, retA), _)  
**9** - ((itemA, retA), (itemB, retB)) -> (Nil, Nil)   
**10** - do two-way exchange.  

<details><summary>Complete source code</summary>
<p>
  
```
new Exchanger in {
  
  contract Exchanger(input) = {
    new atomicRef in {
    
      // INIT
      atomicRef!([]) |                         
      
      // CORE CYCLE
      for (@itemA, @retA <= input) {
        for (@maybePair <- atomicRef) {
          match maybePair {
            [] => atomicRef!([itemA, retA])        
            [itemB, retB] => { atomicRef!([]) |                 
              @retB!(itemA) | @retA!(itemB) }}}}      
    }
  } |

  // === DEMO
  // for (i = 0; i < 6; i++) {
  //   exchange!(i, ?j) | stdout("%i -> " %j)
  // }
  new exchange, k in {
    Exchanger!(*exchange) |
    k!(0) |
    for (@i <= k) {
      if (i < 6) {
        new ret in {
          exchange!(i, *ret) | for (@j <- ret) {
            stdout!([i, " -> ", j]) }} |  
        k!(i + 1)
      }
    }
  }
}
```
</p>
</details><br/>

### Version #2: two concurrent processes do one transition each

```
1  contract Exchanger(input) = {
2    new atomicRef in {    
3      // INIT
4      atomicRef!(Nil) |                               
5      // *(Nil -> [item, ret])
6      for (@Nil <= atomicRef) {
7        for (@itemA, @retA <- input) {
8          atomicRef!([itemA, retA])
9        }
10     } |
11     // *([item, ret] -> Nil)
12     for (@[itemA, retA] <= atomicRef) {
13       for (@itemB, @retB <- input) {
14         atomicRef!(Nil) |                 
15         @retB!(itemA) | @retA!(itemB)
16       }
17     }
18   }      
19 }
```

<details><summary>Complete source code</summary>
<p>
  
```
new Exchanger in {
  
  contract Exchanger(input) = {
    new atomicRef in {    
      // INIT
      atomicRef!(Nil) |                               
      // CORE CYCLE
      for (@Nil <= atomicRef) {
        for (@itemA, @retA <- input) {
          atomicRef!([itemA, retA])
        }
      } |
      for (@[itemA, retA] <= atomicRef) {
        for (@itemB, @retB <- input) {
          atomicRef!(Nil) |                 
          @retB!(itemA) | @retA!(itemB)
        }
      }
    }      
  } |

  // === DEMO
  // for (i = 0; i < 6; i++) {
  //   exchange!(i, ?j) | stdout("%i -> " %j)
  // }
  new exchange, k in {
    Exchanger!(*exchange) |
    k!(0) |
    for (@i <= k) {
      if (i < 6) {
        new ret in {
          exchange!(i, *ret) | for (@j <- ret) {
            stdout!([i, " -> ", j]) }} |  
        k!(i + 1)
      }
    }
  }
}
```
```
>> [0, " -> ", 1]
>> [1, " -> ", 0]
>> [5, " -> ", 2]
>> [2, " -> ", 5]
>> [3, " -> ", 4]
>> [4, " -> ", 3]
```
</p>
</details><br/>
