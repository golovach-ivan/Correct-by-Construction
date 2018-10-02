Логика - это формализм, позволяющий строить высказывания (формулы логики). Каждая формула логики с одной ???дыркой определяет предикат на процессах. Каждый процесс или удовлетворяет этому высказыванию или нет.

В зависимости от типа 

Один из способов определять свойства процессов - выделить подмножество удовлетворяющих формуле некоторой логики (Модальной, Spatial, Separational, etc).



#### Zero-order logic (propositional logic, propositional calculus)
```φ, ψ ::= tt | ff | ¬φ | φ ∧ ψ | φ ∨ ψ```

Syntax sugar:  
```ff ≝ ¬tt```  
```φ ∨ ψ ≝ ¬(¬φ ∧ ¬ψ)```  


Logical connectives (logical operator, logical combinator): ```¬, ∧, ∨```

#### First-order logic (predicate logic)
```
x - name
φ, ψ ::= tt | ff | ¬φ | φ ∧ ψ | φ ∨ ψ | x | ∃x.φ | ∀x.φ
```
Quantifiers: ```∀, ∃```

Logical connectives (logical operator, logical combinator): ```¬, ∧, ∨```

#### Simple Modal logic (Hennessy-Milner Logic for Pi-Calculus)
```
α - action
φ ::= tt | ¬φ | φ ∧ ψ | 〈α〉φ
```
Syntax sugar: ```〈α〉φ ≝ ¬([a](¬φ))```

Т.е. simple modal logic можно рассматривать как расширение propositional logic, но не кванторами ```∀, ∃```, а модальностями ```〈∘〉, ([∘]```.

### Logics: Predicate, Modal, Dynamic, Temporal, ???
  
### Hennesy-Milner variant for Pi-Calculus
Following the initial approach of Hennessy-Milner [22], modal logics for the calculus have been proposed in [29, 16, 17]
  - [29] R. Milner, J. Parrow, and D. Walker. Modal logics for mobile processes. Theoretical Computer Science, 114:149–171, 1993.
  - [16] M. Dam. Model checking mobile processes. Information and Computation, 129(1):35–51, 1996.
  - [17] M. Dam. Proof systems for -calculus logics. In de Queiroz, editor, Logic for Concurrency and Synchronisation, Studies in Logic and Computation. Oxford University Press, To appear.

### Spatial Logics

### Freshness
The connections between name restriction and Gabbay-Pitts notions of freshness [20, 19, 31], first studied in [11], are further explored in this pa per.
- [20] M. Gabbay and A. Pitts. A New Approach to Abstract Syntax Involving Binders. In 14th Annual Symposium on Logic in Computer Science, pages 214–224. IEEE Computer Society Press, Washington, 1999
- [19] M. Gabbay and A. Pitts. A New Approach to Abstract Syntax Involving Binders. To appear in Formal Aspects of Computing.
- [31] A. Pitts. Nominal Logic: A First Order Theory of Names an d Binding. In B.C. Pierce N. Kobayashi, editor, Proceedings of the 10th Symposium on Theoretical Aspects of Computer Science (TACS 2001), volume 2215 of Lecture Notes in Computer Science, pages 219–235. Springer-Verlag, 2001.

### Separation Operator
Separation Logic [32]
[32] J. C. Reynolds. Separation Logic: A Logic for Shared Mutable Data Structures. In Proceedings of the Third Annual Symposium on Logic in Computer Science, Copenhagen, Denmark, 2002. IEEE Computer Society.

namely, of a tensor operator that corresponds t o process composition, and of a revelation operator that corresponds to name restricti on


