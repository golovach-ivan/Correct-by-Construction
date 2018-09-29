### Ordering errors, ???race condition
```init -> *(lock, unlock)```

### Deadlock

We regard deadlock as a state where (i) processes can no longer be reduced, and (ii) a process is trying to perform an input or output operation annotated with c, but has not succeeded to do so (because there is no corresponding output or input process).

#### Deadlock classic
```
new x, y in {
  for (_ <- x) { y!(Nil) | ...} |
  for (_ <- y) { x!(Nil) | <unlock> ...}
}
```

#### Deadlock process
```
new c in {for (_ <- c) {...}}
```

```
new get, ret in {
  get!(*ret) | for (_ <- ret) {...}
}
```

```
new get, ret in {
  new x, y in {
    for (_ <- x) {y!(Nil)} |
    for (_ <- y) {x!(Nil) | contract get(ret) = {ret!(Nil)}}
  }

  get!(*ret) | for (_ <- ret) {...}
}
```

### Livelock
