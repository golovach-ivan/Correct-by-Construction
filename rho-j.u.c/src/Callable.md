## java.util.concurrent.Callable in RhoLang

??? A task that returns a result and may throw an exception.

A task that returns a result ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Callable.html)).
```java
public interface Callable {
  // Computes a result, or throws an exception if unable to do so.
  V call();
}
```
