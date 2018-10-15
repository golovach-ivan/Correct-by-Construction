## java.util.concurrent in Rholang

Most popular [JVM concurrency primitives](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/package-summary.html) rewritten in Rholang with explanation. 

New [Atomic State](docs/atomic-state.md) Rholang concurrency pattern proposed. [WaitSet](wait-set.md) (with wait/notify/notifyAll) or [Condition](with await/signal/signalAll) implementation demonstrated. Multiple WaitSets [can be attached](???) to one Atomic State.

Concurrency primitives is a way to partially [order events](events-ordering.md). RhoLang and Java concurrency environments [compared](compare.md).

Different ways to implement [loops](loops.md) descrideb. Value and pattern [conditional](branches.md)  ???.
Base [Object-Oriented practices ???](oop.md).    
  
###  Concurrency Primitives
  - **Basic**: [Runnable](Runnable+Callable+Executor+Future.md#runnable), [Callable](Runnable+Callable+Executor+Future.md#callable), [Future](Runnable+Callable+Executor+Future.md#future), [Executor](Runnable+Callable+Executor+Future.md#executor), [ExecutorService](ExecutorService.md), [CompletionStage](CompletionStage.md)   
  - **Collections and data structures**: [AtomicInteger](AtomicInteger.md), [BlockingQueue](BlockingQueue.md), [ConcurrentMap](ConcurrentMap.md)   
  - **Locks**: [Lock](Lock.md), [Condition](Condition.md), [ReentrantLock](ReentrantLock.md), [ReadWriteLock](ReadWriteLock.md)   
  - **Synchronizers**: [Semaphore](Semaphore.md), [Exchanger](Exchanger.md), [CountDownLatch](CountDownLatch.md), [CyclicBarrier](CyclicBarrier.md), [Phaser](Phaser.md) 
###  Exercises   
- Easy
  - [Implement method ??? for CompletionStage](CompletionStage.md#exercise)   
  - [Implement method ??? for ExecutorService](ExecutorService.md#exercise)     
  - [Implement method ??? for AtomicInteger](AtomicInteger.md#exercise)   
  - [Implement method ??? for BlockingQueue](BlockingQueue.md#exercise)   
  - [Implement method ??? for ConcurrentMap](ConcurrentMap.md#exercise)   
- Mid
  - [Implement method ??? for Lock](Lock.md#exercise)   
  - [Implement method ??? for Condition](Condition.md#exercise)   
  - [Implement method ??? for ReentrantLock](ReentrantLock.md#exercise)   
  - [Implement method ??? for ReadWriteLock](ReadWriteLock.md#exercise)     
  - [Implement method ??? for Exchanger](Exchanger.md#exercise)   
- Hard
  - [Implement method ??? for Semaphore](Semaphore.md#exercise)   
  - [Implement method ??? for Phaser](Phaser.md#exercise)   
  - [Implement method ??? for CyclicBarrier](CyclicBarrier.md#exercise)   
  - [Implement method ```int getCount()``` for CountDownLatch.](CountDownLatch.md#exercise)       

