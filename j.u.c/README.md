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

#### 1<-∃n: select
