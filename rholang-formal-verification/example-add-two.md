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
