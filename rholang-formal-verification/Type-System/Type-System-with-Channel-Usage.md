## Type System with Channel Usage
A channel can be used as a lock. It, however, works correctly only if the channel is used in an intended manner: When the channel is created, one message should be put into the channel (to model the unlocked state). Afterwards, a process should receive a message from the channel to acquire the lock, and after acquiring the lock, it should eventually release the lock.

A lock can be implemented using a communication channel. Since a receiver on a channel is blocked until a message becomes available, the locked state can be modeled by the absence of a message in the lock channel, and the unlocked state can be modeled by the presence of a message. The operation to acquire a lock is implemented as the operation to receive a message along the lock channel, and the operation to release the lock as the operation to send a message on the channel.



### Type
Usage **0** describes a channel that cannot be used at all.   
Usage **?.U** describes a channel that is first used for input and then used according to U.   
Usage **!.U** describes a channel that is first used for output and then used according to U.   
Usage **U1 | U2** describes a channel that is used according to U1 and U2 possibly in parallel.   
Usage **U1 & U2** describes a channel that is used according to either U1 or U2.   
Usage **µρ.U** describes a channel that is used recursively according to [µρ.U/ρ]U.   

**∗U** is short for µρ.(0 & (U | ρ)).  
**ωU** is short for µρ.(U | ρ).  
 
#### Examples
**µρ.(0 & (!.ρ))** describes a channel that can be sequentially used for output an arbitrary number of times.  

**µρ.(?.!.ρ)** describes a channel that should be used for input and output alternately. 



####
For example, the following process increment the state of the object using a lock channel *lock*
```
new Cell in {
  contract Cell(get, set) = {
    ...
  } |

  new get, set, lock: #{ !|∗(?.!) } in
    lock!(Nil) |                                            // INIT LOCK (set the unlocked state)
    for (_ <- lock) {                                       // LOCK
        new retGet, ackSet in {
          get!(*retGet) | for (@val <- retGet) {            // GET val
            set!(val + 1, ackSet) | for (_ <- ackSet) {     // SET val + 1
              lock!(Nil)                                    // UNLOCK
            }
          }
        }
    }
  }
}
```
Typed only *lock* channel not *get*, *set* and *Cell*.

#### Links 
- ??? Kobayashi, 2003, Type Systems for Concurrent Programs - EXTENDED
- N. Kobayashi, S. Saito, and E. Sumii. An implicitly-typed deadlock-free process calculus. In Proc. of CONCUR2000, volume 1877 of LNCS, pages 489–503. Springer-Verlag, August 2000.
- E. Sumii and N. Kobayashi. A generalized deadlock-free process calculus. In Proc. of Workshop on High-Level Concurrent Language (HLCL’98), volume 16(3) of ENTCS, pages 55–77, 1998.
