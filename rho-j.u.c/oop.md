## Class/Object structures in RhoLang

### Operations

#### Async void -> void
```
contract countDown(_) = {...} |
countDown!(Nil)
```

#### Async something -> void
```
contract unlock(key) = {...} |
countDown!(Nil)
```

#### Sync void -> void
```
contract await(ack) = {ack!(Nil) | ... } |
countDown!(Nil)
```

#### Sync void -> something
```
contract lock(ret) = {ret!(...) | ... } |
countDown!(Nil)
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
