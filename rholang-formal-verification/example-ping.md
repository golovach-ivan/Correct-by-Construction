## Ping server

```
new ping in {
  // ping Server
  contract ping(callback) = { callback!([]) } |
  
  // ping Client
  new callback in {
    ping!(*callback) | for(@resp <- callback) {
      match resp {
        [] => stdout!("Ping - OK!")
      }      
    }  
  }
}

>> Ping - OK!
```
