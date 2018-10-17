### Object/class sceleton
```new BlockingQueue in {``` - ???  
```contract BlockingQueue(@maxSize, putOp, takeOp)``` - ???   
```new stateRef in {``` - ???      

### Method signature
```contract putOp(_) = {``` - ???   
```contract putOp(@newElem) = {``` - ???   
```contract putOp(ack) = {``` - ???   
```contract putOp(ret) = {``` - ???   
```contract putOp(@newElem, ack) = {``` - ???   
```contract putOp(@newElem, ret) = {``` - ???   
```contract putOp(@newElem, @size, ack) = {``` - ???   
```contract putOp(@newElem, @size, ret) = {``` - ???   

### State read
```for (@state <- stateRef) {``` - ???   
```for (@arr, @size, chan <- stateRef) {``` - ???   
```for (@[head...tail] <- stateRef) {``` - ???   
```for (@true <- stateRef) {``` - ???   
```for (@[head...tail], @true <- stateRef) {``` - ???   

### State write
```stateRef!(state)``` - ???   
```stateRef!(arr, size)``` - ???   
```stateRef!(arr, arr.length() > 0)``` - ???   
```stateRef!(arr ++ [item])``` - ???   

### Return
```ack!(Nil)``` - Сonfirm the completion of the operation.   
```ret!(item)``` - Return ???   
