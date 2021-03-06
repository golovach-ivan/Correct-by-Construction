## Comparing RhoLang and Java concurrency environments

### No Time
No time in RhoLang - no time limiting methods
- [Future.get(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Future.html#get-long-java.util.concurrent.TimeUnit-)
- [ExecutorService.awaitTermination(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ExecutorService.html#awaitTermination-long-java.util.concurrent.TimeUnit-)
- [Semaphore.tryAcquire(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html#tryAcquire-long-java.util.concurrent.TimeUnit-)
- etc

### No implicit knowledge Thread.currentThread()
1) No ReentrantLock  
1) No ReentrantReadWriteLock  

### Fair / Unfair mode

### Ordering: Happens-before relation -> reduction order

### No spurious wakeups

### Missed Signals

### Data race

### Exceptions

### No InterruptedException
- [Lock.lockInterruptibly() throws InterruptedException](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Lock.html#lockInterruptibly--)
- [Semaphore.acquire() throws InterruptedException](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html#acquire--)
- [CountDownLatch.await() throws InterruptedException](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/CountDownLatch.html#await--)

### By-value/by-ref


