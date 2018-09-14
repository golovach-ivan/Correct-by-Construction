### Current types/literals in RhoLang

Типы - есть, type literals - нет.

**Ground/Simple types:**
- Bool: true, false
- Int: ... -2, -1, 0, 1, 2, ...
- String: "Hello World!"
- Uri: ???
- ByteArray: ???

**Collection types:**
- List: [0, true, "hello"]
- Tuple: (0, true, "hello")
- Set: Set(0, true, "hello")
- Map: {0: true, "hello": (0, 1)}


### Formulation of the problem

```
contract ping(ret) = { ret!([]) }
```

```
new ping in {
  contract ping(ret) = { ret!([]) } | 
  
  new ret in {
    ping!(*ret) | for(_ <- ret) {stdout!("Ping - OK")}  
  }
}

>> 
```

### Errors
- **A**: write instance of another type: ```contract ping(ret) = { ret!(0) }```
- **B**: write to another channel: ```contract ping(ret) = { ping!(0) }```
- **C**: write instance of another type: ```contract ping(ret) = { ret!(0) }```

- стартовать контракт на ret: ```contract ping(ret) = { ret!([]) | contract ret(_) = {Nil} }```
- стартовать каждый раз новый контракт на новом приватном канале: ```contract ping(ret) = { ret!([]) | new x in {contract x(_) = {Nil} }}```

#### A Simple Type System
```
type R = [] chan
type P = [R] chan

new ping: P in {
  contract ping(ret: R) = { ret!([]) }
}
```

- S. J. Gay. A sort inference algorithm for the polyadic π-calculus. In Proc. of POPL, pages 429–438, 1993
- V. T. Vasconcelos and K. Honda. Principal typing schemes in a polyadic π-calculus. In CONCUR’93, volume 715 of LNCS, pages 524–538. Springer-Verlag, 1993

#### A Type System with Input/Output Modes
```
type T0 = [[] chan!] chan?
type T1 = [] chan!

new ping: T0 in {
  contract ping(ret: T1) = { ret!([]) }
}
```

#### A Linear Type System

#### A Type System with Channel Usage
