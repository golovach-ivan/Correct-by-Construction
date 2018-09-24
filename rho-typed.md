```
new const, copy in {

  // type Ack = []#+1
  
  // const[T]: T * ((T*Ack)#-w)
  contract const(@val, dst) = {
    new ack in {
      dst!((val, *ack)) | for (_ <- ack) { const!(val, *dst) }
    }    
  } |
  

  // copy: ∀T()
  // copy[T]: (T#+8) * (T#-8)
  contract copy(src, dst) = {
    for (@val <= src) {
      dst!(val)
    }
  } |
  
  new s0, s1, dst in {
    const!(0, *s0) |
    const!("A", *s1) |
    contract f(a, b) = {
      for (@v <- a) {
        dst!(v) | f!(b, a)
      }
    }
  }
  
  // foo: (Int#+5 + String#+w)#+10
  contract foo(dst) = {
    new si in {
      new ss0 in {
        new ss1 in {
          new s2 in {
            const!(0,   *si) |
            const!("A", *ss0) |
            take!(*ss0, *ss1, 5) |
            copy!(*si, *s2) |    
            copy!(*ss1, *s2) |    
            takeData!(*s2, *dst, 10)
          }
        }
      }
    }  
  } |
  
  // type +Stream[T] = (T*Ack)#+-w
  // s0: (T*Ack)#-w
  new si in {
    new ss0 in {
      new ss1 in {
        new s2 in {
          const!(0,   *si) |
          const!("A", *ss0) |
          take!(*ss0, *ss1, 5) |
          copy!(*si, *s2) |    
          copy!(*ss1, *s2) |    
          takeData!(*s2, *stdout, 10)
        }
      }
    }
  }
}
```
