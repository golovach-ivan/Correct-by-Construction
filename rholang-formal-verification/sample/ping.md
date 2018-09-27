## Ping Server

```contract ping(ret) = { ret!(Nil) }```  

**Good**  
```
contract ping(ret) = { 
  new foo in {
    foo!(ret) | for (bar <- foo) {
      bar!(Nil)
    }
  } 
}
```  

**Bad**   
```contract ping(ret) = { Nil }``` - не реализована единственная запись в канал ret.  
```contract ping(ret) = { for (_ <- ret) {Nil} }``` - запрещенное чтение из канала ret.  
```contract ping(ret) = { ret!([]) | ret!([]) }``` - множественные записи в канал ret.  
```contract ping(ret) = { ret!(0) }``` - запись в канал ret неверного типа.  
```contract ping(ret) = { ping!(ret) }``` - direct unbounded recursion.  
```
contract ping(ret) = { 
  new foo, bar in {
    contract foo(x) = { bar!(x) } |
    contract bar(x) = { foo!(x) } |
    foo!(ret) 
  }
}
```
indirrect mutually recursive unbounded recursion.  

```
contract ping(ret) = {
  new foo, bar in {
    for (_ <- foo) { 
      bar!(Nil) 
    } |
    for (_ <- bar) { 
      foo!(Nil) | 
      ret!(Nil) 
    }
  }
}
```
deadlock
