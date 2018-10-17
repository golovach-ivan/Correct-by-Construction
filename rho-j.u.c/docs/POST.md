### Java Monitors in RhoLang

Всякий concurrent programming language нуждается в библиотеке concurrency примитивов для частичного или полного упорядочения событий.

Java имеет 
- встроенный механизм: JMM, volatile, synchronized and implicit monitors (Object.wait/notify/notifyAll)
- ???: Thread (new, join, start, currentThread)
- j.u.c: ???

Monitor - это концепция изобретенная hansen/Hoare и используемая в Java, C#, pthreads, ???.

### Зачем нужен Monitor?

Давайте посмотрим на такой примитив синхронизации как CountDownLatch:
```java
CountDownLatch latch = new CountDownLatch(3);

for (int k = 0; k < 5; k++) {
    new Thread(() -> {
        try { latch.await(); } catch (InterruptedException e) {/*NOP*/}
        print("I woke up!");
    }).start();
}

print("knock-knock"); latch.countDown();
print("KNOCK-KNOCK"); latch.countDown();
print("WAKE UP !!!"); latch.countDown();
```
```
>> knock-knock
>> KNOCK-KNOCK
>> WAKE UP !!!
>> I woke up!
>> I woke up!
>> I woke up!
>> I woke up!
>> I woke up!
```

<details><summary>???</summary><p>
  
```java
import java.util.concurrent.CountDownLatch;

public class Demo {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);

        for (int k = 0; k < 5; k++) {
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {/*NOP*/}
                print("I woke up!");
            }).start();
        }

        print("knock-knock");
        latch.countDown();
        print("KNOCK-KNOCK");
        latch.countDown();
        print("WAKE UP !!!");
        latch.countDown();
    }

    static synchronized void print(Object msg) {
        System.out.println(msg);
    }
}
```
</p></details><br/>


и реализуем его на основе implicit monitor
```java
class CountDownLatch {
    private int count;

    public CountDownLatch(int count) { 
        this.count = count; 
    }

    public synchronized void await() throws InterruptedException {
        while (count != 0) { wait(); }
    }

    public synchronized void countDown() {
        if (count == 1) { notifyAll(); }
        if (count > 0) { count--; }
    }
}
```

или реализуем его на основе explicit monitor
```java
class CountDownLatch {
    private final Lock lock = new ReentrantLock();
    private final Condition countZero = lock.newCondition();
    private int count;

    public CountDownLatch(int count) {
        this.count = count;
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (count != 0) { countZero.await(); }
        } finally { lock.unlock(); }
    }

    public synchronized void countDown() {
        lock.lock();
        try {
            if (count == 1) { countZero.signalAll(); }
            if (count > 0) { count--; }
        } finally { lock.unlock(); }
    }
}
```

### Релизуем CDL на RhoLang
В RhoLang нет implicit monitors как в Java и нет аналога библиотеки j.u.c откуда можно взять Lock/Condition. Однако если мы задумаемся, то нам необходим не wait set, как коллекция, а функционал ожидания наступления определенного условия. Именно это имеется в виду под *conditional variable*.

```
1  new CountDownLatch in {
2    contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
3      new stateRef in {    
4    
5        stateRef!(initCount) |
6  
7        contract awaitOp(ack) = {
8          for (@0 <- stateRef) {          
9            stateRef!(0) | ack!(Nil) } } |  
10
11       contract countDownOp(_) = {
12         for (@{count /\ ~0} <- stateRef) {          
13           stateRef!(count - 1) } } 
14     }    
15   }
16 }  
```

