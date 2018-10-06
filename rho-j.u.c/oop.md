## Class/Object structures in RhoLang

```
contract C(@field0, @field1, method0, method1, method2) = {

  new stateRef in {
    stateRef!(field0, field1)
  } |  
  
  contract method0(...) = {...} |  
  contract method1(...) = {...} |  
  contract method2(...) = {...}
}
```

<details><summary>Sceleton in Scala</summary><p>
  
```scala
class C(var field0: Any, var field1: Any) {
  
  def method0(...) = ...
  def method1(...) = ...
  def method2(...) = ...
}
```
</p></details><br/>

<details><summary>Sceleton in Java</summary><p>
  
```java
public class C {

  private Object field0;
  private Object field1;
  public C(Object field0, Object field1) {
    this.field0 = field0;
    this.field1 = field1;
  }
  
  public ... method0(...) {...}
  public ... method1(...) {...}
  public ... method2(...) {...}
}
```
</p></details><br/>

### Contract Sceleton

### State

### Construction, initialization

### Operations

#### Async void -> void
Example: [CountDownLatch.countDown()](CountDownLatch.md).
```
// CONTRACT
contract countDown(_) = {...} |

// DEMO
countDown!(Nil)
```

#### Async something -> void
Example: [Lock.unlock()](Lock.md).
```
// CONTRACT
contract unlock(key) = {...} |

// DEMO
new ret in {
  lock!(ret) | for (@key <- ret) {
    ... |
    unlock!(key)
  }
}
```

#### Sync void -> void
Example: [CountDownLatch.await()](CountDownLatch.md).
```
// CONTRACT
contract await(ack) = {ack!(Nil) | ... } |

// DEMO
countDown!(Nil)
```

#### Sync void -> something
Example: [Lock.lock()](Lock.md).
```
// CONTRACT
contract lock(ret) = {ret!(...) | ... } |

// DEMO
countDown!(Nil)
```

#### Sync something -> void
Example: stdoutAck.
```
// CONTRACT
contract stdoutAck(data, ack) = { ... } |

// DEMO
new ack in {
  stdoutAck!(data, ack) | for (_ <- ack) {
    ...
  }
}
```

#### Sync something -> something
Example: [Exchanger.exhange()](Exchanger.md).
```
// CONTRACT
contract exchange(@item, ret) = {...} |

// DEMO
new ret in {
  exchange!(thisItem, ret) | for (@thatItem <- ret) {
    stdout!([thisItem, " -- exchange to --> ", thatItem])
  }
}
```
