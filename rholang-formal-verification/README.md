## RhoLang Formal Verification (proposals)

### Extended Type System
RhoCalculi is a async polyadic π-calculus. Для этого типа исчислений есть литература по внедрению различных систем типизации, согласованных именно для исчислений процессов.
- A Linear Typs
- A Session Types


#### Example: A Linear Type System
```T1``` - тип канала, не допускающий чтение и требующий еденичную запись процесса ```[]```.  
```T2``` - тип канала, допускающий произвольное количество чтений каналов типа ```T1``` и не допускающий запись.  
```
type T1 = [] \ (?0, !1)
type T2 = T1 \ (?inf, !0)

new ping: T2 in {
  contract ping(ret: T1) = { ret!([]) }
}
```
Такая спецификация отвергнет следующие реализации   
```contract ping(ret: T1) = { Nil }``` - не реализована единственная запись в канал ret.  
```contract ping(ret: T1) = { for (_ <- ret) {Nil} }``` - запрещенное чтение из канала ret.  
```contract ping(ret: T1) = { ret!([]) | ret!([]) }``` - множественные записи в канал ret.  
```contract ping(ret: T1) = { ret!(0) }``` - запись в канал ret неверного типа.  

#### Implementation
Реализовано может быть в виде
- расширенной срецификации типизированного языка (RhoLang Typed)
- typechecker for RhoLang Typed
- translator: RhoLang Typed to RhoLang

#### Proc and Cons
**Proc**  
- достаточно просто в реализации
- можно поддерживать множественные системы типов (Liveness/Safety, Security, ...) (TypeScript/CoffeScript for JavaScript) 
- можно типизировать/специфицировать произвольную часть кода
- легко изучается и внедряется программистом RhoLang

**Cons**  
- невозможно задать точную спецификацию программы
- невозможно выразить все требуемые свойства

### SAT Solvers
Возможно расширить язык механизмом asserts (require, ensure, ...)

```
new ping in {
  
  contract ping(ret) = { ret!([]) }
}
```

### General Theorem Provers

### K Framework

### Bisimulation
Next contract are weak barbed bisimilar (```impl ≈ spec```). 
```
contract spec(ack) = { ack!([]) }
contract impl(ack) = { new foo in { foo!(*ack) | for (val <- foo) { val!([]) } } }
```
**Good** 
  - спецификация пишется на том же языке (RhoLang), что и имплементация  
  
**Bad**  
  - bisimilarity подразумевает только полную спецификацию программы, написание эквивалентной программы
  - необходимо реализовать (или найти) инструмент, который реализует weak barbed bisimilarity
  - теория bisimilarity разработана для untyped process calculi (для RhoCalculi) и не включает работу с ground types (Int, String, Bool)

### Modal Logics (Namespace Logic)
