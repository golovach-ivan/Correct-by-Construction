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

Каждый канал можно использовать как atomic variable, если хранить там не более одного значения и каждую атомарную операцию представлять, как чтение старого значения, выполнения операции и сохранения нового значения.

Если не допускать записи более одного сообщения в канал, то последовательность будет однозначной: {Write, Read, Write, Read, Write, ...}.

Если эти записи можно разбить на 
- инициализацию (Write) и
- атомарные пары {Read, Write}

то у нас оказываются {Init, Atom, Atom, Atom, Atom, Atom}

Рассмотрим следующую программу:
  1. Мы видим две возможных траектории по редукциям: 
  2. ???
  3. ???
  
Если говорить о иммитации отношения happens-before, то:
  1. ```s!(0)``` будет первым процессом, поскольку другие два гарантированно блокируются на чтении пустого канала.
  2. Вторым будет недетерминированно выбрана одна редукция ```s!(0) | for (@v <- s) {...}```
  3. ???  
```
new s in {
  s!(0) |
  
  for (@v <- s) {
    s!(v + 111) | stdout!(v + 111)
  } |
  
  for (@v <- s) {
    s!(v * 222) | stdout!(v * 222)
  }
}
```

```
                                                                      +---------+
                                                                /+==> |s!(v*222)| ---> ...
                                 +---------+     +------------+/      +---------+
                           /+==> |s!(v+111)| ==> |for(@v <- s)| 
+-----+     +------------+/      +---------+     +------------+\                +----------+
|s!(0)| ==> |for(@v <- s)|                                      \+------------> |stdout!(v)|
+-----+     +------------+\                +----------+                         +----------+
                           \+------------> |stdout!(v)|
                                           +----------+
```                                 
OR
```
                                                                                +---------+
                                                                /=============> |s!(v+111)| ---> ...
                                 +---------+     +------------+/                +---------+
                           /+==> |s!(v*222)| ==> |for(@v <- s)| 
+-----+     +------------+/      +---------+     +------------+\      +----------+
|s!(0)| ==> |for(@v <- s)|                                      \+--> |stdout!(v)|
+-----+     +------------+\                                           +----------+   +----------+                         
                           \+------------------------------------------------------> |stdout!(v)|
                                                                                     +----------+
```

```
new countRef in {
  countRef!(10) |
  
  // get
  for (@count <- countRef) {
    countRef!(count) | ... do something with 'count'
  } |
  
  // set
  for (_ <- countRef) {
    countRef!(100)
  } |  
  
  // incrementAndGet
  for (@count <- countRef) {
    countRef!(count + 1) | ... do something with 'count'
  } |   
  
  // 	compareAndSet(10, 20)
  for (@count <- countRef) {
    if (count == 10) {
      countRef!(20)
    } else {
      countRef!(count)
    }
  } |
  
  // OR
  for (@count <- countRef) {
    countRef!(if (count == 10) { 20 } else { count })   
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
