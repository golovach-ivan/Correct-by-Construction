## java.util.concurrent.CompletionStage&lt;T&gt; in RhoLang

A stage of a possibly asynchronous computation, that performs an action or computes a value when another CompletionStage completes. A stage completes upon termination of its computation, but this may in turn trigger other dependent stages.([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CompletionStage.html)).

A Future that may be explicitly completed, and may be used as a CompletionStage, supporting dependent functions and actions that trigger upon its completion. ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CompletableFuture.html)).

**java.util.concurrent.CompletionStage** (short version)   
```java
public interface CompletionStage<T> {
  <U> CompletionStage<U> thenApply(Function<T, U> fn);  
  <U> CompletionStage<U> thenCompose(Function<T, CompletionStage<U>> fn);  
  <U,V> CompletionStage<V> thenCombine(CompletionStage<U> other, BiFunction<T, U, V> fn);  
  CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action);
}
```
**java.util.concurrent.CompletableFuture** (short version)   
```java
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
  // Creates a new incomplete CompletableFuture.
  public CompletableFuture() {...}
  
  public static <U> CompletionStage<U> completedStage(U value) {...}
  public static <U> CompletableFuture<U> completedFuture(U value) {...}
  
  public static CompletableFuture<Void> runAsync(Runnable runnable) {...}  
  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {...}  
  
  // ---
  public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {...}
  public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {...}    
}
```

<details><summary><b>java.util.concurrent.CompletionStage</b> (long version)</summary><p>
  
```java
public interface CompletionStage<T> {

  // Returns a new CompletionStage that, when this stage completes normally, 
  // is executed with this stage's result as the argument to the supplied function.
  <U> CompletionStage<U> thenApply(Function<? super T,? extends U> fn);
  
  // Returns a new CompletionStage that, when this stage completes normally, 
  // is executed with this stage's result as the argument to the supplied action.
  CompletionStage<Void> thenAccept(Consumer<? super T> action);

  // Returns a new CompletionStage that, when this and the other given stage both 
  // complete normally, is executed with the two results as arguments to the supplied action.
  <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action);
  
  // Returns a new CompletionStage that, when this and the other given stage both 
  // complete normally, is executed with the two results as arguments to the supplied function.  
  <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn);
  
  // Returns a new CompletionStage that is completed with the same value 
  // as the CompletionStage returned by the given function. 
  <U> CompletionStage<U> thenCompose(Function<? super T,? extends CompletionStage<U>> fn);

  // Returns a new CompletionStage that, when this stage completes normally, executes the given action.
  CompletionStage<Void> thenRun(Runnable action);
  
  // Returns a new CompletionStage that, when this and the other given stage 
  // both complete normally, executes the given action.
  CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action);

  // Returns a new CompletionStage that, when either this or the other given stage 
  // complete normally, executes the given action.
  CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action);
}
```
</p></details><br/>
<details><summary><b>java.util.concurrent.CompletableFuture</b> (long version)</summary><p>
  
```java
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
  // Creates a new incomplete CompletableFuture.
  public CompletableFuture() {...}
  
  public static <U> CompletionStage<U> completedStage(U value) {...}
  public static <U> CompletableFuture<U> completedFuture(U value) {...}
  
  public static CompletableFuture<Void> runAsync(Runnable runnable) {...}  
  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {...}  
  
  // ---
  public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {...}
  public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {...}    
}
```
</p></details><br/>

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD

```
new thenApply, thenCombine, thenAccept, inc, add in { 

  new futureA, futureB in { 
    futureA!(0) |
    futureB!(0) |
  
    new futureAA, futureBB, futureAABB in {
      thenApply!(*futureA, *inc, *futureAA) |
      thenApply!(*futureB, *inc, *futureBB) |
      thenCombine!(*futureAA, *futureBB, *add, *futureAABB) |
      thenAccept!(*futureAABB, *stdout) } } |  
  
  contract thenApply(future, fun, ret) = {
    for (@arg <- future) {
      fun!(arg, *ret) } } |  
  
  contract thenAccept(future, consumer) = {
    for (@arg <- future) {
      consumer!(arg) } } |  
  
  contract thenCombine(futureA, futureB, fun, ret) = {
    for (@x <- futureA; @y <- futureB) {
      fun!(x, y, *ret) } } |  
  
  contract inc(@{x /\ Int}, ret) = { ret!(x + 1) } |    
  
  contract add(@{x /\ Int}, @{y /\ Int}, ret) = { ret!(x + y) } 
}

```
