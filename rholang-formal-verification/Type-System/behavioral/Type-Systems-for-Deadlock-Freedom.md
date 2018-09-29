## ??? RUSSIAN ???
Интуитивно, уровень обязательств действия (the obligation level) означает степень необходимости исполняемого действия.  
Интуитивно, уровень возможностей действия (the capability level) означает степень гарантии успеха действия.

Предположим, что канал имеет the usage !<to,tc>.U. 
  - Его уровень обязательст действия 'to' означает, что процесс может реализовать возможности уровня меньше, чем 'to' прежде чем выполнить обязательство выполнить вывод на канале.
  - 

### ??? Rules
  - P.Q - какие требования к соотношению obligation/capability между P и Q?
  - P | Q - какие требования к соотношению obligation/capability между P и Q?
  - (P | Q).R - какие требования к соотношению obligation/capability между P, Q и R?

#### - P.Q - какие требования к соотношению obligation/capability между P и Q?
```P<a,b>.Q<c,d>``` => ```b < c```

## Type Systems for Deadlock-Freedom

#### Obligation level

Intuitively, the obligation level of an action denotes the degree of the necessity of the action being executed.

Its obligation level *to* means that a process can exercise capabilities of level less than *to* before fulfilling the obligation to perform an output on the channel.

If the obligation level is 0, the channel must be used for action immediately. 

If the obligation level is ∞, arbitrary actions can be performed before the channel is used for action (so, there is no guarantee that the channel is used for action at all).

#### Capability level
Intuitively, the capability level of an action denotes the degree of the guarantee for the success of the action.

The capability level *tc* means that the success of an output on the channel is guaranteed by a corresponding input action with an obligation level of less than or equal to *tc*. In other words, some process has an obligation of level less than or equal to *tc* to use the channel for input.

If the capability level is ∞, the success of the action is not guaranteed.

### Types

t (level) ::= ∞ | 0 | 1 | 2 | ...


Suppose that a channel has the usage !<to,tc>.U. Its obligation level 'to' means that a process can exercise capabilities of level less than 'to' before fulfilling the obligation to perform an output on the channel.


### Example: Lock
The usage of a lock is refined as !<0,∞> | ∗(?<∞,t>.!<t,∞>)

The part !<0,∞> means that a value must be put into the channel immediately (so as to simulate the unlocked state).  
The part ?<∞,t> means that any actions may be performed before acquiring the lock and that once a process tries to acquire the lock, the process can eventually acquire the lock.
The part !<t,∞> means that once a process has acquired the lock, it has an obligation of level t to release the lock. 



#### Links
  - Kobayashi N. (2003) Type Systems for Concurrent Programs. Lecture Notes in Computer Science, vol 2757. Springer, Berlin, Heidelberg.
  - N. Kobayashi. A partially deadlock-free typed process calculus. ACM Trans. Prog. Lang. Syst., 20(2):436–482, 1998.
  - N. Kobayashi, S. Saito, and E. Sumii. An implicitly-typed deadlock-free process calculus. In Proc. of CONCUR2000, volume 1877 of LNCS, pages 489–503. Springer-Verlag, August 2000.
  - E. Sumii and N. Kobayashi. A generalized deadlock-free process calculus. In Proc. of Workshop on High-Level Concurrent Language (HLCL’98), volume 16(3) of ENTCS, pages 55–77, 1998.
