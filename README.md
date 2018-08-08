# Correct-by-Construction
Tools for RHO-Lang smart contracts formal verification (with Namespace/Spatial/Hennessy-Milner Logics)

## Labeled Transition System (LTS)

### LST syntax

Core classes:
- net.golovach.verification.LTSLib.{LTS, LTSState, LTSAction, LTSEdge}

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
val a_in = ↑("a") // or
val b_in = "b".↑  // or

// Output actions
val a_out  = ↓("a") // or
val b_out = "b".↓   // or

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

Base classes
- net.golovach.verification.ccs.CCSLib
- net.golovach.verification.ccs.CCSLib.Process
- net.golovach.verification.ccs.CCSLib.{∅, ⟲, Prefix, Sum, Par, Restriction, Renaming}

Process = ∅ | ⟲ | Prefix | Sum | Par | Restriction | Renaming

CM ≡ Coffee Machine

**1) Prefix and ∅ syntaxes with transformation to LTS**
Simple disposable (acyclic) Coffee Machine
```scala
import LTSLib._
import CCSLib._

val CM = ↑("$") :: ↓("☕") :: ∅
println(dump(toLTS(CM)))
```
```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'s0, 's1, 's2}
>> init:    's2
>> edges:   
>>     's1 × ↓☕ → 's0
>>     's2 × ↑$ → 's1
```

Or states can be named explicitly
```scala
val CM = ↑("$") :: ↓("☕") :: ∅("CM")
```
```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'CM0, 'CM1, 'CM2}
>> init:    'CM2
>> edges:   
>>     'CM1 × ↓☕ → 'CM0
>>     'CM2 × ↑$ → 'CM1
```

**2) Loop/Recursion (⟲) syntax**
Simple cyclic Coffee Machine
```scala
import LTSLib._
import CCSLib._

val CM = ↑("$") :: ↓("☕") :: ⟲
println(dump(toLTS(CM)))
```
```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'s0, 's1}
>> init:    's1
>> edges:   
>>     's0 × ↓☕ → 's1
>>     's1 × ↑$ → 's0
```

Or states can be named explicitly
```scala
val CM = "$".↑ :: "☕".↓ :: ⟲("CM")
```

Or sequence with loop at the end
```scala
import LTSLib._
import CCSLib._

val LOOP = "$".↑ :: "☕".↓ :: ⟲
val CM = "init".↑ |: LOOP

println(dump(toLTS(CM)))
```
```
>> ports:   {☕, $, init}
>> actions: {↓☕, ↑$, ↑init}
>> states:  {'s0, 's1, 's2}
>> init:    's2
>> edges:   
>>     's2 × ↑init → 's1
>>     's1 × ↑$ → 's0
>>     's0 × ↓☕ → 's1
```

Or one-liner
```scala
val CM = "init".↑ |: ("$".↑ :: "☕".↓ :: ⟲)
```

Another sequence with loop at the end
```scala
import LTSLib._
import CCSLib._

val LOOP = "$".↑ :: "☕".↓ :: ⟲
val CM = "initA".↑ :: ("initB".↑ |: LOOP)

println(dump(toLTS(CM)))
```
```
>> ports:   {☕, $, initB, initA}
>> actions: {↓☕, ↑$, ↑initB, ↑initA}
>> states:  {'s0, 's1, 's2, 's3}
>> init:    's3
>> edges:   
>>     's3 × ↑initA → 's2
>>     's2 × ↑initB → 's1
>>     's1 × ↑$ → 's0
>>     's0 × ↓☕ → 's1
```

Or one-liner
```scala
val CM = "initA".↑ :: ("initB".↑ |: ("$".↑ :: "☕".↓ :: ⟲))
```



## Hennessy-Milner Logic (HML)
