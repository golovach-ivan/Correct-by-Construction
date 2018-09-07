## Kadena

### Kadena history
Kadena is a continuation of the JP Morgan project Juno, which was forked from Tangaroa, which was developed based on the Raft consensus algorithm, none of which are longer under active development.

The JPM team behind Juno saw the potential that a Tangaroa-like approach represented — a high performance private blockchain. They iterated on the idea for a year and open sourced the project in February of 2016, adding Pact, fixing mistakes and succeeding in achieving a significant performance increase.

Kadena was spawned from the open source code of the Juno project and was built by the two key developers who built Juno: Will Martino and Stuart Popejoy.

### Kadena links
- [Project Summary Whitepaper](http://kadena.io/docs/KadenaPublic.pdf)
- [github.com/kadena-io](https://github.com/kadena-io)
- [Ex-J.P. Morgan Heads’ Multi-Chain Smart Contract Platform Kadena](https://medium.com/theblock1/ex-jp-morgan-heads-multi-chain-smart-contract-platform-kadena-4d3be4cedf0)

### Pact

We strongly disagree with the use of virtual machines that require the storage and invocation of illegible bytecode and instead designed Pact as an interpreted language where the code is always available.

Main facts
- Pact sources saved in ??blockchain?? (no compilation step) and interpreted during ???
- is Turing-incomplete
- Pact is database-focused
- every transaction is a smart contract
- Pact prohibits recursion and unterminated loops

Secondary facts
- Pact interpreter is written in Haskell
- Pact uses unbounded integers which don’t overflow ()

Languages like Ethereum’s Solidity lack critical features that are part of the day-to-day operation of business applications: 
- enforcing business rules (with unambiguous error messages on failure); 
- modeling and maintaining database schemas; and 
- authorizing users to perform sensitive operations.

The Yoke formal verification system starts by compiling Pact smart contract code directly into the SMT-LIB2 language. This code can then be loaded into the Z3 theorem prover, yielding a system usable by experts in SMT techniques. 

Yoke offers a simple domain specific language (DSL) that can be inserted directly into Pact code to express inviolable business rules.

To write safe smart contracts you need a safe language. Pact is immutable, deterministic, and Turing-incomplete, fighting bugs and exploits while offering the full power of a high-level language. Atomic transactions keep your data sane.

With Pact’s system, you can specify particular properties in order to prove they are true for all possible inputs and states, but unlike full specification, you don’t need to specify every detail of your program.

Pact 2.4 introduces a powerful new system to allow developers to specify **properties** and **invariants** right next to their code.

**Properties** are used on functions to establish behavior that must be enforced no matter what inputs are provided, or what state the blockchain database is in, and resemble “contracts” from languages like Racket or Eiffel. 

**Invariants** are rules governing database columns, ensuring that no code that ever writes to the database can ever violate those rules, resembling database constraints in traditional RDBMSs.

The big difference here is that these properties and invariants, along with the Pact code itself, are directly compiled into **SMT-LIB2** to be verified by the **Z3** theorem prover, an extremely powerful tool that can test the entire universe of inputs and database states with lightning speed, ensuring that the code can never violate these rules. 

### Pact Links
#### Info
- [github.com/kadena-io/pact](https://github.com/kadena-io/pact)
- [Pact Whitepaper](http://kadena.io/docs/Kadena-PactWhitepaper.pdf)
- [Pact Reference Docs](http://pact-language.readthedocs.io/)
- [Try Pact is in the browser](http://kadena.io/try-pact/)
- [Syntax and linter for the Pact](https://github.com/kadena-io/pact-atom)
#### News
- [Pact 2.4 Is Out! ("The Pact Property Checker" section)](https://medium.com/kadena-io/pact-2-4-is-out-dd88a3e7ca31)
- [Pact Formal Verification: Making Blockchain Smart Contracts Safer](https://medium.com/kadena-io/pact-formal-verification-for-blockchain-smart-contracts-done-right-889058bd8c3f)
