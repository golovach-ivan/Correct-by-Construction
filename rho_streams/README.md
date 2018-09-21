```
new const, take, iter, copy in {

  contract const(@val, dst) = {
    new ack in {
      dst!((val, *ack)) | for (_ <- ack) { const!(val, *dst) }
    }    
  } |
  
  contract take(src, dst, @count) = {
    if (count > 0) {
      for (@(val, ack) <- src) {
        @ack!(Nil) |
        new newAck in { 
          dst!((val, *newAck)) | for (_ <- newAck) {
            take!(*src, *dst, count - 1)
          } 
        }
      }
    }
  } |

  // DEMO const + take
  new stream in {
    const!(0, *stream) |
    new dst in {
      take!(*stream, *dst, 10) | for (@(val, ack) <= dst) {
        stdout!(val) | @ack!(Nil)
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
    for (@val <- src) {
      new ack in {
        dst!((val, *ack)) | for (_ <- ack) { copy!(*src, *dst) }
      }
    }
  }
}
```
