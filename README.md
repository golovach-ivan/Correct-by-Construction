# Correct-by-Construction
Tools for RHO-Lang smart contracts formal verification (with Namespace/Spatial/Hennessy-Milner Logics)

## Labeled Transition System (LTS)

### LST syntax

Base classes:
- net.golovach.verification.LTSLib.LTSState
- net.golovach.verification.LTSLib.LTSAction
- net.golovach.verification.LTSLib.LTSEdge
- net.golovach.verification.LTSLib.LTS

Base import:
- import net.golovach.verification.LTSLib._

Nota bene:
- scala.Symbol - for LTSState (implicit conversion for syntax sugar)
- scala.String - for Action (implicit conversion for syntax sugar)

Type aliases
```scala
type ⌾ = LTSState
type ⒜ = LTSAction
type ➝ = Edge
```

#### State syntax
```scala
import LTSLib._

val s0 = LTSState("s", 0) // or
val s1: LTSState = 's1    // or
val s2: ⌾ = 's2          // or
```

#### Action syntax
```scala
import LTSLib._

// Input actions
val a_in = ↑("a")
val b_in = "b".↑

// Output actions
val a_out  = ↓("a")
val b_out = "b".↓

// Silent action
val tau = τ

// Checking are there annihilated actions
val x: Boolean = a_in ⇅ a_out // true
```

#### Edge syntax
```scala
import LTSLib._ 

val e0 = Edge('s0, τ, 's1) // standard syntax
val e1 = 's0 × τ ➝ 's1    // syntax sugar
```

#### LTS operations
Simple cyclic Coffee Machine definition
```scala
import LTSLib._ 

val $ = "$"
val ☕ = "☕"

val lts = LTS(
  ports = ($, ☕),
  actions = (↑($), ↓(☕)),
  states = ('s0, 's1),
  init = 's0,
  edges = Set(
    's0 × ↑($) ➝ 's1,
    's1 × ↓(☕) ➝ 's0
  ))  
```

Print to console
```scala
import LTSLib._ 

println(dump(lts))
```
```
>> ports:   {$, ☕}
>> actions: {↑$, ↓☕}
>> states:  {'s0, 's1}
>> init:    's0
>> edges:   
>>     's0 × ↑$ → 's1
>>     's1 × ↓☕ → 's0
```

## Calculus of Communication Systems (CCS)

## Hennessy-Milner Logic (HML)
