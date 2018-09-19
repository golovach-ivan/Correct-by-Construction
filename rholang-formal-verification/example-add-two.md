## Add two ints

```
new add in {

  // server
  contract add(x, y, callback) = {
    callback!(*x + *y)
  } |
   
  // client
  new callback in {
    add!(1, 2, *callback) | 
    for (@resp <- callback) {stdout!(resp)}
  }
}
```

### Type errors

### Logic errors

#### Not sum
```
contract add(x, y, callback) = {
  callback!(*x - *y) // '-' not '+'
}
```

#### Int overflows

### Behavior/liveness/safety errors

#### Unbounded recursion
```
contract add(x, y, callback) = {
  add!(*x, *y, *callback) 
}
```

#### Unbounded mutual recursion
```
contract add(x, y, callback) = {
  sub!(*x, *y, *callback)
} |   
contract sub(x, y, callback) = {
  add!(*x, *y, *callback)
}
```
