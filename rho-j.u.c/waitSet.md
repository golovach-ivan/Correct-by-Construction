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

### One-off waitSet

```
new waitSet, wait, notify, notifyAll in {

  contract wait(wakeUpAck) = {
    waitSet!(*wakeUpAck)
  } |

  contract notify(_) = {
    for (wakeUpAck <- waitSet) {
      wakeUpAck!(Nil)
    }
  } |

  contract notifyAll(_) = {
    for (wakeUpAck <= waitSet) {
      wakeUpAck!(Nil)
    }
  } 
}
```

<details><summary>???</summary><p>

```
???
```
</p></details><br/>

### Reusable waitSet
<details><summary>???</summary><p>

```
???
```
</p></details><br/>

