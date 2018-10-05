## Loops

In sources (mostly in demos) udes loops.

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
