## Type System with Channel Usage

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
```
new Cell in {
  contract Cell(get, set) = {
    ...
  } |

  new get, set, lock: #{ !|∗(?.!) } in
    lock!(Nil) |
    for (_ <- lock) {                                       // LOCK
        new retGet, retSet in {
          get!(*retGet) | for (@val <- retGet) {            // GET val
            set!(val + 1, retSet) | for (_ <- retSet) {     // SET val + 1
              lock!(Nil)                                    // UNLOCK
            }
          }
        }
    }
  }
}
```

#### Links
- usage [24, 44],
