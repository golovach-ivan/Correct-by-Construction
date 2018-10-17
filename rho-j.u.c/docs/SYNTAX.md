### 'foo' - free variable not bounded variable (Blocking Queue)
```
new chan in {

  chan!(0) |
  chan!(1) |
  
  for (@foo <- chan) {    
    for (@{bar /\ ~foo} <- chan) {
      stdout!([foo, bar])
    }    
  }
}
```
