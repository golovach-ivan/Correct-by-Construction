```
new const, take, takeData, iter, copy in {

  contract const(@val, dst) = {
    new ack in {
      dst!((val, *ack)) | for (_ <- ack) { const!(val, *dst) }
    }    
  } |
  
  contract take(src, dst, @count) = {
    if (count > 0) {
      for (@item <- src) {
        dst!(item) | take!(*src, *dst, count - 1)        
      }
    }
  } |

  contract takeData(src, dst, @count) = {
    if (count > 0) {
      for (@(val, ack) <- src) {
        @ack!(Nil) | 
        dst!(val) | 
        takeData!(*src, *dst, count - 1)
      }
    }
  } |

  contract iter(@init, op, dst) = {
    new ack in {
      dst!((init, *ack)) | for (_ <- ack) { 
        new opRet in {
          op!(init, *opRet) |
          for (@newInit <- opRet) {
            iter!(newInit, *op, *dst) 
          }
        }
      }
    }    
  } |

  contract copy(src, dst) = {
    for (@val <= src) {
      dst!(val)
    }
  } |

  // DEMO const + copy + take
  new s0, s1, s2 in {
    const!(0, *s0) |
    copy!(*s0, *s1) |    
    takeData!(*s1, *stdout, 10)
  }
}
```
