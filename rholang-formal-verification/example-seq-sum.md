## seq<int> sum

```
new sum in {

  // server
  contract sum(@arr, callback) = {
    match arr {
      [] => callback!(0) 
      [head ...tail] => {
        new tailCallback in {
          sum!(tail, *tailCallback) | for(tailSum <- tailCallback) { 
            callback!(head + *tailSum) 
          }
        }
      }
    }
  } |
   
  // client
  new callback in {
    sum!([1, 2, 3], *callback) | 
    for (@resp <- callback) {stdout!(resp)}
  }
}

>> 6
```
