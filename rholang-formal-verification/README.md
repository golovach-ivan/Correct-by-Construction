## RhoLang Formal Verification (proposals)

### Extended Type System

### SAT Solvers

### General Theorem Provers

### K Framework

### Bisimulation
Next contract are weak barbed bisimilar. 
```
contract spec(ack) = { ack!([]) }
```
```
contract impl(ack) = { new foo in { foo!(*ack) | for (val <- foo) { val!([]) } } }
```
**Good** 
  - спецификация пишется на том же языке (RhoLang), что и имплементация  
  
**Bad**  
  - bisimilarity подразумевает только полную спецификацию программы, написание эквивалентной программы
  - необходимо реализовать (или найти) инструмент, который реализует weak barbed bisimilarity
  - теория bisimilarity разработана для untyped process calculi (для RhoCalculi) и не включает работу с ground types (Int, String, Bool)

### Modal Logics (Namespace Logic)
