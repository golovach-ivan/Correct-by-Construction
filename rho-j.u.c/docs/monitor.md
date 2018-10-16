## Monitor (sync primitive)

A monitor consists of a **mutex** (lock) object and one or more **condition variables**. A condition variable is basically a container of threads that are waiting for a certain condition. There are two main operations on condition variables
- wait / await
- notify / signal
- notifyAll / signalAll / broadcast

Conditional variable methods should be called only ???when owned mutex.  

### In java
#### Implicit monitor (synchronized / Object.wait()/.notify()/.notifyAll())
#### java.util.concurrenct.locks.{Lock, Condition}

#### ??? Every conditional variable must be associated with some mutex?

