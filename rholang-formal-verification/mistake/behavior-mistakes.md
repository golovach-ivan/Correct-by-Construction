### Ordering errors
```init -> *(lock, unlock)```

### Deadlock

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

### Livelock
