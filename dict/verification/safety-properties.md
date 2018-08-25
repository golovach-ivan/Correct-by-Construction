## Safety properties

all reachable states verify φ

A maximal fixpoint formula, in effect, expresses a safety property. 

The crucial safety property of a mutual exclusion algorithm is that no two processes are ever in their critical sections at the same time.

Always F     = minfix(X)(F & \[Act\]X)
