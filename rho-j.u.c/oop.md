## Class/Object structures in RhoLang

### Operations

#### Async void -> void
```
// CONTRACT
contract countDown(_) = {...} |

// DEMO
countDown!(Nil)
```

#### Async something -> void
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
```
// CONTRACT
contract await(ack) = {ack!(Nil) | ... } |

// DEMO
countDown!(Nil)
```

#### Sync void -> something
```
// CONTRACT
contract lock(ret) = {ret!(...) | ... } |

// DEMO
countDown!(Nil)
```

#### Sync something -> void
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

### Scala
```scala
class C(field0: Any, field1: Any) {
  
  def method0(...) = ...
  def method1(...) = ...
  def method2(...) = ...
}
```

### RhoLang
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

### Java
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
