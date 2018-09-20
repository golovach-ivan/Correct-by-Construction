### Stream Merge product

### Stream Cartesian product

### Fibonacci stream

### Eratosphene seive stream

## === Everithing is Monad

### Stream map

### Stream filter

```
new iter in {
	
  // 0, 1, 2, 3, ...
  contract iter(@k, chan) = { 
    new buf in {
      chan!(k) | iter!(k + 1, *chan)      
    }
  } |
  
  new chan in {
    iter!(0, *chan) | for (@val <- chan) { 
      stdout!(val) 
    }  
  }
}



new src in {
	
  new buff in {
    src!(0) | buff!(0) | 
  }   
   
  // 0, 1, 2, 3, ...
  contract iter(@k, chan) = { 
    new buf in {
      chan!(k) | iter!(k + 1, *chan)      
    }
  } |
  
  // take 10 from src
  new chan, count in {
    iter!(0, *chan) | for (@val <= chan) { 
      stdout!(val) 
    }  
  }
}
```
