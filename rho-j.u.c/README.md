Package [java.util.concurrent](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/package-summary.html) includes a few small standardized extensible frameworks, as well as some classes that provide useful functionality.

[Runnable](https://docs.oracle.com/javase/9/docs/api/java/lang/Runnable.html) interface should be implemented by any class whose instances are intended to be executed by a thread. The class must define a method of no arguments called run. This interface is designed to provide a common protocol for objects that wish to execute code while they are active.

[Callable](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Callable.html) is a task that returns a result and may throw an exception. Implementors define a single method with no arguments called call.

The Callable interface is similar to Runnable, in that both are designed for classes whose instances are potentially executed by another thread. A Runnable, however, does not return a result and cannot throw a checked exception.

[Future](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Future.html) returns the results of a function, allows determination of whether execution has completed, and provides a means to cancel execution. A Future represents the result of an asynchronous computation. Methods are provided to check if the computation is complete, to wait for its completion, and to retrieve the result of the computation. The result can only be retrieved using method get when the computation has completed, blocking if necessary until it is ready. Cancellation is performed by the cancel method. Additional methods are provided to determine if the task completed normally or was cancelled. 

[Executor](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Executor.html) is a simple standardized interface for defining custom thread-like subsystems, including thread pools, asynchronous I/O, and lightweight task frameworks. An object that executes submitted Runnable tasks. This interface provides a way of decoupling task submission from the mechanics of how each task will be run

[AtomicInteger](???) An int value that may be updated atomically.

#### Collections

[ConcurrentLinkedQueue](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html) class supplies an efficient scalable thread-safe non-blocking FIFO queue. An unbounded thread-safe queue based on linked nodes. This queue orders elements FIFO (first-in-first-out). 

[ConcurrentMap](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ConcurrentMap.html) ???.

#### Locks
[Lock](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html) ???

[Condition](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Condition.html) ???

[ReentrantLock](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReentrantLock.html)

[ReadWriteLock](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/ReadWriteLock.html)

#### Synchronizers

[Semaphore](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html) is a classic concurrency tool. A counting semaphore. Conceptually, a semaphore maintains a set of permits. Each acquire() blocks if necessary until a permit is available, and then takes it. Each release() adds a permit, potentially releasing a blocking acquirer.

[CountDownLatch](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html) is a very simple yet very common utility for blocking until a given number of signals, events, or conditions hold. A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes. A CountDownLatch is initialized with a given count. The await methods block until the current count reaches zero due to invocations of the countDown() method, after which all waiting threads are released and any subsequent invocations of await return immediately. 

[CyclicBarrier](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CyclicBarrier.html) a resettable multiway synchronization point useful in some styles of parallel programming. A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point. CyclicBarriers are useful in programs involving a fixed sized party of threads that must occasionally wait for each other. 

CountDownLatch is a one-shot phenomenon -- the count cannot be reset. If you need a version that resets the count, consider using a CyclicBarrier. The barrier is called cyclic because it can be re-used after the waiting threads are released. A CyclicBarrier supports an optional barrier action ??? is useful for updating shared-state before any of the parties continue.

[Phaser](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Phaser.html) provides a more flexible form of barrier that may be used to control phased computation among multiple threads. A reusable synchronization barrier, similar in functionality to CyclicBarrier and CountDownLatch but supporting more flexible usage.

[Exchanger](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Exchanger.html) allows two threads to exchange objects at a rendezvous point, and is useful in several pipeline designs. A synchronization point at which threads can pair and swap elements within pairs. Each thread presents some object on entry to the exchange method, matches with a partner thread, and receives its partner's object on return. Exchangers may be useful in applications such as genetic algorithms and pipeline designs.

### You can block on any pattern
**structure pattern**  
block on linked list:   
item0 = [0, Nil],   
item1 = [1, item0],   
item2 = [2, item1]  
...  
```contract copy(src, dst) = for (@[item, next] <= src) { dst!(item) }```

**value pattern** 
block on linked list:   
itemA = (false, true,  1, (A, ())),   
itemB = (true,  true,  2, (B, itemA)),   
itemC = (true,  true,  3, (C, itemB)),   
itemD = (true,  false, 4, (D, itemC))

### There is no destinction between data and processes: Streams == Futures
Everething is Process or can be converted to Process. Every Process if Thread.  
Everething is Channel or can be converted to Channel. Every Channel if Stream.  
Everething can be throuth as Thread and Stream at the same time.

### Concurrency primitives

#### ∃n<-1: select, queue

#### ∀n<-1: wait
#### 1->∀n: notifyAll

#### 1<-∀n: join
```
new c0, c1, c2, c3 in {
  // send all
  c0!(0) | c1!(1) | c2!(2) | c3!(3) |
  
  // receive all
  for (@v0 <- c0; @v1 <- c1; @v2 <- c2; @v3 <- c3) {
    stdout!([v0, v1, v2, v3])
  }
}
```

```
new c0, c1, c2, c3 in {
  // send all
  c0!!(0) | c1!!(1) | c2!!(2) | c3!!(3) |
  
  // receive all N times
  new N in {
    N!(10) |
    for (@v0 <= c0; @v1 <= c1; @v2 <= c2; @v3 <= c3; @k <= N) {      
      if (k > 0) { N!(k - 1) } |
      stdout!([v0, v1, v2, v3])
    }
  }
}
```

```
new join in {
  contract join(seq, ret) = {
    match *seq {
      [head] => { for (@value <- @head) { ret!([value]) } }
      [head...tail] => {
        new retTail in {
          join!(tail, *retTail) | 
          for (@headItem <- @head; @tailItems <- retTail) {
            ret!([headItem] ++ tailItems)
          }
        }
      }
    }
  } |
  
  new c0, c1, c2, c3 in {
    // send all
    c0!(0) | c1!(1) | c2!(2) | c3!(3) |
  
    // receive all
    new ret in {
      join!([*c0, *c1, *c2, *c3], *ret) | for (@value <- ret) {
        stdout!(value)
      }
    }
  }
}
```

#### 1<-∃n: select
```
new copy in {
  
  contract copy(src, dst) = {
    for (@val <- src) {
      new ack in {
        dst!((val, *ack)) | for (_ <- ack) { copy!(*src, *dst) }
      }
    }
  } |  
  
  new c0, c1 in {
  
    // send all
    c0!!(0) | c1!!(1) |  
  
    // receive any N times
    new N, R in {    
      copy!(*c0, *R) | copy!(*c1, *R) |
      
      N!(10) |
      for (@(val, ack) <= R; @k <= N) {
        if (k > 0) { N!(k - 1) } | @ack!(Nil) |
        stdout!(val)    
      }
    }  
  }
}
```
