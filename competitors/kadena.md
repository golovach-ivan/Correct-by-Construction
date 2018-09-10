- [Kadena (blockchain)](#kadena-blockchain)
  - [Kadena history](#kadena-history)
  - [Blockchain Topology (Chainweb)](#blockchain-topology-chainweb)
  - [Concensus protocol (ScalableBFT / Tangaroa)](#concensus-protocol-scalablebft--tangaroa)
- Pact (language)
  - Pact ???Entities
  - ???
- Formal verification
  - Properties
  - Invariants
  - Schema Invariants
  - Current State

## Kadena (blockchain)

### Kadena history
[Kadena](http://kadena.io/) is a continuation of the **JP Morgan** project Juno/Tangaroa, which was developed based on the **Raft consensus algorithm**. Kadena was built by the two key developers who built Juno: Will Martino and Stuart Popejoy, that iterated on the idea for a year and open sourced the project in February of 2016, adding Pact.

### Blockchain Topology (Chainweb)
Chainweb is a parallel-chain architecture which can combine hundreds to thousands of Proof-of-Work blockchains, increasing throughput to 10,000 transactions per second and beyond. The Chainweb network transacts a single currency using trustless Simple Payment Verification (SPV) cross-chain transfers. Chains incorporate the Merkle roots of each other to enforce a single super chain offering an effective hash power that is the sum of each individual chain’s hash rate.

### Concensus protocol (ScalableBFT / Tangaroa)
ScalableBFT is Byzantine Fault Tolerant (BFT) variant of the Raft consensus algorithm inspired by the original Raft algorithm and the Practical Byzantine Fault Tolerance (PBFT) algorithm.

## Pact (language)

**Pact is an interpreted language**. Pact sources saved in ??blockchain?? (no compilation step) and interpreted during ???. For any function definition in a Pact module, any subsequent call to another function is inlined (???when???: before typecheck/verify/exec). Pact module loading to resolve all references in advance, meaning that instead of addressing functions in a lookup table, the function definition is directly injected (or “inlined”) into the callsite. where the code is always available.

**Pact is Turing-incomplete language**. Pact prohibits recursion and unterminated loops (recursion is detected when smart-contract modules are loaded into the blockchain). Pact does support operation on list structures via *map*, *fold* and *filter*, but since there is no ability to define infinite lists, these are necessarily bounded.

**Pact is database-focused language**. modeling and maintaining database schemas.

**Pact is ???authorizing language**. authorizing users to perform sensitive operations.

**Pact has Formal verification support in design**.

Т.е. перед выполнение любого смарт контракта среда исполнения имеет один файл (inline) исходного не компилированного кода на Pact, упрощенного (нет циклов, рекурсии, гарантированная остановка). Этот код используя keysets модифицирует blockchain, представленный как база данных. Код имеет врапления ?invarians and ?properties, до исполнения ?SMT-prover (например ?Z3) осуществляет ???.

### Pact ???Entities

Execution Modes
1. **Contract definition**. 
Code is sent into the blockchain to establish the smart contract, as comprised of code (modules), tables (data), and keysets (authorization). This can also include database-modifying code, for instance to initialize data.
2. **Transaction execution**.
Refer to business events enacted on the blockchain, like a payment, a sale, or a workflow step of a complex contractual agreement. A transaction is generally a single call to a module function.
3. **Queries and local execution**. 
Querying data is carried out as a local execution on the node receiving the message. Pact code has no ability to distinguish between transactional and local execution.

Contract Definition components
1. **Keyset definition**. Definition stores in the global keyset database.
2. **Module declaration**. Functions, Schema definitions, Table definitions.



Secondary facts
- Pact interpreter is written in Haskell
- Pact uses unbounded integers which don’t overflow
- Variables are immutable: they cannot be re-assigned, or modified in-place

## Formal verification

With Pact’s system, you can specify particular properties in order to prove they are true for all possible inputs and states, but unlike full specification, you don’t need to specify every detail of your program.

The Yoke formal verification system starts by compiling Pact smart contract code directly into the SMT-LIB2 language. This code can then be loaded into the Z3 theorem prover, yielding a system usable by experts in SMT techniques. 

Yoke offers a simple domain specific language (DSL) that can be inserted directly into Pact code to express inviolable business rules.

Pact 2.4 introduces a powerful new system to allow developers to specify **properties** and **invariants** right next to their code.

The big difference here is that these properties and invariants, along with the Pact code itself, are directly compiled into **SMT-LIB2** to be verified by the **Z3** theorem prover, an extremely powerful tool that can test the entire universe of inputs and database states with lightning speed, ensuring that the code can never violate these rules. 

Pact’s property checker works by realizing the language’s semantics in an SMT (“Satisfiability Modulo Theories”) solver – by building a formula for a program, and testing the validity of that formula. The SMT solver can prove that there is no possible assignment of values to variables which can falsify a provided proposition about some Pact code. Pact currently uses Microsoft’s Z3 theorem prover to power its property checking system.

Such a formula is built from the combination of the functions in a Pact module, the properties provided for those functions, and invariants declared on schemas in the module.

For any function definition in a Pact module, any subsequent call to another function is inlined. Before any properties are tested, this inlined code must pass typechecking.

### Properties

**Properties** has vocabulary for talking about function inputs and outputs, and database interactions. 

**Properties** are used on functions to establish behavior that must be enforced no matter what inputs are provided, or what state the blockchain database is in, and resemble “contracts” from languages like Racket or Eiffel. 

### Invariants

**Invariants** have vocabulary for talking about the shape of data. 

Pact’s **invariants** correspond to a simplified initial step towards refinement types, from the world of formal verification.

**Invariants** are rules governing database columns, ensuring that no code that ever writes to the database can ever violate those rules, resembling database constraints in traditional RDBMSs.

### Schema Invariants

For schema invariants, the property checker takes an inductive approach: it assumes that the schema invariants hold for the data currently in the database, and checks that all functions in the module maintain those invariants for any possible DB modification.

### Current state

For this initial release we don’t yet support 100% of the Pact language, and the implementation of the property checker itself has not yet been formally verified, but this is only the first step. We’re excited to continue broadening support for every possible Pact program, eventually prove correctness of the property checker, and continually enable authors to express ever more sophisticated properties about their smart contracts over time.

### Links

#### Kadena links
- Martino, Popejoy, Quaintance, Jan2018, [ChainWeb White Paper](http://kadena.io/docs/chainweb-v15.pdf)
- Quaintance, Martino, Jan2018, ["ChainWeb Protocol Security Calculations"](http://kadena.io/docs/chainweb_calculations_v7.pdf)
- [Project Summary Whitepaper](http://kadena.io/docs/KadenaPublic.pdf)
- [github.com/kadena-io](https://github.com/kadena-io)
- [Ex-J.P. Morgan Heads’ Multi-Chain Smart Contract Platform Kadena](https://medium.com/theblock1/ex-jp-morgan-heads-multi-chain-smart-contract-platform-kadena-4d3be4cedf0)
- Jan2017, ["Kadena Blockchain Uses ScalableBFT Consensus Protocol, Offering 8-12,000 TPS"](https://www.chainofthings.com/news/2017/1/16/kadena-blockchain-uses-scalablebft-consensus-protocol-to-offer-8000-tps)

#### Pact Info
- [github.com/kadena-io/pact](https://github.com/kadena-io/pact)
- [Pact Whitepaper](http://kadena.io/docs/Kadena-PactWhitepaper.pdf)
- [Pact Reference Docs](http://pact-language.readthedocs.io/)
- [Try Pact is in the browser](http://kadena.io/try-pact/)
- [Syntax and linter for the Pact](https://github.com/kadena-io/pact-atom)

#### Pact News
- [Pact 2.4 Is Out! ("The Pact Property Checker" section)](https://medium.com/kadena-io/pact-2-4-is-out-dd88a3e7ca31)
- [Pact Formal Verification: Making Blockchain Smart Contracts Safer](https://medium.com/kadena-io/pact-formal-verification-for-blockchain-smart-contracts-done-right-889058bd8c3f)
