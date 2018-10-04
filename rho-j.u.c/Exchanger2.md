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

### Version #1: one process do both transition

### Version #2: two concurrent processes do one transition each

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
2    new atomicRef in {    
3      // INIT
4      atomicRef!([]) |                               
5      // CORE CYCLE
6      for (@itemA, @retA <= input) {
7        for (@maybePair <- atomicRef) {
8          match maybePair {
9            [] => atomicRef!([itemA, retA])        
10           [itemB, retB] => { atomicRef!([]) |                 
11             @retB!(itemA) | @retA!(itemB) 
12           }
13         }
14       }
15     }      
16   }
17 } 
```  
  **4** - ???.   
  **6** - ???.   
  **7-8** - ???.   
  **9** - ???.   
  **10-11** - ???.   

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
