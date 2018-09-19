```
new Cell, lock in {

  contract Cell(@init, get, set) = {  
    new State in {    
      State!(init) |
      contract get(ret) = {
        for (@value <- State) {      
          State!(value) | ret!(value)
        }
      } |
      contract set(@newValue, ack) = {
        for (_ <- State) {      
          State!(newValue) | ack!(Nil)
        }
      }      
    }
  } |
    
  lock!(Nil) |  
    
  new get, set, ack, x, y in {
    Cell!(10, *get, *set) | 
    
    for (_ <- lock) {
      new ret in {
        get!(*ret) | for (@val <- ret) {
          set!(val + 1, *ack) | for (_ <- ack) {lock!(Nil) | x!(Nil)}
        }
      } 
    }|
    
    for (_ <- lock) {
      new ret in {
        get!(*ret) | for (@val <- ret) {
          set!(val * 2, *ack) | for (_ <- ack) {lock!(Nil) | y!(Nil)}
        }
      } 
    }|
    
    new ret in {        
      for (_ <- x; _ <- y) {
        get!(*ret) | for (@val <- ret) { stdout!(val) }
      }
    }
    
  }    
}
```
