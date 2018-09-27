### General properties

### Совокупность свойств однозначно определяющая функционал

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
???

#### Linear Search

```
_(ensures \result != -1 ==> ar[\result] == elt)
_(ensures \forall unsigned i; i < sz && i < \result ==> ar[i] != elt)
```