<details><summary>Complete source code for CountDownLatch (with demo)</summary><p>
  
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      stateRef!(initCount) |
  
      contract awaitOp(ack) = {
        for (@0 <- stateRef) {          
          stateRef!(0) | 
          ack!(Nil) } } |  

      contract countDownOp(_) = {
        for (@{count /\ ~0} <- stateRef) {          
          stateRef!(count - 1) } } 
    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(3, *await, *countDown) |
    
    new n in {
      n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | for (@i <= n) { 
        new ack in { 
          await!(*ack) | for (_ <- ack) { stdout!([i, "I woke up!"]) } } } } |     
    
    new ack in { 
      stdoutAck!("knock-knock", *ack) | for (_ <- ack) {
        countDown!(Nil) |
        stdoutAck!("KNOCK-KNOCK", *ack) | for (_ <- ack) {
          countDown!(Nil) |
          stdoutAck!("WAKE UP !!!", *ack) | for (_ <- ack) { 
            countDown!(Nil) } } } }    
  }
}
```
```
>> "knock-knock"
>> "KNOCK-KNOCK"
>> "WAKE UP !!!"
>> [4, "I woke up!"]
>> [1, "I woke up!"]
>> [0, "I woke up!"]
>> [3, "I woke up!"]
>> [2, "I woke up!"]
```
</p></details><br/>

### Как сделать явный wait set в RhoLang?

#### Embedded WaitSet
???
<details><summary>CountDownLatch based on embedded WaitSet</summary><p>
  
```
new CountDownLatch in {
  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef in {    
    
      stateRef!(initCount, []) |
  
      contract awaitOp(ack) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 0) {
            stateRef!(count, waitSet ++ [*ack])
          } else {             
            stateRef!(count, waitSet) |
            ack!(Nil) } } } |  
  
      contract countDownOp(_) = {
        for (@count, @waitSet <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1, waitSet)          
          } else {
            stateRef!(0, []) |            
            new notifyAll in {            
              notifyAll!(waitSet) |
              contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) }  
            } } } }                  
    }    
  }
}
```
</p></details><br/>

#### WaitSet abstracted/separated to contract
???

Мы можем abstract/separat WaitSet to contract
```
new Condition in {
  contract Condition(awaitOp, signalOp, signalAllOp) = {
    new stateRef in {
     
      stateRef!([]) |
      
      contract awaitOp(ackCall, ackWakeUp) = {
        for (@waitSet <- stateRef) {
          stateRef!(waitSet ++ [*ackWakeUp]) |
          ackCall!(Nil) } } |
    
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
  }
}
```

И построить на его основе CountDownLatch
```
new Condition, CountDownLatch in {

  contract Condition(awaitOp, signalOp, signalAllOp) = { ... } |

  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef, await, signal, signalAll in {
    
      Condition!(*await,  *signal, *signalAll) |    
      stateRef!(initCount) |
  
      contract awaitOp(ack) = {
        for (@count <- stateRef) {                    
          if (count == 0) {
            stateRef!(count) | ack!(Nil)             
          } else {
            new ackCall, ackWakeUp in {
              await!(*ackCall, *ackWakeUp) | for (_ <- ackCall) {
                stateRef!(count) | 
                for (_ <- ackWakeUp) {
                  awaitOp!(*ack) } } }                            
          }
        } 
      } |  

      contract countDownOp(_) = {
        for (@count <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1)         // k  ->  k - 1
          } else {
            if (count == 1) {    
              new ack in {              
                signalAll!(*ack) | for (_ <- ack) {
                  stateRef!(count - 1)   // 1  ->  0 
                }
              }
            } else {
              stateRef!(count)           // 0  ->  0
            }     
          }          
        } 
      } 
    }    
  } 
}
```

<details><summary>CountDownLatch based on WaitSet abstracted/separated to contract</summary><p>

```
new Condition, CountDownLatch in {

  contract Condition(awaitOp, signalOp, signalAllOp) = {
    new stateRef in {
     
      stateRef!([]) |
      
      contract awaitOp(ackCall, ackWakeUp) = {
        for (@waitSet <- stateRef) {
          stateRef!(waitSet ++ [*ackWakeUp]) |
          ackCall!(Nil) } } |
    
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

  contract CountDownLatch(@initCount, awaitOp, countDownOp) = {  
    new stateRef, await, signal, signalAll in {
    
      Condition!(*await,  *signal, *signalAll) |    
      stateRef!(initCount) |
  
      contract awaitOp(ack) = {
        for (@count <- stateRef) {                    
          if (count == 0) {
            stateRef!(count) | ack!(Nil)             
          } else {
            new ackCall, ackWakeUp in {
              await!(*ackCall, *ackWakeUp) | for (_ <- ackCall) {
                stateRef!(count) | 
                for (_ <- ackWakeUp) {
                  awaitOp!(*ack) } } }                            
          }
        } 
      } |  

      contract countDownOp(_) = {
        for (@count <- stateRef) {          
          if (count > 1) {
            stateRef!(count - 1)         // k  ->  k - 1
          } else {
            if (count == 1) {    
              new ack in {              
                signalAll!(*ack) | for (_ <- ack) {
                  stateRef!(count - 1)   // 1  ->  0 
                }
              }
            } else {
              stateRef!(count)           // 0  ->  0
            }     
          }          
        } 
      } 

    }    
  } |
  
  new countDown, await in {
    CountDownLatch!(3, *await, *countDown) |
    
    new n in {
      n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | for (@i <= n) { 
        new ack in { 
          await!(*ack) | for (_ <- ack) { stdout!([i, "I woke up!"]) } } } } | 
    
    new ack in { 
      stdoutAck!("knock-knock", *ack) | for (_ <- ack) {
        countDown!(Nil) |
        stdoutAck!("KNOCK-KNOCK", *ack) | for (_ <- ack) {
          countDown!(Nil) |
          stdoutAck!("WAKE UP !!!", *ack) | for (_ <- ack) { 
            countDown!(Nil) } } } }    
  }
}
```
</p></details><br/>

### Когда нужен явный wait set?
- необходимы методы мониторинга (количество)
- мы работаем c waiters (Semaphore.drain for negative permits)
- ??? неописываемый паттерн

