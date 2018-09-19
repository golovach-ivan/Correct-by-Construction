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

#### Incorrect arg types
```
add!(*callback, 1, 2)       // not add!(1, 2, *callback)
```

```
add!(true, "A", *callback)  // not add!(1, 2, *callback)
```

#### Incorrect arg count
```
add!([1, 2], *callback)  // not add!(1, 2, *callback)
```

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
