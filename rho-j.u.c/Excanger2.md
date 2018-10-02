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
