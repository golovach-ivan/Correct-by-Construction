## Comparing RhoLang and Java concurrency environments

### No Time
No time in RhoLang - no time limiting methods
- [Future.get(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Future.html#get-long-java.util.concurrent.TimeUnit-)
- [ExecutorService.awaitTermination(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ExecutorService.html#awaitTermination-long-java.util.concurrent.TimeUnit-)
- [Semaphore.tryAcquire(long timeout, TimeUnit unit)](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Semaphore.html#tryAcquire-long-java.util.concurrent.TimeUnit-)
- etc

### Ordering: Happens-before relation -> reduction order

### No spurious wakeups

### Missed Signals

### Data race

### Exceptions

### By-value/by-ref


