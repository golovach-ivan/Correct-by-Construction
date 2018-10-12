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

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

### State / Operations Model
TBD

### Explanation
```
new Condition, OneOffValve in {

  // ********** Condition **********
  contract Condition(awaitOp, signalOp, signalAllOp) = {
    new stateRef in {
     
      stateRef!([]) |
      
      contract awaitOp(ack, ackWakeUp) = {
        for (@waitSet <- stateRef) {
          stateRef!(waitSet ++ [*ackWakeUp]) |
          ack!(Nil) } } |
    
      contract signalOp(ack) = {
        for (@waitSet <- stateRef) {
          match waitSet {
            [head...tail] => { 
              stateRef!(tail) |
              @head!(Nil) }
            [] => 
              stateRef!(waitSet)
          } |
          ack!(Nil) } } |
    
      contract signalAllOp(ack) = {
        for (@waitSet <- stateRef) {
          stateRef!([]) |
          new notifyAll in {            
            notifyAll!(waitSet) |
            contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) }  
          } |
          ack!(Nil) } }           
    }  
  } |
  
  // ********** OneOffValve **********
  contract OneOffValve(leanOp, openOp) = {  
    new stateRef, await, signal, signalAll in {
    
      Condition!(*await,  *signal,  *signalAll) |    
      stateRef!(true) |
  
      contract leanOp(ack) = {
        for (@isClose <- stateRef) {                    
          if (isClose) {
            new awaitCallAck, awaitWakeUpAck in {
              await!(*awaitCallAck, *awaitWakeUpAck) | for (_ <- awaitCallAck) {             
                stateRef!(isClose) | 
                for (_ <- awaitWakeUpAck) {
                  leanOp!(*ack)
                }
              }
            }            
          } else {
            stateRef!(isClose) | 
            ack!(Nil) 
          } } } |  

      contract openOp(_) = {
        for (@isClose <- stateRef) {                    
          if (isClose) {
            new ack in {
              signalAll!(*ack) | for (_ <- ack) {
                stateRef!(false)
              }
            }
          } else {
            stateRef!(isClose)
          } } } 
    }
  } |
  
  // ********** Demo **********
  new await, open in {
    OneOffValve!(*await, *open) |
    new ack in {
      await!(*ack) | for (_ <- ack) {
        stdout!("Im free!")
      }
    } |
    open!(Nil) 
  }          
  
}
```
Always reduce to
```
>> Im free!
```

#### Why async methods - wrong decision?
```
new Condition, OneOffValve in {

  // ********** Condition **********
  contract Condition(awaitOp, signalOp, signalAllOp) = {
    new stateRef in {
     
      stateRef!([]) |
      
      contract awaitOp(ack) = {
        for (@waitSet <- stateRef) {
          stateRef!(waitSet ++ [*ack]) } } |
    
      contract signalOp(_) = {
        for (@waitSet <- stateRef) {
          match waitSet {
            [head...tail] => { 
              stateRef!(tail) |
              @head!(Nil) }
            [] => 
              stateRef!(waitSet) } } } |
    
      contract signalAllOp(_) = {
        for (@waitSet <- stateRef) {
          stateRef!([]) |
          new notifyAll in {            
            notifyAll!(waitSet) |
            contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) }  
          } } }           
    }  
  } |
  
  contract OneOffValve(leanOp, openOp) = {  
    new stateRef, await, signal, signalAll in {
    
      Condition!(*await,  *signal,  *signalAll) |    
      stateRef!(true) |
  
      contract leanOp(ack) = {
        for (@isClose <- stateRef) {          
          stateRef!(isClose) | 
          if (isClose) {
            new awaitAck in {
              await!(*awaitAck) | for (_ <- awaitAck) {
                leanOp!(*ack)
              }
            }            
          } else {
            ack!(Nil)
          } } } |  

      contract openOp(_) = {
        for (@isClose <- stateRef) {          
          stateRef!(false) |
          if (isClose) {
            signalAll!(Nil) } } } 
      
    }
  } |
  
  // ********** Demo **********
  new await, open in {
    OneOffValve!(*await, *open) |
    new ack in {
      await!(*ack) | for (_ <- ack) {
        stdout!("Im free!")
      }
    } |
    open!(Nil) 
  }          
  
}
```
Due to reduction race
```
>> Im free!
```
**OR**  
**NOTHING!**

### Complete source code (with demo)
TBD

### Exercise
TBD
