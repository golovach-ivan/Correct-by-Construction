Tezos	requires	developers	to	write	smart	contracts	in	a	formally-specified	low-level	bytecode	and	construct	proofs	using	the	Coq	theorem	prover.

Our proof-of-stake mechanism is a mix of several ideas, including Slasher[1], chain-of-activity[2], and proof-of-burn.

The philosophy of Tezos is inspired by Peter Suber’s Nomic[1], a game built around a fully introspective set of rules.

OCaml’s semantic is fairly rigorous and a very large subset has been formalized[13], which removes any ambiguity as to what is the intended behavior of amendments.

Coq, one of the most advanced proof checking software is able to extract OCaml code from proofs. As Tezos matures, it will be possible to automatically extract key parts of the protocol’s code from mathematical proofs of correctness.

[1] Peter Suber. Nomic: A game of self-amendment. http://legacy.earlham.edu/~peters/writing/nomic.htm, 1982.
[13] Scott Owens. A sound semantics for ocaml light. http://www.cl.cam.ac.uk/~so294/ocaml/paper.pdf, 2008.

### Tezos (blockchain)
- Tezos is implemented in OCaml

#### Tezos history 

#### Blockchain Topology (???)

#### Concensus protocol (???)

### Michelson (low-level languages)

Michelson is the domain-specific language used to write smart contracts on the Tezos blockchain. 

Michelson
- Michelson is a stack-based language, it doesn't have any variables
- a program is a series of instructions that are run in sequence
- each instruction receives as input the stack resulting of the previous instruction, and rewrites it for the next one
- the stack contains both immediate values and heap allocated structures
- all values are immutable and garbage collected
- Michelson has primitives, high level data types and strict static type checkin

- there is no standard library
- is designed as a compilation target, though it can be hand written (even the output of a compiler can be understood)
- a way to implement pieces of business logic than as a generic “world computer” (like Ethereum)

Programs written in Michelson can be reasonably analyzed by SMT solvers and formalized in Coq without the need for more complicated techniques like separation logic.

- [The Michelson Language (michelson-lang.com/)](https://www.michelson-lang.com/)

### Liquidity (high-level languages)

Liquidity is a high-level language to program Smart Contracts for Tezos. It is a fully typed functional language, it uses the syntax of OCaml, and strictly complies with Michelson security restrictions. Liquidity already covers 100% of the Michelson features. 

Liquidity is compiled back to Michelson. It is easier to approach as it has local variables instead of stack manipulations and high-level types.

### Formal verification

A formal-method framework for Liquidity is under development, to prove the correctness of smart-contracts written in Liquidity.

### Links

#### Tezos (general)
- Main site: [Tezos.com](https://tezos.com)
- White paper (Sep2014), ["Tezos — a self-amending crypto-ledger"](https://tezos.com/static/papers/white_paper.pdf)
- Position Paper (Aug2014), ["Tezos: A Self-Amending Crypto-Ledger"](https://tezos.com/static/papers/position_paper.pdf)

#### Liquidity / Michelson
- ???

#### Formal verification
