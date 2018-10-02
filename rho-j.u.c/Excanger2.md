## java.util.concurrent.Exchanger

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

#### Model
```
storage:  [] --------------------> [itemA, retA] --------------------> [] --------------------> [itemС, retС]
API calls:   exchange(itemA, retA)               exchange(itemB, retB)    exchange(itemС, retС)
                                                                  retA!(itemB)
                                                                  retB!(itemA)
```

<details><summary>Complete source code</summary>
<p>
  
```
new Exchanger, exchange in {
  
  contract Exchanger(input) = {
    new storage in {
      storage!([]) |                         
      for (@xItem, @xRet <= input) {
        for (@maybePair <- storage) {
          match maybePair {
            [] =>                            
              storage!([xItem, xRet])        
            [yItem, yRet] => {               
              storage!([]) |                 
              @yRet!(xItem) | @xRet!(yItem) 
            } 
          }
        }
      }
    }
  } |

  contract exchange(@my, input) = {
    new ret in {
      input!(my, *ret) | for (@other <- ret) {
        stdout!([my, other])
      }
    }
  } |

  // Demo  
  new input, N in {
    Exchanger!(*input) |
    N!(0) |
    for (@my <= N) {
      if (my < 6) {
        exchange!(my, *input) | N!(my + 1)
      }
    }
  }
}
```
</p>
</details><br/>
