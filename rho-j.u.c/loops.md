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
>> 0
>> 1
>> 2
>> 3
>> 4
```

### B

### C
