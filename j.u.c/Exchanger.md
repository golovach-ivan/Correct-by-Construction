Этот код эквивалентен следующему

```s!([]) --> storage()``` 

```java
import static java.util.concurrent.ForkJoinPool.commonPool;

public class Exchanger {
    private Channel storage = new Channel();

    public Exchanger(Channel input) throws InterruptedException {
        this.storage.put(new Object[0]);

        commonPool().execute(() -> {
            while (true) {
                try {
                    Object[] x = (Object[]) input.take();
                    Object xItem = x[0];
                    Channel xRet = (Channel) x[1];

                    Object[] maybePair = (Object[]) storage.take();
                    if (maybePair.length == 0) {
                        storage.put(x);
                    } else {
                        Object yItem = maybePair[0];
                        Channel yRet = (Channel) maybePair[1];
                        storage.put(new Object[0]);
                        xRet.put(yItem);
                        yRet.put(xItem);
                    }
                } catch (InterruptedException e) {/*NOP*/}
            }
        });
    }
}
```

```
new Exchanger in {
  
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

  // Demo
  new exchange in {
    Exchanger!(*exchange) |
    new ret in {exchange!(0, *ret) | for (@other <- ret) {stdout!([0, other])}} |
    new ret in {exchange!(1, *ret) | for (@other <- ret) {stdout!([1, other])}} |
    new ret in {exchange!(2, *ret) | for (@other <- ret) {stdout!([2, other])}} |
    new ret in {exchange!(3, *ret) | for (@other <- ret) {stdout!([3, other])}} |
    new ret in {exchange!(4, *ret) | for (@other <- ret) {stdout!([4, other])}} |
    new ret in {exchange!(5, *ret) | for (@other <- ret) {stdout!([5, other])}}
  }
}

>> [1, 5]
>> [5, 1]
>> [0, 3]
>> [2, 4]
>> [3, 0]
>> [4, 2]
```

```
 for (@xItem, @xRet <= input) {          ~           Object[] x = (Object[]) input.take();
                                         ~           Object xItem = x[0];
                                         ~           Channel xRet = (Channel) x[1];                                        
```

```
for (@maybePair <- storage) {            ~           Object[] maybePair = (Object[]) storage.take();
  match maybePair {                      ~           
    [] =>                                ~           if (maybePair.length == 0) {
      ...                                ~             ... 
    [yItem, yRet] => {                   ~           } else if (maybePair.length == 2) {              
      ...                                ~             Object yItem = maybePair[0];
      ...                                ~             Channel yRet = (Channel) maybePair[1];
      ...                                ~             ...
    }                                    ~           }
  }
}
```

```
storage!([xItem, xRet])                  ~           storage.put(x);
```

```
storage!([]) |                           ~           storage.put(new Object[0]);   
@yRet!(xItem) | @xRet!(yItem)            ~           xRet.put(yItem); yRet.put(xItem);
```
