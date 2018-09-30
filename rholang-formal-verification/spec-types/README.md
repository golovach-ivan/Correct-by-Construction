### Equality
  - Z3: bit count
  - ping server

### Total Spec: Совокупность свойств однозначно определяющая функционал
  - dafny - lsearch, bsearch, sort

### General properties: Liveness/Safety
- ackermann is total function

### Specific functionality: security

### Specific Model: OCAP, spatial

### Behavior: ???

#### Sort
https://rise4fun.com/Boogie/Bubble
```
ensures (forall i, j: int :: 0 <= i && i <= j && j < N ==> a[i] <= a[j]);
// perm is a permutation
ensures (forall i: int :: 0 <= i && i < N ==> 0 <= perm[i] && perm[i] < N);
ensures (forall i, j: int :: 0 <= i && i < j && j < N ==> perm[i] != perm[j]);
// the final array is that permutation of the input array
ensures (forall i: int :: 0 <= i && i < N ==> a[i] == old(a)[perm[i]]);
contract sort(@arr, ret) = {
  ...
}
```

#### Binary Search
https://rise4fun.com/SpecSharp/BinarySearch
```
  public static int BinarySearch(int[] a, int key)
    requires forall{int i in (0: a.Length), int j in (i: a.Length); a[i] <= a[j]};
    ensures 0 <= result ==> a[result] == key;
    ensures result < 0 ==> forall{int i in (0: a.Length); a[i] != key};
```

https://rise4fun.com/OpenJMLESC/BinarySearch
```
    //@ requires (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] <= arr[j]);
    //@ ensures \result == -1 ==> (\forall int i; 0 <= i && i < arr.length; arr[i] != key);
    //@ ensures 0 <= \result && \result < arr.length ==> arr[\result] == key;
    public static int BinarySearch(int[] arr, int key) {
```

#### Linear Search

```
_(ensures \result != -1 ==> ar[\result] == elt)
_(ensures \forall unsigned i; i < sz && i < \result ==> ar[i] != elt)
```
