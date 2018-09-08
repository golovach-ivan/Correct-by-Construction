## Satisfiability Modulo Theories (SMT)

> Satisfiability Modulo Theories (SMT) is an area of automated deduction that studies methods for checking the satisfiability of first-order formulas with respect to some logical theory T of interest [BSST09]. What distinguishes SMT from general automated deduction is that the background theory T need not be finitely or even first-order axiomatizable, and that specialized inference methods are used for each theory. By being theory-specific and restricting their language to certain classes of formulas (such as, typically but not exclusively, quantifier-free formulas), these specialized methods can be implemented in solvers that are more efficient in practice than general-purpose theorem provers.

> While SMT techniques have been traditionally used to support deductive software verification, they have found applications in other areas of computer science such as, for instance, planning, model checking and automated test generation. Typical theories of interest in these applications include **formalizations of various forms of arithmetic, arrays, finite sets, bit vectors, algebraic datatypes, strings, floating point numbers, equality with uninterpreted functions, and various combinations of these**.

> The defining problem of Satisfiability Modulo Theories is checking whether a given (closed) logical formula ϕ is satisfiable, not in general but in the context of some background theory T which constrains the interpretation of the symbols used in ϕ. Technically, the SMT problem for ϕ and T is the question of whether there is a model of T that makes ϕ true.

### SMT solvers

SMT solver is any software system that implements a procedure for satisfiability modulo some given theory. In general, one can distinguish among a solver’s
1. **underlying logic**, e.g., first-order, modal, temporal, second-order, and so on,
2. **background theory**, the theory against which satisfiability is checked,
3. **input formulas**, the class of formulas the solver accepts as input, and
4. **interface**, the set of functionalities provided by the solver.

#### SMT solver competition (SMT-COMP)

### Links
- Barrett, Sebastiani, Seshia, Tinelli, 2009, "Satisfiability Modulo Theories"
- Barrett, de Moura, Stump, 2005, "SMT-COMP: Satisfiability Modulo Theories Competition"
