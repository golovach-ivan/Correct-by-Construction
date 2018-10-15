## RhoLang conditionals / branches

### Value branches

#### Boolean operators

#### ????

### Pattern branches

#### match ???

#### for ???

lazy evaluating: if/else = proc not value   

```
stateRef!(if (x > 0) x else 0)
```
!=
```
if (x > 0) 
  stateRef!(x) 
else 
  stateRef!(0)
```
