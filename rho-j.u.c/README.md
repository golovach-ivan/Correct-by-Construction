## java.util.concurrent in Rholang

Most popular [JVM concurrency primitives](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/package-summary.html) rewritten in Rholang with explanation. 

New [Atomic State](docs/atomic-state.md) Rholang concurrency pattern proposed. [WaitSet](docs/wait-set.md) (with wait/notify/notifyAll) or [Condition](src/Condition.md)(with await/signal/signalAll) implementation demonstrated. Multiple WaitSets [can be attached](???) to one Atomic State.

Concurrency primitives is a way to partially [order events](docs/events-ordering.md). RhoLang and Java concurrency environments [compared](docs/compare.md).

[Loops](docs/loops.md) and [conditionals](docs/conditionals.md) showed.
Contract vs Object/Class [comparison](docs/oop.md) proposed.    
  
###  Concurrency Primitives
  - **Basic**: [Runnable](src/Runnable.md), [Callable](src/Callable.md), [Future](src/Future.md), [Executor](src/Executor.md), [ExecutorService](src/ExecutorService.md), [CompletionStage](src/CompletionStage.md)   
  - **Collections and data structures**: [AtomicInteger](src/AtomicInteger.md), [BlockingQueue](src/BlockingQueue.md), [ConcurrentMap](src/ConcurrentMap.md)   
  - **Locks**: [Lock](src/Lock.md), [Condition](src/Condition.md), [ReentrantLock](src/ReentrantLock.md), [ReadWriteLock](src/ReadWriteLock.md)   
  - **Synchronizers**: [Semaphore](src/Semaphore.md), [Exchanger](src/Exchanger.md), [CountDownLatch](src/CountDownLatch.md), [CyclicBarrier](src/CyclicBarrier.md), [Phaser](src/Phaser.md) 
###  Exercises   
- Easy
  - [Implement method ??? for CompletionStage](src/CompletionStage.md#exercise)   
  - [Implement method ??? for ExecutorService](src/ExecutorService.md#exercise)     
  - [Implement method ??? for AtomicInteger](src/AtomicInteger.md#exercise)   
  - [Implement method ??? for BlockingQueue](src/BlockingQueue.md#exercise)   
  - [Implement method ??? for ConcurrentMap](src/ConcurrentMap.md#exercise)   
- Mid
  - [Implement method ??? for Lock](src/Lock.md#exercise)   
  - [Implement method ??? for Condition](src/Condition.md#exercise)   
  - [Implement method ??? for ReentrantLock](src/ReentrantLock.md#exercise)   
  - [Implement method ??? for ReadWriteLock](src/ReadWriteLock.md#exercise)     
  - [Implement method ??? for Exchanger](src/Exchanger.md#exercise)   
- Hard
  - [Implement method ??? for Semaphore](src/Semaphore.md#exercise)   
  - [Implement method ??? for Phaser](src/Phaser.md#exercise)   
  - [Implement method ??? for CyclicBarrier](src/CyclicBarrier.md#exercise)   
  - [Implement method ??? for CountDownLatch.](src/CountDownLatch.md#exercise)       

