## Events ordering in Rholang

### waitSet by-reference
**Incorrect**

```
                ---> ws!(*ack)
+--------------+
|ws <- stateRef| =>  stateRef!(*ws)  =>  ws <- stateRef  => new newWS in { stateRef!(*newWS) } => ws <- stateRef
+--------------+                                     
                                         for (ack <= ws) { ack!(Nil) } ---------------------------^
    
```

```
new waitSet in {
  stateRef!(*waitSet) } |
      
contract wait(ack) = {
  for (waitSet <- stateRef) {
    waitSet!(*ack) |
    stateRef!(*waitSet) } } |

contract notifyOp(_) = {
  for (waitSet <- stateRef) {
    for (ack <- waitSet) { ack!(Nil) } |
    stateRef!(*waitSet) } } |

contract notifyAll(_) = {
  for (waitSet <- stateRef) {
    for (ack <= waitSet) { ack!(Nil) } |
    new newWaitSet in { stateRef!(*newWaitSet) } } }  
```

**Correct**

```
stateRef!([]) |
      
contract await(ack) = {
  for (@waitSet <- stateRef) {
    stateRef!(waitSet ++ [*ack]) } } |
  
contract signalOp(_) = {
  for (@waitSet <- stateRef) {
    match waitSet {
      [head...tail] => { 
        stateRef!(tail) |
        @head!(Nil) }
      [] => 
        stateRef!(waitSet) } } } |  
  
contract signalAll(_) = {
  for (@waitSet <- stateRef) {
    stateRef!([]) |
    new notifyAll in {            
      notifyAll!(waitSet) |
      contract notifyAll(@[head...tail]) = { @head!(Nil) | notifyAll!(tail) } } } }            
```


