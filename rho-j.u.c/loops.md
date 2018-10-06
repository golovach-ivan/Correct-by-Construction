## Loops

Loops used in demos. Unordered loop prints *0..4* in any order, ordered only consequentially (from 0 to 4).

- [Index set (unordered)](#index-set-unordered)    
- [Index variable (unordered)](#index-variable-unordered)    
- [Index variable (ordered)](#index-variable-ordered)    
- [Index array (unordered)](#index-array-unordered)    
- [Index array (ordered)](#index-array-ordered)    

### Index set (unordered)
```
new n in {
  n!(0) | n!(1) | n!(2) | n!(3) | n!(4) | 
  for (@i <= n) {
    stdout!(i)
  }
}
```

### Index variable (unordered)
```
new n in {
  n!(0) | for (@i <= n) {
    if (i < 5) {
      stdout!(i) | loopIndex!(i + 1)
    }
  }
}
```

### Index variable (ordered)
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

### Index array (unordered)
```
new n in {
  n!([0, 1, 2, 3, 4]) | for (@[i...tail] <= loopRange) { 
    stdout!(i) | n!(tail)    
  }
}
```

### Index array (ordered)
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
