## Type Systems for Rholang

example is the race freedom property, stating that at any time, nowhere in the system there are two output actions ready on the same channel.

There has been no satisfactorily general framework of type systems for concurrent programming languages: most type systems have been designed in a rather ad hoc manner for guaranteeing certain specific properties.

properties: race freedom, unique receptiveness [16], deadlock freedom
16. Sangiorgi, D.: The Name Discipline of Uniform Receptiveness. Theoretical Computer Science 221(1-2), 457–493 (1999)

### Behavioral Type Systems

Behavioural types describe the dynamic aspects of programs, in contrast to data types, which describe the fixed structure of data.

ФФФ
  - **typestate systems**, which specify state-dependent availability of operations; 
  - **choreographies**, which specify collective communication behaviour; 
  - and **behavioural contracts**.
  - [Linear Type Systems](behavioral/Linear-Type-Systems.md)
  - [Type Systems with Channel Usage](behavioral/Type-Systems-with-Channel-Usage.md)
  - [Type Systems for Deadlock Freedom](behavioral/Type-Systems-for-Deadlock-Freedom.md)
  - [Type Systems for Lock Freedom](behavioral/Type-Systems-for-Lock-Freedom.md)
  - [Type Systems for Termination](behavioral/Type-Systems-for-Termination.md)
  - [Session Types](behavioral/Session-Types.md)

Igarashi and Kobayashi’s behavioural type systems

behavioural type systems to simplify the analysis of concurrent message-passing programs [11,10,8].
11. Kobayashi, N.: Type-based information flow analysis for the pi-calculus. Acta Inf. 42(4-5), 291–347 (2005)
10. Igarashi, A., Kobayashi, N.: A generic type system for the Pi-calculus. Theor. Comput. Sci. 311(1-3), 121–163 (2004)
8. Chaki, S., Rajamani, S.K., Rehof, J.: Types as models: model checking message-passing programs. POPL’02, pp. 45–57 (2002)

### Spatial Type Systems



spatial logics [9,7]
9. Cardelli, L., Gordon, A.D.: Anytime, Anywhere: Modal Logics for Mobile Ambients. POPL 2000, pp. 365–377 (2000)
7. Caires, L., Cardelli, L.: A spatial logic for concurrency (part I). Inf. Comput. 186(2), 194–235 (2003)

### LADL
