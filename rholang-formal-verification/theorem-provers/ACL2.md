## ACL2

ACL2 (A Computational Logic for Applicative Common Lisp) is a software system consisting of 
- a programming language, 
- an extensible theory in a first-order logic, and 
- an automated theorem prover.

Industrial users of ACL2 include AMD, Centaur Technology, IBM, Intel, Oracle, and Rockwell Collins.

### A programming language
The ACL2 programming language is an applicative (side-effect free) variant of Common Lisp. ACL2 is untyped. All ACL2 functions are total — that is, every function maps each object in the ACL2 universe to another object in its universe.

### An extensible theory in a first-order logic

### An automated theorem prover

The core of ACL2's theorem prover is based on term rewriting. The prover is an "industrial strength" version of the Boyer-Moore theorem prover, Nqthm.

### Example
Short version of [Walking Course](http://www.cs.utexas.edu/users/moore/acl2/v8-1/combined-manual/index.html?topic=ACL2____An_02Example_02of_02ACL2_02in_02Use)

Lets send this def to ACL2
```
(defun app (x y) 
  (cond ((endp x) y) 
        (t (cons (car x)  
                 (app (cdr x) y))))) 
```  

See on result
```
>> The admission of APP is trivial, using the relation O< (which 
>> is known to be well-founded on the domain recognized by O-P) 
>> and the measure (ACL2-COUNT X).  We observe that the type of APP is 
>> described by the theorem (OR (CONSP (APP X Y)) (EQUAL (APP X Y) Y)). 
>> We used primitive type reasoning. 
```

This little example shows you what the syntax looks like and is a very typical successful interaction with the definitional principle.

Evaluating App on Sample Input
```
ACL2 !>(app nil '(x y z)) 
(X Y Z) 
 
ACL2 !>(app '(1 2 3) '(4 5 6 7)) 
(1 2 3 4 5 6 7) 
 
ACL2 !>(app '(a b c d e f g) '(x y z))   ; click here for an explanation 
(A B C D E F G X Y Z) 
 
ACL2 !>(app (app '(1 2) '(3 4)) '(5 6)) 
(1 2 3 4 5 6) 
 
ACL2 !>(app '(1 2) (app '(3 4) '(5 6))) 
(1 2 3 4 5 6) 
```

Build ???гипотезу.  
Observe that, for the particular a, b, and c above, (app (app a b) c) returns the same thing as (app a (app b c)). **Perhaps app is associative**.

The Theorem that App is Associative
```
ACL2!>(defthm associativity-of-app 
        (equal (app (app a b) c) 
               (app a (app b c)))) 
```

Its talk to you
```
Name the formula above *1. 
 
Perhaps we can prove *1 by induction.  Three induction schemes are 
suggested by this conjecture.  Subsumption reduces that number to two. 
However, one of these is flawed and so we are left with one viable 
candidate. 
 
We will induct according to a scheme suggested by (APP A B).  If we 
let  (:P A B C) denote *1 above then the induction scheme we'll use 
is 
(AND 
   (IMPLIES (AND (NOT (ENDP A)) 
                 (:P (CDR A) B C)) 
            (:P A B C)) 
   (IMPLIES (ENDP A) (:P A B C))). 
This induction is justified by the same argument used to admit APP, 
namely, the measure (ACL2-COUNT A) is decreasing according to the relation 
O< (which is known to be well-founded on the domain recognized 
by O-P).  When applied to the goal at hand the above induction 
scheme produces the following two nontautological subgoals. 
```

and summary
```
Summary 
Form:  ( DEFTHM ASSOCIATIVITY-OF-APP ...) 
Rules: ((:REWRITE CDR-CONS) 
        (:REWRITE CAR-CONS) 
        (:DEFINITION NOT) 
        (:DEFINITION ENDP) 
        (:FAKE-RUNE-FOR-TYPE-SET NIL) 
        (:DEFINITION APP)) 
Warnings:  None 
Time:  0.27 seconds (prove: 0.10, print: 0.05, other: 0.12) 
 ASSOCIATIVITY-OF-APP 
```
