```
// https://rise4fun.com/Dafny/Mul
method Mul(x: int, y: int) returns (r: int)
  requires 0 <= x && 0 <= y
  ensures r == x*y
  decreases x
{
  if x == 0 {
    r := 0;
  } else {
    var m := Mul(x-1, y);
    r := m + x;
  }
}
```
