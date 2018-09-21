```
new countRef in {
  countRef!(10) |
  
  // get
  for (@count <- countRef) {
    countRef!(count)
  } |
  
  // set
  for (_ <- countRef) {
    countRef!(100)
  } |  
  
  // incrementAndGet
  for (@count <- countRef) {
    countRef!(count + 1)
  } |   
  
  // 	compareAndSet(10, 20)
  for (@count <- countRef) {
    if (count == 10) {
      countRef!(20)
    } else {
      countRef!(count)
    }
  }  
}
```

```
new AtomicInteger in {

  contract AtomicInteger(@initValue, get, set, getAndIncrement, compareAndSet) = {
    new valueRef in {
      valueRef!(initValue) |
      
      contract get(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value) | ret!(value)
        } 
      } |
      
      contract set(newValue) = { 
        for (_ <- valueRef) { 
          valueRef!(*newValue)
        } 
      } |
      
      contract getAndIncrement(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value + 1) | ret!(value)
        } 
      } |
      
      contract compareAndSet(expect, newValue, ret) = { 
        for (oldValue <- valueRef) {
          if (*oldValue == *expect) {
            valueRef!(*newValue) | ret!(true)
          } else {
            valueRef!(*oldValue) | ret!(false)
          }
        }
      }      
    }
  } |

  new get, set, getAndIncrement, compareAndSet in {
    AtomicInteger!(0, *get, *set, *getAndIncrement, *compareAndSet) |
        
    // get
    new ret in { get!(*ret) | for (@value <- ret) {stdout!(value)} } |
    
    // set
    set!(10) |
    
    // getAndIncrement
    new ret in { getAndIncrement!(*ret) | for (@value <- ret) {stdout!(value)} } |
    
    // compareAndSet
    new ret in { compareAndSet!(0, 20, *ret) | for (@value <- ret) {stdout!(value)} }
  }
}
```