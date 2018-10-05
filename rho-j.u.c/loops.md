## Loops

Loops used in demos. Unordered loop prints *0..4* in any order, ordered only consequentially (from 0 to 4).

- [Index set (unordered)](#index-set-unordered)    
- [Loop index (unordered)](#loop-index-unordered)    
- [Loop index (ordered)](#loop-index-ordered)    
- [Loop range (unordered)](#loop-range-unordered)    
- [Loop range (ordered)](#loop-range-ordered)    

### Index set (unordered)
```
new n in {
  n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | 
  for (@i <= n) {
    stdout!(i)
  }
}
```

### Loop index (unordered)
```
new n in {
  n!(0) | for (@i <= n) {
    if (i < 5) {
      stdout!(i) | loopIndex!(i + 1)
    }
  }
}
```

### Loop index (ordered)
```
new n in {
  n!(0) | for (@i <= n) {
    if (i < 5) {
      new ack in {
        stdoutAck!(i, *ack) | for (_ <- ack) {
          n!(i + 1)
        }
      }
    }
  }
}
```

### Loop range (unordered)
```
new n in {
  n!([0, 1, 2, 3, 4]) | for (@[i...tail] <= loopRange) { 
    stdout!(i) | n!(tail)    
  }
}
```

### Loop range (ordered)
```
new n in {
  n!([0, 1, 2, 3, 4]) | for (@[i...tail] <= loopRange) { 
    new ack in {
      stdoutAck!(i, *ack) | for (_ <- ack) {
        n!(tail)
      }
    }
  }
}
```
