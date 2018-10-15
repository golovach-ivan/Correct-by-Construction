## java.util.concurrent.Exchanger\<V\> in RhoLang

A synchronization point at which threads can pair and swap elements within pairs. Each thread presents some object on entry to the exchange method, matches with a partner thread, and receives its partner's object on return ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Exchanger.html)).

**java.util.concurrent.Exchanger**  
```java
public class Exchanger<V> {
  /** 
   * Waits for another thread to arrive at this exchange point, and then 
   * transfers the given object to it, receiving its object in return. 
   */  
  public V exchange(V x) {...}
}
```

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)  
- [Equivalent reduction core](#equivalent-reduction-core)  
- [Exercise](#exercise)

### State / Operations Model

```
+-----> Nil--------+
|                  |
|                  |
+--[itemA, retA] <-+
```

```
(state, input) -> (state, result):
  (Nil, (itemA, retA)) -> ((itemA, retA), WAIT)
  ((itemA, retA), (itemB, retB)) -> (Nil, _)
```

```
> Nil
exchange(itemA, retA)
> [itemA, retA]
exchange(itemB, retB)
> Nil
exchange(itemC, retC)
> [itemC, retC]
exchange(itemD, retD)
> Nil
...
```

- [Version #1: one process do both transition](#version-1-one-process-do-both-transition)
- [Version #2: two concurrent processes do one transition each](#version-2-two-concurrent-processes-do-one-transition-each)

### Explanation

### Version #1: one process do both transition

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

### Complete source code (with demo)

<details><summary>Complete source code</summary>
<p>
  
```
new Exchanger in {
  
  contract Exchanger(exchangeOp) = {
    new stateRef in {    
      stateRef!(Nil) |                         
      
      contract exchangeOp(@itemA, @retA) = {
        for (@state <- stateRef) {
          match state {
            Nil => stateRef!([itemA, retA])        
            [itemB, retB] => { stateRef!(Nil) |                 
              @retB!(itemA) | @retA!(itemB) } } } }
    }
  } |

  new exchange in {
    Exchanger!(*exchange) |
    
    new n in {
      n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | n!(5) | for (@i <= n) { 
        new ret in {
          exchange!(i, *ret) | for (@j <- ret) {
            stdout!([i, " -> ", j]) } }
      }
    }
  }
}
```
```
>> [1, " -> ", 0]
>> [0, " -> ", 1]
>> [4, " -> ", 3]
>> [3, " -> ", 4]
>> [5, " -> ", 2]
>> [2, " -> ", 5]
```
</p>
</details><br/>

### Version #2: two concurrent processes do one transition each

```
1  contract Exchanger(input) = {
2    new atomicRef in {    
3      
4      atomicRef!(Nil) |  
5      
6      for (@itemA, @retA <= input; @Nil <= atomicRef) {
7        atomicRef!([itemA, retA])
8      } |
9     
10     for (@itemB, @retB <= input; @[itemA, retA] <= atomicRef) {
11       atomicRef!(Nil) |                 
12       @retB!(itemA) | @retA!(itemB)
13     }
14   }      
15 }
```
**4** - Init.   
**6** - ???.
**7** - ???.   
**10** - ???.   
**11** - ???.   
**12** - ???.   

Core cycles can be rewritten to
```
for (@itemA, @retA <= input; @Nil <= atomicRef) {
  atomicRef!([itemA, retA])
} |
     
for (@itemB, @retB <= input; @[itemA, retA] <= atomicRef) {
  atomicRef!(Nil) |                 
  @retB!(itemA) | @retA!(itemB)
}
```

### Equivalent reduction cores

[Reduction core](atomic-state.md#reduction-core) 
```
```

can be rewritten to
```
```

### Complete source code (with demo)
<details><summary>Complete source code for Exchanger (with demo)</summary><p>  
  
```
new Exchanger in {
  
  contract Exchanger(input) = {
    new atomicRef in {    
    
      // init state
      atomicRef!(Nil) |                               
      
      // *(Nil -> [item, ret])
      for (@itemA, @retA <- input; @Nil <= atomicRef) {
        atomicRef!([itemA, retA])
      } |
      
      // *([item, ret] -> Nil)
      for (@itemB, @retB <- input; @[itemA, retA] <= atomicRef) {
        atomicRef!(Nil) |                 
        @retB!(itemA) | @retA!(itemB)
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
</p></details><br/>

### Exercise
Implement non-blocking method *tryExchange(item, ret)* for Exchanger in RhoLang.
