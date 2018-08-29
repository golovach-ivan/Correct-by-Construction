## Typestate

Whereas the type of an object specifies all operations that can be performed on the object, typestates identify subsets of these operations that can be performed on the object in particular abstract states. When an operation is applied to the object, the typestate of the object may change, thereby dynamically changing the object’s set of permitted operations.

Whereas the type of a data object determines the set of operations over permitted on the object, typestate determines the subset of these operations which is permitted in a particular context. Typestate tracking is a program analysis technique which enhances program reliability by detecting at compile-time syntactically legal but semantically undefined execution sequences. These include reading a variable before it has been initialized and dereferencing a pointer after the dynamic object has been deallocated.

Typestates were introduced by Strom and Yemini in 1986, who applied typestates as abstractions over the states of data structures to control the initialization of variables (with the two typestates "uninitialized" and "initialized").

Strom and Yemini observe that although unrestricted aliasing and concurrency make the static checking of typestates impossible.

### Links
- STROM, R. E., AND YEMINI, S. 1986. Typestate: A programming language concept for enhancing software reliability. IEEE Trans. Softw. Eng. SE-12, 1 (Jan.), 157-171
