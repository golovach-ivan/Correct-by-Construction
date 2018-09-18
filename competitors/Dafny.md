## Dafny

The language and verifier Dafny. The language is imperative, sequential, supports generic classes and dynamic allocation, and builds in specification constructs. In addition to class types, the language supports sets, sequences, and algebraic datatypes. The types available in Dafny are booleans, mathematical integers, (possibly null) references to instances of user-defined generic classes, sets, sequences, and user-defined algebraic datatypes.

The specifications include standard pre- and postconditions, framing constructs, and termination metrics.

Dafny’s program verifier works by translating a given Dafny program into the intermediate verification language Boogie 2. The Boogie tool is then used to generate first-order verification conditions that are passed to a theorem prover, in particular to the SMT solver Z3.

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
