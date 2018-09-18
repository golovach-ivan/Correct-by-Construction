Based on Inria researchers

### Organization: Tezos Foundation Council

- [Twitter (18k+ followers)](https://twitter.com/tezosfoundation?lang=en)
- [Site: tezos.foundation](https://tezos.foundation/)

#### Project: Tezos

Tezos is a self-amending cryptographic ledger. It achieves consensus not just about the state of a ledger, but about the state of its own protocol. Tezos is a blockchain that can evolve through self-amendment. Tezos allows token holders to reach consensus on protocol upgrades, including upgrades to the governance mechanism itself.

- Site: [tezos.com](https://tezos.com)
- Blog (2K followers): [medium.com/tezos](https://medium.com/tezos)
- Position Paper (Aug2014), ["Tezos: A Self-Amending Crypto-Ledger"](https://tezos.com/static/papers/position_paper.pdf)
- White paper (Sep2014), ["Tezos — a self-amending crypto-ledger"](https://tezos.com/static/papers/white_paper.pdf)
- Source code: [gitlab.com/tezos](https://gitlab.com/tezos)
- [Developer Documentation (Sphinx)](https://tezos.gitlab.io/master/)
- [Reddit (10k subscribers)](https://www.reddit.com/r/tezos/)

#### History
- 2014 Sep - the position paper and the white paper published
- 2015 Aug - founded a company, Dynamic Ledger Solutions
- 2016 Sep - the source code was published on Github
- 2017 Feb - the public alphanet was launched
- 2017 Jul - the creation of the Tezos Foundation
- 2018 Jun - betanet launched

#### Personalities
- [Michel Mauny](https://tezos.foundation/tezos-foundation-council) - a senior researcher at Inria, joined Inria in 1985 and worked on programming languages with the research group that designed and developed the Coq proof assistant and the OCaml, the functional programming language used to implement Tezos. From 1989 to 2005 Michel led the research team that further developed OCaml.

#### Science news
- [Tezos Foundation Issues Grants To Four Leading Research Institutions](https://tezos.foundation/news/tezos-foundation-issues-grants-to-four-leading-research-institutions)
- [Tezos Foundation to support Inria and Tarides](https://www.reddit.com/r/tezos/comments/8lhn0g/tezos_foundation_to_support_inria_and_tarides/)

## Main
Tezos	requires	developers	to	write	smart	contracts	in	a	formally-specified	low-level	bytecode	and	construct	proofs	using	the	Coq	theorem	prover.

Our proof-of-stake mechanism is a mix of several ideas, including Slasher[1], chain-of-activity[2], and proof-of-burn.

The philosophy of Tezos is inspired by Peter Suber’s Nomic[1], a game built around a fully introspective set of rules.

OCaml’s semantic is fairly rigorous and a very large subset has been formalized[13], which removes any ambiguity as to what is the intended behavior of amendments.

Coq, one of the most advanced proof checking software is able to extract OCaml code from proofs. As Tezos matures, it will be possible to automatically extract key parts of the protocol’s code from mathematical proofs of correctness.


- High-level language Liquidity with OCaml syntax
- Low-level stack oriented language Michelson 
- Liquidity-to-Michelson compiler with formal semantics - ???
- Michelson working based on OCaml semantics (interpreter validated by the use of OCaml GADTs)
- Tezos developed by OCamlPro that developed SMT Solver ??? too - ???


- contracts will be stored on the blockchain in Michelson format


- [Sources: gitlab.com/tezos (1300 stars, OCalml)](https://github.com/tezos/tezos/)

### Tezos (blockchain)
- Tezos is implemented in OCaml

#### Tezos history 
- July 2017 ICO
- The Tezos network has been live and open since June 30th 2018
- zeronet, mainnet, alphanet,
- The Tezos Alpha (test) network has been live and open since February 2017
- The Tezos Beta (experimental) network has been live and open since June 2018

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

- primitive types: string, nat, int, bool, unit and bytes
- ??? types: list, pair, option, or, set, map, big_map
- domain specific data types: timestamp, mutez (a type for manipulating tokens), contract 'param: A contract, with the type of its code.
address: An untyped contract address.
operation: An internal operation emitted by a contract.
key: A public cryptography key.
key_hash: The hash of a public cryptography key.
signature: A cryptographic signature.

Programs written in Michelson can be reasonably analyzed by SMT solvers and formalized in Coq without the need for more complicated techniques like separation logic.

#### Example: empty contract

```
parameter unit;
storage unit;
return unit;
code {}
```

```
code { CDR ;           # keep the storage
       NIL operation ; # return no internal operation
       PAIR };         # respect the calling convention
storage unit;
parameter unit;
```

- [The Michelson Language (michelson-lang.com/)](https://www.michelson-lang.com/)
- [Michelson: the language of Smart Contracts in Tezos](http://tezos.gitlab.io/master/whitedoc/michelson.html)
- [Online Michelson Interpreter](https://try-michelson.com/)

### Liquidity (high-level languages)

- It uses the syntax of OCaml
- Syntax is a subset of OCaml
- Local variables instead of stack manipulations

Liquidity is a high-level language to program Smart Contracts for Tezos. It is a fully typed functional language, it uses the syntax of OCaml, and strictly complies with Michelson security restrictions. Liquidity already covers 100% of the Michelson features. 

Liquidity is compiled back to Michelson. It is easier to approach as it has local variables instead of stack manipulations and high-level types.

 - ["Liquidity: OCaml for the Blockchain" (slides)](http://www.ocamlpro.com/wp-content/uploads/2018/02/liquidity_slides.en_.pdf)

### OCaml

- The abstract syntax of OCaml light is represented by mutually recursive algebraic datatypes
- The type system (which is purely syntactic and nonalgorithmic) is expressed with inductively defined relations
- The operational semantics is small-step
- op sem = the 137 rules, type system = 173 rules, 
- The operational semantics is expressed as an inductively defined labeled transition relation over source programs
- The (small-step) operational semantics of OCamllight is phrased as a relation on definitions, programs, and stores; 

### Formal verification

A formal-method framework for Liquidity is under development, to prove the correctness of smart-contracts written in Liquidity.

Roadmap: Development of a proof assistant for Liquidity contracts

### Links

#### Liquidity / Michelson
- ???

#### Formal verification
