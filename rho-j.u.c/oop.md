## Class/Object structures in RhoLang

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

  private Any field0;
  private Any field1;
  public C(Any field0, Any field1) {
    this.field0 = field0;
    this.field1 = field1;
  }
  
  public ... method0(...) {...}
  public ... method1(...) {...}
  public ... method2(...) {...}
}
```
