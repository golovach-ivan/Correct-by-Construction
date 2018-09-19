## seq<int> sum

```
new sum in {

  // === SERVER
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
   
  // === CLIENT
  new callback in {
    sum!([1, 2, 3], *callback) | 
    for (@resp <- callback) {stdout!(resp)}
  }
}

>> 6
```

or *sum*-contract can use private *add*-contract
```
[head ...tail] => {
  new add, tailCallback in {
    contract add(x, y, addCallback) = {
      addCallback!(*x + *y)
    } |        
    sum!(tail, *tailCallback) | for(tailSum <- tailCallback) { 
      add!(head, *tailSum, *callback) 
    }
  }
}
```
