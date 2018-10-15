## java.util.concurrent.Executor in RhoLang

An object that executes submitted Runnable tasks
```java
public interface Executor {
  void execute(Runnable command)
}
```

[javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Executor.html)

### Explicit Executor
```
new Executor in {

  contract Executor(taskStream) = {
    for (@task <= taskStream) { task }
  } |
  
  new taskQueue in {
    // create Executor
    Executor!(*taskQueue)|
    
    // send tasks
    taskQueue!(stdout!(0)) |
    taskQueue!(taskQueue!(stdout!(1))) |    
    taskQueue!(new q in {Executor!(*q) | q!(stdout!(2))})
  }
}
```

### Implicit Executor (higher-order processes)
```
new taskQueue in {
  // listen and run
  for (@task <= taskQueue) { task }|
  
  // send tasks
  taskQueue!(stdout!(0)) |
  taskQueue!(taskQueue!(stdout!(1))) |    
  taskQueue!(new q in {for (@t <= q) { t } | q!(stdout!(2))})
}
```
