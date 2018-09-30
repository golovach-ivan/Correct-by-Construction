
### Explicit Executor
```
new Executor in {

  contract Executor(taskStream) = {
    for (@task <= taskStream) { task }
  } |
  
  new taskQueue in {
    // create Executor
    Executor!(*taskQueue)|
    
    // send tasks
    taskQueue!(stdout!(0)) |
    taskQueue!(taskQueue!(stdout!(1))) |    
    taskQueue!(new q in {Executor!(*q) | q!(stdout!(2))})
  }
}
```

### Implicit Executor (higher-order processes)
```
new taskQueue in {
  // listen and run
  for (@task <= taskQueue) { task }|
  
  // send tasks
  taskQueue!(stdout!(0)) |
  taskQueue!(taskQueue!(stdout!(1))) |    
  taskQueue!(new q in {for (@t <= q) { t } | q!(stdout!(2))})
}
```
