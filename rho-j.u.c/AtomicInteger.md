## java.util.concurrent.atomic.AtomicInteger in RhoLang

An *int* value that may be updated atomically ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/atomic/AtomicInteger.html)).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)  
- [Equivalent reduction cores](#equivalent-reduction-cores)  
- [Exercise](#exercise)

**java.util.concurrent.AtomicInteger** (short version)   
```java

public class AtomicInteger {
  public int get() {...}  
  public void set(int newValue) {...}
  public boolean compareAndSet(int expectedValue, int newValue) {...}
}
```

<details><summary><b>java.util.concurrent.atomic.AtomicInteger</b> (long version)</summary><p>

```java
public class AtomicInteger {
  // Creates a new AtomicInteger with the given initial value.
  public AtomicInteger(int initialValue) {...}
  
  // Returns the current value
  public int get() {...}
  
  // Sets the value to newValue
  public void set(int newValue) {...}

  // Atomically sets the value to newValue if the current value == expectedValue
  public boolean compareAndSet(int expectedValue, int newValue) {...}

  // Atomically sets the value to newValue if the current value, 
  // referred to as the witness value, == expectedValue
  public int compareAndExchange(int expectedValue, int newValue) {...}
  
  // Atomically decrements the current value
  public int decrementAndGet() {...}
  
  // Atomically increments the current value
  public int incrementAndGet() {...}
  
  // Atomically decrements the current value
  public int getAndDecrement() {...}
  
  // Atomically increments the current value
  public int getAndIncrement() {...}
  
  // Atomically adds the given value to the current value
  public int getAndAdd(int delta) {...}
  
  // Atomically adds the given value to the current value
  public int addAndGet(int delta) {...}
  
  // Atomically sets the value to newValue and returns the old value
  public int getAndSet(int newValue) {...}
}
```
</p></details><br/>

### State / Operations Model
AtomicInteger это [Atomic State](atomic-state.md) = *Int* with non-blocking operations      
*get* - non-blocking [restore state](atomic-state.md#restore-state)      
*set*, *getAndInc* - [non-blocking update](atomic-state.md#non-blocked-update)     

### Explanation
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

[Reduction core](atomic-state.md#reduction-core)    
```
contract getOp(ret) = { 
  for (@value <- valueRef) { 
    valueRef!(value) | ret!(value) } } |
      
contract setOp(newValue, ack) = { 
  for (_ <- valueRef) { 
    valueRef!(*newValue) | ack!(Nil) } } |
      
contract getAndIncOp(ret) = { 
  for (@value <- valueRef) { 
    valueRef!(value + 1) | ret!(value) } } 
```

can be rewritten to
```      
for (@value <= valueRef; ret <= getOp) { 
  valueRef!(value) | ret!(value) }  |
      
for (@value <= valueRef; newValue, ack <= setOp) { 
    valueRef!(*newValue) | ack!(Nil) } |
      
for (@value <= valueRef; ret <= getAndIncOp) { 
    valueRef!(value + 1) | ret!(value) }
```

### Exercise
Implement method [boolean compareAndSet(expect, newValue)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/atomic/AtomicInteger.html#compareAndSet-int-int-) for AtomicInteger in RhoLang.
