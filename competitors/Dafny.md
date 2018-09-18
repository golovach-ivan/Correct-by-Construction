## Dafny

The language and verifier Dafny. The language is imperative, sequential, supports generic classes and dynamic allocation, and builds in specification constructs. In addition to class types, the language supports sets, sequences, and algebraic datatypes.

The specifications include standard pre- and postconditions, framing constructs, and termination metrics.

### Termination check
[CountToN](https://rise4fun.com/Dafny/CountToN) modifications
```
method M(n: int) {
  var i := 0;
  while i < n {  // <
    i := i + 1;
  }
}

>> Dafny program verifier finished with 1 verified, 0 errors
```

```
method M(n: int) {
  var i := 0;
  while i != n {  // !=
    i := i + 1;
  }
}

>> Cannot prove termination
```
