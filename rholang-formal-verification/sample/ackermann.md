## Ackermann function

Is this function total?

```
function ackermann(m: int, n: int): int = {
  if m <= 0 then
    n + 1
  else if n <= 0 then
    ackermann(m - 1, 1)
  else
    ackermann(m - 1, ackermann(m, n - 1))
}
```

```
contract ackermann(m, n, ret) = {
  if (m <= 0) {
    ret!(n + 1)
  } else {
    if (n < 0) {
        ackermann(m - 1, 1, *ret)
    } else {
      new foo in {
        ackermann(m, n - 1, *foo) | for (@newN <- foo) {
          ackermann(m - 1, *newN, *ret)
        }
      }      
    }
  }
}
```
