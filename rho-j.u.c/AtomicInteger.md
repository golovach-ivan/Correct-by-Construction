## java.util.concurrent.atomic.AtomicInteger in RhoLang

An *int* value that may be updated atomically ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/atomic/AtomicInteger.html)).

- [Model](#model)
- [Impl](#impl)
- [Complete source code (with demo)](#complete-source-code-with-demo)  
- [Equivalent reduction cores](#equivalent-reduction-cores)  
- [Exercise](#exercise)

<details><summary><b>java.util.concurrent.atomic.AtomicInteger.java</b></summary><p>

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
</p></details><br/>

### Model
???

### Impl
```
1  contract AtomicInteger(@initValue, getOp, setOp, getAndIncOp) = {
2    new valueRef in {
3      valueRef!(initValue) |
4      
5      contract getOp(ret) = { 
6        for (@value <- valueRef) { 
7          valueRef!(value) | ret!(value)
8        } 
9      } |
10      
11     contract setOp(newValue, ack) = { 
12       for (_ <- valueRef) { 
13         valueRef!(*newValue) | ack!(Nil)
14       } 
15     } |
16     
17     contract getAndIncOp(ret) = { 
18       for (@value <- valueRef) { 
19         valueRef!(value + 1) | ret!(value)
20       } 
21     }       
22   }
23 }
```

```
  contract AtomicInteger(@initValue, getOp, setOp, getAndIncOp) = {
    new valueRef in {
      valueRef!(initValue) |
      
      contract getOp(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value) | ret!(value) } } |
      
      contract setOp(newValue, ack) = { 
        for (_ <- valueRef) { 
          valueRef!(*newValue) | ack!(Nil) } } |
      
      contract getAndIncOp(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value + 1) | ret!(value) } } } } |
```

### Complete source code (with demo)

<details><summary>Complete source code for AtomicInteger (with demo)</summary><p>
  
```
new AtomicInteger in {

  contract AtomicInteger(@initValue, getOp, setOp, getAndIncOp) = {
    new valueRef in {
      valueRef!(initValue) |
      
      contract getOp(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value) | ret!(value)
        } 
      } |
      
      contract setOp(newValue, ack) = { 
        for (_ <- valueRef) { 
          valueRef!(*newValue) | ack!(Nil)
        } 
      } |
      
      contract getAndIncOp(ret) = { 
        for (@value <- valueRef) { 
          valueRef!(value + 1) | ret!(value)
        } 
      }       
    }
  } |

  new get, set, getAndInc in {
    AtomicInteger!(0, *get, *set, *getAndInc) |
        
    new ack, ret in {
      set!(42, *ack) | for (_ <- ack) {
       getAndInc!(*ret) | for (_ <- ret) {
         get!(*ret) | for (@value <- ret) {
           stdout!(value)
         }
       }
     }
    }
  }
}
```
</p></details><br/>

### Equivalent reduction cores
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

### Exercise
Implement method [boolean compareAndSet(expect, newValue)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/atomic/AtomicInteger.html#compareAndSet-int-int-) for AtomicInteger in RhoLang.
