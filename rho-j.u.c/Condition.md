## java.util.concurrent.locks.Condition in RhoLang

*Condition* factors out the *Object* monitor methods (*wait*, *notify* and *notifyAll*) into distinct objects to give the effect of having multiple wait-sets per object, by combining them with the use of arbitrary *Lock* implementations. 
Where a *Lock* replaces the use of synchronized methods and statements, a *Condition* replaces the use of the *Object* monitor methods.

Conditions (also known as *condition queues* or *condition variables*) provide a means for one thread to suspend execution (to "wait") until notified by another thread that some state condition may now be true. Because access to this shared state information occurs in different threads, it must be protected, so a lock of some form is associated with the condition. The key property that waiting for a condition provides is that it atomically releases the associated lock and suspends the current thread, just like *Object.wait*.

A *Condition* instance is intrinsically bound to a lock. To obtain a *Condition* instance for a particular *Lock* instance use its *newCondition()* method ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/locks/Condition.html)).

```java
public interface Condition {
  // Causes the current thread to wait until it is signalled.
  void await();

  // Wakes up one waiting thread.
  void signal();  

  // Wakes up all waiting threads.
  void signalAll();
}
```

### Exercise
