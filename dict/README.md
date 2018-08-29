**Формализм** - это формальный язык наделенный структурами (типы, правила редукции, классы эквивалентности, ???) и семантикой (операционной, денотационной). Известные классы формализмов = алгебры, исчисления, логики.  
**Вычислительный формализм** (Тьюринг-полный) - это ??? синтаксис и computation rules, средствами которых можно выразить произвольный algorithm. В рамках ???этого текста совпадает с исчислением (computation rules = term reduction/transformation rules).  
**Программа/контракт** - это набор инструкций в рамках вычислительного формализма, который определяют/задают конкретный алгоритм.  
**Спецификация** - это набор инструкций в рамках формализма спецификаций.  
**Formal verification** - это функция с сигнатурой (contract, spec) -> bool. 
**RhoLang** - язык программирования, основанный на pi-calculi.
**Pi-calculi** - вычислительный формализм из класса process calculi, поддерживающий два типа formal verification - bisimilarity-based и modal logic based.
**Bisimilarity** - семейство отношений эквивалентности на процессах (weak b, strong b, weak barbed b, strong barbed b, ???) обозначаются ~, ~, ~, ~.
**Bisimilarity based FV** - процедура сопоставления процесса - спецификации, при котором обе задаются средствами одного вычислительного формализма - исчисления процессов.
**Modal logics** - формализмы из класса логик, позволяющие описывать свойства процессов (safety, liveness, security, ???).
**Modal logic based FV** - ???



### FAQ
- what is Formal Verification?
- what is Formal Verification good for?
- Formal Verification vs Testing?
- Formal Verification vs Runtime Contracts?
- Formal Verification vs Type Systems?

### Formalisms: Algebra, Calculi, Logic
- Formal Language, Context-Free grammar, 
- Fixed-point formalisms: calculi, logics

### Process Algebras/Calculi

### Process Logics
- Basics: Predicate, Proposition, Quantifier, ???
- Time: Possible Worlds, Temporal Logics, Branching time Logics
- Actions: Modal / Multimodal / Dynamic Logics
- Space: Separation / Spatial logics
- LTL, CTL, CTL*
- Hennessy-Milner Logic, Modal Mu-calculus

### Formal Verification, Model Checking

### Types
- Typed Pi-Calculi
- Linear Types
- Session Types
- Behavior Types
- Spatial Types

### Operational semantics

### Denational semantics
- Domain Theory
- Category Theory
- LADL
