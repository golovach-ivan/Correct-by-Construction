### Spatial modalities, Modal logics

Modal logics (particularly, temporal logics) have emerged in many domains as a good compromise between expressiveness and abstraction. In addition, many modal logics support useful computational applications, such as model checking

Spatial configurations evolve over time as a consequence of the activities  of  processes.

We  regard  mobility  as  the  evolution of  spatial  configurations  over time. A specification logic for mobility should be able to talk about the  structure  of  spatial  configurations  and  about  their  evolution through time; that is, it should be a modal logic of space and time.

Spatial modalities  have an intensional  flavor  that  distinguishes  our logic from other modal logics for concurrency.

We present a logic that can express properties of freshness, secrecy, structure, and behavior of concurrent systems. In addition to standard logical and temporal operators, our logic includes spatial operations corre sponding to composition, local name restriction, and a primitive fresh name quantifie r.

The spatial properties that we consider here are essentially of two kinds: 
  - whether a system is composed of two or more identifiable subsystems, 
  - and whether a system restricts the use of certain resources to certain subsystems.

The initial motivation for studying these logics was to be able to specify systems that deal with fresh or secret resources such as keys, nonces , channels, and locations.

### Freshness
logical notions of freshness [20, 19, 31]
[20] M. Gabbay and A. Pitts. A New Approach to Abstract Syntax Involving Binders. In 14th Annual Symposium on Logic in Computer Science, pages 214–224. IEEE Computer Society Press, Washington, 1999.
[19] M. Gabbay and A. Pitts. A New Approach to Abstract Syntax Involving Binders. To appear in Formal Aspects of Computing.
[31] A. Pitts. Nominal Logic: A First Order Theory of Names an d Binding. In B.C. Pierce N. Kobayashi, editor, Proceedings of the 10th Symposium on Theoretical Aspects of Computer Science (TACS 2001), volume 2215 of Lecture Notes in Computer Science, pages 219–235. Springer-Verlag, 2001.

### Separation Logic

### ???
```
P, Q ::=
    0            - spatial
    P | Q        - spatial
    !P           - spatial
    c!(P)        - behavioral
    c?(P)        - behavioral
```

### Links
  - 7. Caires, L., Cardelli, L.: A spatial logic for concurrency (part I). Inf. Comput. 186(2), 194–235 (2003)
  - 9. Cardelli, L., Gordon, A.D.: Anytime, Anywhere: Modal Logics for Mobile Ambients. POPL 2000, pp. 365–377 (2000)
