## java.util.concurrent.ExecutorService in RhoLang

An Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ExecutorService.html)).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

**java.util.concurrent.ExecutorService** (short version)   
```java
public interface ExecutorService {  
  void execute(Runnable command);
  <T> Future<T> submit(Callable<T> task);
  <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);  
  <T> T invokeAny(Collection<? extends Callable<T>> tasks)ж
}
```

<details><summary><b>java.util.concurrent.ExecutorService</b> (long version)</summary><p>
  
```java
public interface ExecutorService {
  
  // Executes the given command at some time in the future.
  void execute(Runnable command);

  // Executes the given tasks, returning a list of Futures 
  // holding their status and results when all complete.
  <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);
  
  // Executes the given tasks, returning the result of one that has completed.  
  <T> T invokeAny(Collection<? extends Callable<T>> tasks)ж
  
  // Submits a Runnable task for execution and returns a Future representing that task.  
  Future<?>submit(Runnable task);	
  
  // Submits a value-returning task for execution and returns 
  // a Future representing the pending results of the task.
  <T> Future<T> submit(Callable<T> task);
}
```
</p></details><br/>

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
