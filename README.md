# Correct-by-Construction
Tools for RHO-Lang smart contracts formal verification (with Namespace/Spatial/Hennessy-Milner Logics)

- [Labeled Transition System (LTS)](#labeled-transition-system-lts)
- [Calculus of Communication Systems (CCS)](#calculus-of-communication-systems-ccs)
- [Hennessy-Milner Logic (HML)](#hennessy-milner-logic-hml)

## Labeled Transition System (LTS)

### LST syntax

Core classes:
- net.golovach.verification.LTSLib.{LTS, LTSState, LTSAction, LTSEdge}

Base imports:
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
val s0 = LTSState("s", 0) // or
val s1: LTSState = 's1    // or
val s2: ⌾ = 's2          // or
```

#### Action syntax
```scala
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
val e0 = Edge('s0, τ, 's1) // standard syntax
val e1 = 's0 × τ ➝ 's1    // syntax sugar
```

#### LTS operations
Simple cyclic Coffee Machine definition
```scala
val lts = LTS(
  ports = ("$", "☕"),
  actions = ("$".↑, "☕".↓),
  states = ('s0, 's1),
  init = 's0,
  edges = Set(
    's0 × "$".↑ ➝ 's1,
    's1 × "☕".↓ ➝ 's0
  ))  
```

Print to console
```scala
println(dump(lts))
```
<details><summary>CONSOLE</summary>
<p>
  
```
>> ports:   {$, ☕}
>> actions: {↑$, ↓☕}
>> states:  {'s0, 's1}
>> init:    's0
>> edges:   
>>     's0 × ↑$ → 's1
>>     's1 × ↓☕ → 's0
```
</p>
</details><br/>

## Calculus of Communication Systems (CCS)

Base classes
- net.golovach.verification.ccs.CCSLib.Process
- net.golovach.verification.ccs.CCSLib.{∅, ⟲, Prefix, Sum, Par, Restriction, Renaming}

Process = ∅ | ⟲ | Prefix | Sum | Par | Restriction | Renaming

Base imports:
- import net.golovach.verification.LTSLib._
- import net.golovach.verification.ccs.CCSLib._



CM ≡ Coffee Machine

**1) Prefix and ∅ syntaxes with transformation to LTS**

Simple disposable (acyclic) Coffee Machine
```scala
val CM = ↑("$") :: ↓("☕") :: ∅
println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>
  
```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'s0, 's1, 's2}
>> init:    's2
>> edges:   
>>     's1 × ↓☕ → 's0
>>     's2 × ↑$ → 's1
```  
</p>
</details><br/>

Or states can be named explicitly
```scala
val CM = ↑("$") :: ↓("☕") :: ∅("CM")
```

<details><summary>CONSOLE</summary>
<p>

```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'CM0, 'CM1, 'CM2}
>> init:    'CM2
>> edges:   
>>     'CM1 × ↓☕ → 'CM0
>>     'CM2 × ↑$ → 'CM1
```
</p>
</details><br/>

**2) Loop/Recursion (⟲) syntax**

Simple cyclic Coffee Machine
```scala
val CM = ↑("$") :: ↓("☕") :: ⟲
println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>

```
>> ports:   {☕, $}
>> actions: {↓☕, ↑$}
>> states:  {'s0, 's1}
>> init:    's1
>> edges:   
>>     's0 × ↓☕ → 's1
>>     's1 × ↑$ → 's0
```
</p>
</details><br/>

Or states can be named explicitly
```scala
val CM = "$".↑ :: "☕".↓ :: ⟲("CM")
```

Or sequence with loop at the end
```scala
val LOOP = "$".↑ :: "☕".↓ :: ⟲
val CM = "init".↑ |: LOOP

println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>

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
</p>
</details><br/>

Or one-liner
```scala
val CM = "init".↑ |: ("$".↑ :: "☕".↓ :: ⟲)
```

Another sequence with loop at the end
```scala
val LOOP = "$".↑ :: "☕".↓ :: ⟲
val CM = "initA".↑ :: ("initB".↑ |: LOOP)

println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>

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
</p>
</details><br/>

Or one-liner
```scala
val CM = "initA".↑ :: ("initB".↑ |: ("$".↑ :: "☕".↓ :: ⟲))
```

**3) Alternative/Choise/Sum (+) syntax**
```scala
val CS = ("$".↓ :: "☕".↑ :: "✉".↓ :: ⟲) + ("☕".↑ :: "✉".↓ :: ⟲)
println(dump(toLTS(CS)))
```

<details><summary>CONSOLE</summary>
<p>

```
>> ports:   {✉, ☕, $}
>> actions: {↓✉, ↑☕, ↓$}
>> states:  {'s0, 's1, 's2, 's3}
>> init:    's2
>> edges:   
>>     's1 × ↑☕ → 's0
>>     's2 × ↓$ → 's1
>>     's0 × ↓✉ → 's2
>>     's3 × ↓✉ → 's2
>>     's2 × ↑☕ → 's3
```
</p>
</details><br/>

**4) Parallel Composition (|) syntax**

With silent τ-action generation

```scala
val A_IN  = "A".↑ :: ⟲
val A_OUT = "A".↓ :: ⟲

println(dump(toLTS(A_IN | A_OUT)))
```

<details><summary>CONSOLE</summary>
<p>

```
>> ports:   {A}
>> actions: {↑A, ↓A, τ}
>> states:  {'s0}
>> init:    's0
>> edges:   
>>     's0 × ↑A → 's0
>>     's0 × ↓A → 's0
>>     's0 × τ → 's0
```
</p>
</details><br/>

More complex example
```scala
val CM = "$".↑ :: "☕".↓ :: ⟲
val CS = "$".↓ :: "☕".↑ :: "✉".↓ :: ⟲

println(dump(toLTS(CM | CS)))
```

<details><summary>CONSOLE</summary>
<p>
 
```
>> ports:   {☕, $, ✉}
>> actions: {↓☕, ↑☕, ↓✉, τ, ↑$, ↓$}
>> states:  {'s0, 's1, 's2, 's3, 's4, 's5}
>> init:    's5
>> edges:   
>>     's1 × τ → 's3
>>     's3 × ↓✉ → 's5
>>     's3 × ↑$ → 's0
>>     's1 × ↑☕ → 's0
>>     's4 × ↑☕ → 's3
>>     's2 × ↓$ → 's1
>>     's2 × ↓☕ → 's5
>>     's5 × ↑$ → 's2
>>     's4 × ↑$ → 's1
>>     's0 × ↓☕ → 's3
>>     's5 × τ → 's1
>>     's5 × ↓$ → 's4
>>     's0 × ↓✉ → 's2
>>     's1 × ↓☕ → 's4
``` 
</p>
</details><br/>


**5) Restriction (|) syntax**
```scala
val CM = ("$".↑ :: "☕".↓ :: ⟲) \ "$"
println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>
  
```
>> ports:   {☕}
>> actions: {↓☕}
>> states:  {'s0, 's1}
>> init:    's1
>> edges:   
>>     's0 × ↓☕ → 's1
```  
</p>
</details><br/>

Or
```scala
val CM = ("$".↑ :: "☕".↓ :: ⟲) \ "$" \ "✉"
```

Or
```scala
val CM = ("$".↑ :: "☕".↓ :: ⟲) \ ("$", "✉")
```

**6) Rename (∘) syntax**
```scala
val CM = ("$".↑ :: "☕".↓ :: ⟲) ∘ ("$" -> "A")
println(dump(toLTS(CM)))
```

<details><summary>CONSOLE</summary>
<p>

```
>> ports:   {☕, A}
>> actions: {↓☕, ↑A}
>> states:  {'s0, 's1}
>> init:    's1
>> edges:   
>>     's0 × ↓☕ → 's1
>>     's1 × ↑A → 's0
```
</p>
</details><br/>

Or
```scala
val CM = ("$".↑ :: "☕".↓ :: ⟲) ∘ ("$" -> "A", "☕" -> "B")
```

## Strong/Weak Bisimulation

## Hennessy-Milner Logic (HML)
  

