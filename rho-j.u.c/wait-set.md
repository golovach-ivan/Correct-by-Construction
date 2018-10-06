## WaitSet: wait()/notify()/notifyAll()
aaa
- Object implicit monitor
- Condition (Lock, ReadWriteLock)
- CountDownLatch waitSet
- Semaphore.drainAll
- ???

bbb
- [One-off waitSet](#one-off-waitset)  
- [Reusable waitSet](#reusable-waitset)  

### wait()
???

### notify()
???

### notifyAll()
???

### One-off waitSet

```
  contract Condition(waitOp, notifyOp, notifyAllOp) = {
    new waitSet in {

      contract waitOp(wakeUpAck) = {
        waitSet!(*wakeUpAck)
      } |

      contract notifyOp(_) = {
       for (wakeUpAck <- waitSet) {
          wakeUpAck!(Nil)
        }
      } |

      contract notifyAllOp(_) = {
        for (wakeUpAck <= waitSet) {
          wakeUpAck!(Nil)
        }
      } 
    }
  }
```

<details><summary>Complete source code (with comments and demo)</summary><p>

```
new Condition in {

  contract Condition(waitOp, notifyOp, notifyAllOp) = {
    new waitSet in {

      contract waitOp(wakeUpAck) = {
        waitSet!(*wakeUpAck)
      } |

      contract notifyOp(_) = {
       for (wakeUpAck <- waitSet) {
          wakeUpAck!(Nil)
        }
      } |

      contract notifyAllOp(_) = {
        for (wakeUpAck <= waitSet) {
          wakeUpAck!(Nil)
        }
      } 
    }
  }|
  
  new waitOp, notifyOp, notifyAllOp in {
    Condition!(*waitOp, *notifyOp, *notifyAllOp) |
  
    // start 5 waiters
    new loopRange in {
      loopRange!([0, 1, 2, 3, 4]) | for (@[index...tail] <= loopRange) {        
          loopRange!(tail) |
          new wakeUpAck in {
            waitOp!(*wakeUpAck) | for (_ <- wakeUpAck) {
              stdout!([index, "woke up!"])
            }
          }                    
      }
    } |
 
    // 'notifyAll' - wake up them all
    new ack in {
      stdoutAck!("Lets wake up them all.", *ack) | for (_ <- ack) {
        notifyAllOp!(Nil)
      }
    }
  }
}
```
</p></details><br/>

### Reusable waitSet

```
new Condition in {
  contract Condition(waitOp, notifyOp, notifyAllOp) = {
    new waitSetRef in {
    
      new waitSet in {
        waitSetRef!(*waitSet)
      } |      
      
      contract waitOp(wakeUpAck) = {
        for (waitSet <- waitSetRef) {
          waitSet!(*wakeUpAck) |
          waitSetRef!(*waitSet)
        }
      } |

      contract notifyOp(_) = {
        for (waitSet <- waitSetRef) {
          for (wakeUpAck <- waitSet) {
            wakeUpAck!(Nil)
          } |
          waitSetRef!(*waitSet)
        }    
      } |

      contract notifyAllOp(_) = {
        for (waitSet <- waitSetRef) {
          for (wakeUpAck <- waitSet) {
            wakeUpAck!(Nil)
          } |
          new newWaitSet in {
            waitSetRef!(*newWaitSet)
          }
        }  
      }        
    }
  }
}
```

<details><summary>???</summary><p>

```
???
```
</p></details><br/>

