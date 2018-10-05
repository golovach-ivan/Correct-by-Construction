## Loops

### Index set
```
new indexSet in {
  indexSet!(0) | indexSet!(1) | indexSet!(2) |
  for (@index <= indexSet) {
    stdout!(index)
  }
}
```

### Loop index
```
new loopIndex in {
  loopIndex!(0) | for (@index <= loopIndex) {
    if (index < 5) {
      stdout!(index) | loopIndex!(index + 1)
    }
  }
}
```
```
new loopIndex in {
  loopIndex!(0) | for (@index <= loopIndex) {
    if (index < 5) {
      new ack in {
        stdoutAck!(index, *ack) | for (_ <- ack) {
          loopIndex!(index + 1)
        }
      }
    }
  }
}
```

### ???
```
new loopRange in {
  loopRange!([0, 1, 2, 3, 4]) | for (@[index...tail] <= loopRange) { 
    stdout!(index) | loopRange!(tail)    
  }
}
```
```
new loopRange in {
  loopRange!([0, 1, 2, 3, 4]) | for (@[index...tail] <= loopRange) { 
    new ack in {
      stdoutAck!(index, *ack) | for (_ <- ack) {
        loopRange!(tail)
      }
    }
  }
}
```

```
>> 0
>> 1
>> 2
>> 3
>> 4
```
