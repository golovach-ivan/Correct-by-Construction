## Loops

### A
```
new loopRange in {
  loopRange!([0, 1, 2, 3, 4]) | for (@[index...tail] <= loopRange) { 
    loopRange!(tail) |
    stdout!(index)
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

### B

### C
