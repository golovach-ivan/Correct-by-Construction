## F*

F* (pronounced F star) is a general-purpose ML-like functional programming language with effects aimed at program verification. It puts together the automation of an SMT-backed deductive verification tool with the expressive power of a proof assistant based on dependent types. After verification, F* programs can be extracted to efficient OCaml, F#, or C code. This enables verifying the functional correctness and security of realistic applications, such as a verified HTTPS stack. 

F*'s type system includes dependent types, monadic effects, refinement types, and a weakest precondition calculus. Together, these features allow expressing precise and compact specifications for programs, including functional correctness and security properties. The F* type-checker aims to prove that programs meet their specifications using a combination of SMT solving and interactive proofs. 

The main ongoing use case of F* is building a verified, drop-in replacement for the whole HTTPS stack in [Project Everest](https://project-everest.github.io/). This includes verified implementations of [TLS 1.2 and 1.3](https://github.com/mitls/mitls-fstar) including [the underlying cryptographic primitives](https://github.com/mitls/hacl-star). Moreover, while F* is extracted to OCaml by default, [a subset of F* can be compiled to C for efficiency](https://github.com/FStarLang/kremlin).

### Как используется язык

```
                      +----+
               /+---> | Z3 |
+------------+/       +----+
| F* sources |
+------------+
```

Алгоритмы пишутся на F*.
В код на F* вставляются ???.
??? верифицирует корретность кода на F*.
Special tools extract an F* program to C/F#/OCaml code.

### Example of usage: implementing the TLS 1.3 in C
We have written 20,000 lines of low-level F* code, implementing the TLS 1.3 record layer.
[TLS implemented in F*](https://github.com/project-everest/mitls-fstar)
[miTLS: A Verified Reference Implementation of TLS](https://www.mitls.org/)

### Example of usage: Crypto???



### Как работает верификация

### Example
#### Totality of Ackerman function (partial spec, property checking)
```
module Test

val ackermann: m:nat -> n:nat -> Tot nat
let rec ackermann m n =
  if m=0 then n + 1
  else if n = 0 then ackermann (m - 1) 1
  else ackermann (m - 1) (ackermann m (n - 1))
```
```
Verified module: Test (844 milliseconds)
All verification conditions discharged successfully
```

#### Append property: length (append l1 l2) = length l1 + length l2
```
module Demo

val length: list 'a -> Tot nat
let rec length l = match l with
  | [] -> 0
  | _ :: tl -> 1 + length tl

val append : list 'a -> list 'a -> Tot (list 'a)
let rec append l1 l2 = match l1 with
  | [] -> l2
  | hd :: tl -> hd :: append tl l2

val append_len: l1:list 'a -> l2:list 'a 
         -> Lemma (requires True)
                  (ensures (length (append l1 l2) = length l1 + length l2))
let rec append_len l1 l2 =
  match l1 with 
   | [] -> ()
   | hd::tl -> append_len tl l2

```
```
Verified module: Ex04b (1093 milliseconds)
All verification conditions discharged successfully
```


#### Links
- Home: [fstar-lang.org](https://fstar-lang.org/)
- Sources: [github.com/FStarLang](https://github.com/FStarLang/FStar)
- [F* tutorial with online interpreter](https://www.fstar-lang.org/tutorial/)
- [KreMLin: a tool that extracts an F* program to C code](https://github.com/FStarLang/kremlin)



