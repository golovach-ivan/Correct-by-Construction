### Examples
#### ∃p ∊ Bool: p ∧ ¬p = true  
ans: no  
```
(declare-const p Bool)
(assert (and p (not p)))
(check-sat)

>> unsat
```

#### ∃p ∊ Bool, ∃q ∊ Bool: p ∧ ¬q = true  
ans: yes, p = true, q = false

```
(declare-const p Bool)
(declare-const q Bool)
(assert (and p (not q)))
(check-sat)
(get-model)

>> sat
>> (model 
>>   (define-fun q () Bool
>>     false)
>>   (define-fun p () Bool
>>     true)
>> )
```

### Links
- [github.com/Z3Prover](https://github.com/Z3Prover/z3)
- [Z3 publications (50+)](https://github.com/Z3Prover/z3/wiki/Publications)
- [Try Z3 online](https://rise4fun.com/z3)
