### You can block on any pattern
**structure pattern**  
block on linked list:   
item0 = [0, Nil],   
item1 = [1, item0],   
item2 = [2, item1]  
...  
```contract copy(src, dst) = for (@[item, next] <= src) { dst!(item) }```

**value pattern** 
block on linked list:   
itemA = (false, true,  1, (A, ())),   
itemB = (true,  true,  2, (B, itemA)),   
itemC = (true,  true,  3, (C, itemB)),   
itemD = (true,  false, 4, (D, itemC))

### There is no destinction between data and processes: Streams == Futures
Everething is Process or can be converted to Process. Every Process if Thread.  
Everething is Channel or can be converted to Channel. Every Channel if Stream.  
Everething can be throuth as Thread and Stream at the same time.

### Concurrency primitives

#### ∃n<-1: select, queue

#### ∀n<-1: wait
#### 1->∀n: notifyAll

#### 1<-∀n: join
```
new c0, c1, c2, c3 in {
  // send all
  c0!(0) | c1!(1) | c2!(2) | c3!(3) |
  
  // receive all
  for (@v0 <- c0; @v1 <- c1; @v2 <- c2; @v3 <- c3) {
    stdout!([v0, v1, v2, v3])
  }
}
```

```
new c0, c1, c2, c3 in {
  // send all
  c0!!(0) | c1!!(1) | c2!!(2) | c3!!(3) |
  
  // receive all N times
  new N in {
    N!(10) |
    for (@v0 <= c0; @v1 <= c1; @v2 <= c2; @v3 <= c3; @k <= N) {      
      if (k > 0) { N!(k - 1) } |
      stdout!([v0, v1, v2, v3])
    }
  }
}
```

```
new join in {
  contract join(seq, ret) = {
    match *seq {
      [head] => { for (@value <- @head) { ret!([value]) } }
      [head...tail] => {
        new retTail in {
          join!(tail, *retTail) | 
          for (@headItem <- @head; @tailItems <- retTail) {
            ret!([headItem] ++ tailItems)
          }
        }
      }
    }
  } |
  
  new c0, c1, c2, c3 in {
    // send all
    c0!(0) | c1!(1) | c2!(2) | c3!(3) |
  
    // receive all
    new ret in {
      join!([*c0, *c1, *c2, *c3], *ret) | for (@value <- ret) {
        stdout!(value)
      }
    }
  }
}
```

#### 1<-∃n: select
