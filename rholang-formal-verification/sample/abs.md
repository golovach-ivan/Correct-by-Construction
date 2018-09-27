## Function Abs
Проблема в том, что такая реализация содержит ошибку работы с 32-bit int: abs(-2147483648) = -2147483648
```
contract abs(@x, ret) = {
  if (x < 0) {
    ret!(-x)
  } else {
    ret!(x)
  }
}
```
