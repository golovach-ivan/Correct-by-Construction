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
AtomicInteger это [Atomic State](atomic-state.md) = *Int* with non-blocking operations      
*get* - non-blocking [restore state](atomic-state.md#restore-state)      
*set*, *getAndInc* - [non-blocking update](atomic-state.md#non-blocked-update)     

### Impl
[Sceleton](oop.md#contract--object) modification   
```
1  contract AtomicInteger(@initValue, getOp, setOp, getAndIncOp) = {
2    new valueRef in {
3      valueRef!(initValue) |
4      
5      contract getOp(ret) = { 
6        for (@value <- valueRef) { 
7          valueRef!(value) | ret!(value) } } |
8      
9      contract setOp(newValue, ack) = { 
10       for (_ <- valueRef) { 
11         valueRef!(*newValue) | ack!(Nil) } } |
12     
13     contract getAndIncOp(ret) = { 
14       for (@value <- valueRef) { 
15         valueRef!(value + 1) | ret!(value) } } 
16   } 
17 }
```
**1** - [contract/object](oop.md#contract--object) with [constructor arg](oop.md#initialization) *initValue*        
**2** - [atomic state](atomic-state.md) = Int   
**3** - atomic state [initialization](atomic-state.md#initialization)
**5** - *get* [contract/method](oop.md#contract--method): [sync void -> something](oop.md#sync-void---something)         
**6-7** - [read-and-restore](atomic-state.md#restore-state) atomic state and  return *value*
**9** - *set* [contract/method](oop.md#contract--method): [async something -> void](oop.md#sync-something---void)             
**10-11** - [non-blocked update](atomic-state.md#non-blocked-update) *valueState* with new value and send ack   
**13** - *getAndInc* [contract/method](oop.md#contract--method): [sync void -> something](oop.md#sync-void---something)                   
**14-15** - [non-blocked update](atomic-state.md#non-blocked-update) *valueState* with new value and return updated *value*       

### Complete source code (with demo)

<details><summary>Complete source code for AtomicInteger (with demo)</summary><p>
  
```
new AtomicInteger in {

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
          valueRef!(value + 1) | ret!(value) } } 
    } 
  } |

  new get, set, getAndInc in {
    AtomicInteger!(0, *get, *set, *getAndInc) |
        
    new ack, ret in {
      set!(42, *ack) | for (_ <- ack) {
        getAndInc!(*ret) | for (_ <- ret) {
          get!(*ret) | for (@value <- ret) {
            stdout!(value) } } } }
  }
}
```
```
>> 43
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
