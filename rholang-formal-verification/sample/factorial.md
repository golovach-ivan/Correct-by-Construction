## factorial function

```
contract factorial(@n, @x, r) = {
  if (n == 0) {
    r!(x)
  } else {
    factorial!(n - 1, x * n, r)
  }
}
```

#### Incorrect: infinite recursion
Not ```factorial!(n - 1, x * n, r)``` but ```factorial!(n, x * n, r)```.

### From
  - - Kobayashi N. (2003) Type Systems for Concurrent Programs. Lecture Notes in Computer Science, vol 2757. Springer, Berlin, Heidelberg.
