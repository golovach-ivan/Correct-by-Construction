## java.util.concurrent.atomic.AtomicInteger in RhoLang

An *int* value that may be updated atomically ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/atomic/AtomicInteger.html)).

```java
public class AtomicInteger {
  // Creates a new AtomicInteger with the given initial value.
  public AtomicInteger(int initialValue) {...}
  
  // Returns the current value
  public int get() {...}
  
  // Sets the value to newValue
  public void set(int newValue) {...}
  
  // Atomically sets the value to newValue if the current value, referred to as the witness value, == expectedValue
  public int compareAndExchange(int expectedValue, int newValue) {...}
  
  // Atomically sets the value to newValue if the current value == expectedValue
  public boolean compareAndSet(int expectedValue, int newValue) {...}
  
  public int decrementAndGet() {...}
  public int incrementAndGet() {...}
  public int getAndDecrement() {...}
  public int getAndIncrement() {...}
  public int getAndAdd(int delta) {...}
  public int addAndGet(int delta) {...}
  public int getAndSet(int newValue) {...}
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

#### Equivalent reduction cores
```
for (@value <= valueRef) {            
  for (ret       <- get) { valueRef!(...) | ... } |     
  for (newValue  <- set) { valueRef!(...) | ... } |      
  for (ret <- getAndInc) { valueRef!(...) | ... }
}
```
```           
for (@value <= valueRef; ret       <= get) { valueRef!(...) | ... } |     
for (@value <= valueRef; newValue  <= set) { valueRef!(...) | ... } |      
for (@value <= valueRef; ret <= getAndInc) { valueRef!(...) | ... }
```
```
for (ret       <= get; @value <= valueRef) { valueRef!(...) | ... } |     
for (newValue  <= set; @value <= valueRef) { valueRef!(...) | ... } |      
for (ret <= getAndInc; @value <= valueRef) { valueRef!(...) | ... }
```
```
for (ret       <= get) {            
  for (@value <- valueRef) { valueRef!(...) | ... }
} |     
  
for (newValue  <= set) {              
  for (@value <- valueRef) { valueRef!(...) | ... }
} |      
  
for (ret <= getAndInc) {              
  for (@value <- valueRef) { valueRef!(...) | ... }
}
```

```
contract get(ret) = {            
  for (@value <- valueRef) { valueRef!(...) | ... }
} |     
  
contract set(newValue) = {              
  for (@value <- valueRef) { valueRef!(...) | ... }
} |      
  
contract getAndInc(ret) = {              
  for (@value <- valueRef) { valueRef!(...) | ... }
}
```
