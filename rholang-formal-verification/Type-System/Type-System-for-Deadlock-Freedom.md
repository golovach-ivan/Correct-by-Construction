## A Type System for Deadlock-Freedom

#### Obligation level

Intuitively, the obligation level of an action denotes the degree of the necessity of the action being executed.

If the obligation level is 0, the channel must be used for action immediately. 

If the obligation level is ∞, arbitrary actions can be performed before the channel is used for action (so, there is no guarantee that the channel is used for action at all).

#### Capability level
Intuitively, the capability level of an action denotes the degree of the guarantee for the success of the action.



### Types

t (level) ::= ∞ | 0 | 1 | 2 | ...


Suppose that a channel has the usage !<to,tc>.U. Its obligation level 'to' means that a process can exercise capabilities of level less than 'to' before fulfilling the obligation to perform an output on the channel.


### Example: Lock
The usage of a lock is refined as !<0,∞> | ∗(?<∞,t>.!<t,∞>)

The part !<0,∞> means that a value must be put into the channel immediately (so as to simulate the unlocked state).  
The part ?<∞,t> means that any actions may be performed before acquiring the lock and that once a process tries to acquire the lock, the process can eventually acquire the lock.
The part !<t,∞> means that once a process has acquired the lock, it has an obligation of level t to release the lock. 



#### Links
[19, 24, 44].
