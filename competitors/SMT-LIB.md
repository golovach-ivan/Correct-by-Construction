## SMT-LIB
SMT-LIB is an international initiative, coordinated by team and endorsed by a large number of research groups world-wide, aimed at facilitating research and development in [SMT](SMT.md). The main motivation of the SMT-LIB initiative was the expectation that the availability of **common standards** and of **a library of benchmarks** would greatly facilitate the evaluation and the comparison of SMT systems.

### ???
Like Version 2.0 and later versions, Version 2.6 defines:
- a language for writing **terms and formulas** in a sorted (i.e., typed) version of first-order logic;
- a language for specifying **background theories** and fixing a standard vocabulary of sort, function, and predicate symbols for them;
- a language for specifying **logics**, suitably restricted classes of formulas to be checked for satisfiability with respect to a specific background theory;
- a **command** language for interacting with SMT solvers via a textual interface that allows asserting and retracting formulas, querying about their satisfiability, examining their models or their unsatisfiability proofs, and so on.

### Underlying Logic
> SMT-LIB adopts as its underlying logic a version of many-sorted first-order logic with equality. Finally, in addition to the usual existential and universal quantifiers, the logic includes a **let** binder and a **match** binder analogous to constructs with the same name found in functional programming languages. The concrete syntax of the logic is part of the SMT-LIB language of formulas and theories.

### Background Theories
> One of the goals of the SMT-LIB initiative is to clearly define a catalog of background theories, starting with a small number of popular ones, and adding new ones as solvers for them are developed.

> This version of the SMT-LIB standard distinguishes between **basic theories** and **combined theories**. Basic theories, such as the theory of real numbers, the theory of arrays, the theory of fixed-size bit vectors and so on, are those explicitly defined in the SMT-LIB catalog. Combined theories are defined implicitly in terms of basic theories by means of a general modular combination operator.

> The signature of a theory (essentially, its set of sort symbols and sorted function symbols) be specified formally — provided it is finite. By “formally” here we mean written in a machine-readable and processable format, as opposed to written in free text, no matter how rigorously. By this definition, theories themselves are defined informally, in natural language. Some theories, such as the theory of bit vectors, have an infinite signature. For them, the signature too is specified informally in English.

### Input Formulas


### Interface

#### Boolean algebra
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????

#### Int arithmetic
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????

#### Set theory
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????

#### ???
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????
????????????????????????????????????????????????????????

### Links
- [The SMT-LIB Standard: Version 2.6](http://smtlib.cs.uiowa.edu/papers/smt-lib-reference-v2.6-r2017-07-18.pdf)
- [The SMT-LIB website](http://www.smt-lib.org)
- []()
- []()
- []()