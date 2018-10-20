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
new inc, dbl, add, mul in { 

  new a, b, c in {
    a!(0) | b!(1) | c!(2) | 
    
    new x, y in {
    
      new r0, r1 in { inc!(*a, *r0) | dbl!(*r0, *r1) | inc!(*r1, *x) } | 
      new r0 in { dbl!(*b, *r0) | inc!(*r0, *y) } |
         
      new z, w in {
        add!(*x, *y, *z) |        
        mul!(*z, *c, *w) |        
        for (@v <- w) { stdout!(v) } } }    
        
  } |

  contract inc(src, ret) = { 
    for (@val <- src) {
      ret!(val + 1) } } |
  
  contract dbl(src, ret) = { 
    for (@val <- src) {
      ret!(val * 2) } } |
  
  contract add(src0, src1, ret) = { 
    for (@val0 <- src0; @val1 <- src1) {
      ret!(val0 + val1) } } |
  
  contract mul(src0, src1, ret) = { 
    for (@val0 <- src0; @val1 <- src1) {
      ret!(val0 * val1) } }
}
```

```
// new c in {
//   c!(stdout!(0))
// }

// a -> inc -> dbl -> inc -> | 
//                           |->add -> |
// b -> dbl -> inc --------> |         |-> mul -> stdout
// c --------------------------------> |
//
//
//
new inc, dbl, add, mul in { 

  new a, b, c in {
    a!(0) | b!(1) | c!(2) | 
    
    new x, y in {
    
      new r0, r1 in {
        for (@v0 <- a) {        
          inc!(v0, *r0) | for (@v1 <- r0) { 
            dbl!(v1, *r1) | for (@v2 <- r1) { 
              inc!(v2, *x) } } } } |
    
      new r0 in {
        for (@v0 <- b) {        
          dbl!(v0, *r0) | for (@v1 <- r0) { 
            inc!(v1, *y) } } } |    
         
      new z, w in {
        for (@v0 <- x; @v1 <- y) { add!(v0, v1, *z) } |        
        for (@v0 <- z; @v1 <- c) { mul!(v0, v1, *w) } |        
        for (@v <- w) { stdout!(v) }
      }
          
    }
    
  } |

  contract inc(@{arg /\ Int}, ret) = { 
    ret!(arg + 1)
  } |
  
  contract dbl(@{arg /\ Int}, ret) = { 
    ret!(arg * 2)
  } |
  
  contract add(@{arg0 /\ Int}, @{arg1 /\ Int}, ret) = { 
    ret!(arg0 + arg1)
  } |
  
  contract mul(@{arg0 /\ Int}, @{arg1 /\ Int}, ret) = { 
    ret!(arg0 * arg1)
  }  
}

```

```
new a, b, c in {
  a!(0) | b!(1) | c!(2) | 
    
  for (@v0 <- a; @v1 <- b; @v2 <- c) {        
    stdout!( ((((v0+1)*2)+1) + ((v1*2)+1)) * v2 ) } 
} 
```

```java
class App {
    public static void main(String[] args) {

        CompletableFuture<Integer> a = completedFuture(0);
        CompletableFuture<Integer> b = completedFuture(1);
        CompletableFuture<Integer> c = completedFuture(2);

        CompletableFuture<Integer> x =
                a.thenApply(App::inc).thenApply(App::dbl).thenApply(App::inc);
        CompletableFuture<Integer> y =
                b.thenApply(App::dbl).thenApply(App::inc);

        x.thenCombine(y, App::add).thenCombine(c, App::mul).thenAccept(out::println);
    }

    static int inc(int arg) { return arg + 1; }

    static int dbl(int arg) { return arg * 2; }

    static int add(int arg0, int arg1) { return arg0 + arg1; }

    static int mul(int arg0, int arg1) { return arg0 * arg1; }
}
```

```java
class App2 {
    public static void main(String[] args) {

        CompletableFuture<Integer> a = completedFuture(0);
        CompletableFuture<Integer> b = completedFuture(1);
        CompletableFuture<Integer> c = completedFuture(2);

        a.thenApply(App::inc)
                .thenApply(App::dbl)
                .thenApply(App::inc)
                .thenCombine(b.thenApply(App::dbl).thenApply(App::inc), App::add)
                .thenCombine(c, App::mul)
                .thenAccept(out::println);
    }
}
```

```java
class App3 {
    public static void main(String[] args) {

        CompletableFuture<Integer> a = completedFuture(0);
        CompletableFuture<Integer> b = completedFuture(1);
        CompletableFuture<Integer> c = completedFuture(2);

        a.thenApply(x -> x + 1)
                .thenApply(x -> x * 2)
                .thenApply(x -> x + 1)
                .thenCombine(b.thenApply(x -> x * 2).thenApply(arg -> arg + 1), (x, y) -> x + y)
                .thenCombine(c, (x, y) -> x * y)
                .thenAccept(out::println);
    }
}
```

```java
class App4 {
    public static void main(String[] args) {

        CompletableFuture<Integer> a = completedFuture(0);
        CompletableFuture<Integer> b = completedFuture(1);
        CompletableFuture<Integer> c = completedFuture(2);

        CompletableFuture.allOf(a, b, c).thenRun(() -> {
            try {
                int x = ((((a.get() + 1) * 2) + 1) + ((b.get() * 2) + 1)) * c.get();
                out.println(x);
            } catch (InterruptedException | ExecutionException e) { /*NOP*/ }
        });
    }
}
```

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
