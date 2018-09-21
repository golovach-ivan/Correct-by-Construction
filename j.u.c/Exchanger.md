```
new Exchanger in {
  
  contract Exchanger(exchangeChannel) = {
    new storage in {
      storage!([]) |                          // init storage to empty
      for (@[xItem, xChan] <= exchangeChannel) {
        for (@maybePair <- storage) {
          match maybePair {
            [] =>                             // odd client, no pair now
              storage!([xItem, xChan])        // odd: wait for pair
            [yItem, yChan] => {               // even client, has pair
              storage!([]) |                  // even: restore storage to empty
              @yChan!(xItem) | @xChan!(yItem) // even: do exchange=cross sending
            } 
          }
        }
      }
    }
  } |

  new exchange in {
    Exchanger!(*exchange) |
    new ret in {exchange!([0, *ret]) | for (@x <- ret) {stdout!([0, x])}} |
    new ret in {exchange!([1, *ret]) | for (@x <- ret) {stdout!([1, x])}} |
    new ret in {exchange!([2, *ret]) | for (@x <- ret) {stdout!([2, x])}} |
    new ret in {exchange!([3, *ret]) | for (@x <- ret) {stdout!([3, x])}} |
    new ret in {exchange!([4, *ret]) | for (@x <- ret) {stdout!([4, x])}} |
    new ret in {exchange!([5, *ret]) | for (@x <- ret) {stdout!([5, x])}} |
    Nil
  }
}

>> [1, 5]
>> [5, 1]
>> [0, 3]
>> [2, 4]
>> [3, 0]
>> [4, 2]
```
