## java.lang.Runnable in RhoLang

This interface should be implemented by any class whose instances are intended to be executed by a thread ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/lang/Runnable.html)).
```java
public interface Runnable {
  void run();
}
```

### Asynchronous Runnable
Runnable может быть использован в асинхронном режиме, т.е.
```java
Runnable task = ...
new Thread(task).start();
```
```java
Runnable task = ...
Executor exec = ...
exec.execute(task);
```

в таком случае произвольный Process ???является Runnable
```
new tasks in {
  tasks!(stdout!(0)) |
  tasks!(stdout!(1)) |
  tasks!(stdout!(2)) |
  
  allInPar!(tasks)  
} |

contract allInPar(tasks) = {
  for (@task <= tasks) {
    task 
  }  
}
```

### Synchronous Runnable
Runnable может быть использован в синхронном режиме, т.е.
```java
Runnable task = ...
task.run();
```
```java
Runnable task = ...
Thread t = new Thread(task);
t.start();
t.join();
```

в таком случае его можно представить в виде пары Process + AckChannel, так реализован **barrier action** в [CyclicBarrier](CyclicBarrier.md).
```
new tasks in {
  new ack in { tasks!(stdoutAck!(0, *ack)) } |
  new ack in { tasks!(stdoutAck!(1, *ack)) } |
  new ack in { tasks!(stdoutAck!(2, *ack)) } |
  
  oneByOne!(tasks)
} |
  
contract oneByOne(tasks) = {
  for (@task, ack <- tasks) {
    task | for (_ <- ack) {
      oneByOne(tasks)
    }
  }  
}
```
