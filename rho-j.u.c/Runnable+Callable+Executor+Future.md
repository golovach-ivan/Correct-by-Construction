## java.util.concurrent.{Callable, Executor, Future} in RhoLang

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Runnable
### Callable
### Future
### Executor

https://docs.oracle.com/javase/9/docs/api/java/lang/Runnable.html
https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Callable.html
https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Executor.html
https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ExecutorService.html

This interface should be implemented by any class whose instances are intended to be executed by a thread
```java
public interface Runnable {
  void run();
}
```

A task that returns a result
```java
public interface Callable {
  V call();
}
```

An object that executes submitted Runnable tasks
```java
public interface Executor {
  void execute(Runnable command)
}
```

An Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks
```java
public interface ExecutorService {
  // Executes the given command at some time in the future.
  void execute(Runnable command)

  // Submits a value-returning task for execution and returns 
  // a Future representing the pending results of the task.
  <T> Future<T> submit(Callable<T> task);

  // Executes the given tasks, returning a list of Futures 
  // holding their status and results when all complete.
  <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);
  
  // Executes the given tasks, returning the result of one 
  // that has completed successfully, if any do.
  <T> T invokeAny(Collection<? extends Callable<T>> tasks);
  
  // Initiates an orderly shutdown in which previously submitted 
  // tasks are executed, but no new tasks will be accepted.
  void shutdown();
  
  // Attempts to halts the processing of waiting tasks, and 
  // returns a list of the tasks that were awaiting execution.
  List<Runnable> shutdownNow();
}
```

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
