## Bit counting for 32-bit words

Check correctness of [this algorithm](https://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel) from Internet.
```
unsigned int v; // count bits set in this (32-bit value)
unsigned int c; // store the total here

c = v - ((v >> 1) & 0x55555555);
c = ((c >> 2) & 0x33333333) + (c & 0x33333333);
c = ((c >> 4)  + c) & 0x0F0F0F0F;
c = ((c >> 8)  + c) & 0x00FF00FF;
c = ((c >> 16) + c) & 0x0000FFFF;
```

Rewrite it to SMT format
```
(define-fun impl ((x (_ BitVec 32))) (_ BitVec 32)
  (let ((result (bvsub x (bvand ((_ rotate_right 1) x) #x55555555))))    
  (let ((result (bvadd (bvand ((_ rotate_right  2) result) #x33333333) (bvand result #x33333333))))    
  (let ((result (bvand (bvadd ((_ rotate_right  4) result) result) #x0F0F0F0F)))
  (let ((result (bvand (bvadd ((_ rotate_right  8) result) result) #x00FF00FF)))  
  (let ((result (bvand (bvadd ((_ rotate_right 16) result) result) #x0000FFFF)))    
    result))))))  
```

Implement specification (slow but correct version)
```
(define-fun spec ((x (_ BitVec 32))) (_ BitVec 32)
  (bvadd 
   (ite (= ((_ extract 31 31) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 30 30) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 29 29) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 28 28) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 27 27) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 26 26) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 25 25) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 24 24) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 23 23) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 22 22) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 21 21) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 20 20) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 19 19) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 18 18) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 17 17) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 16 16) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 15 15) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 14 14) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 13 13) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 12 12) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 11 11) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 10 10) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  9  9) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  8  8) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  7  7) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  6  6) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  5  5) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  4  4) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  3  3) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  2  2) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  1  1) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  0  0) x) #b1) #x00000001 #x00000000)))
```

Full sources
```
(define-fun spec ((x (_ BitVec 32))) (_ BitVec 32)
  (bvadd 
   (ite (= ((_ extract 31 31) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 30 30) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 29 29) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 28 28) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 27 27) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 26 26) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 25 25) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 24 24) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 23 23) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 22 22) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 21 21) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 20 20) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 19 19) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 18 18) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 17 17) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 16 16) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 15 15) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 14 14) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 13 13) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 12 12) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 11 11) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract 10 10) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  9  9) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  8  8) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  7  7) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  6  6) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  5  5) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  4  4) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  3  3) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  2  2) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  1  1) x) #b1) #x00000001 #x00000000)
   (ite (= ((_ extract  0  0) x) #b1) #x00000001 #x00000000)))

; https://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetParallel
; c = v - ((v >> 1) & 0x55555555);
; c = ((c >> 2) & 0x33333333) + (c & 0x33333333);
; c = ((c >> 4)  + c) & 0x0F0F0F0F;
; c = ((c >> 8)  + c) & 0x00FF00FF;
; c = ((c >> 16) + c) & 0x0000FFFF;
(define-fun impl ((x (_ BitVec 32))) (_ BitVec 32)
  (let ((result (bvsub x (bvand ((_ rotate_right 1) x) #x55555555))))    
  (let ((result (bvadd (bvand ((_ rotate_right  2) result) #x33333333) (bvand result #x33333333))))    
  (let ((result (bvand (bvadd ((_ rotate_right  4) result) result) #x0F0F0F0F)))
  (let ((result (bvand (bvadd ((_ rotate_right  8) result) result) #x00FF00FF)))  
  (let ((result (bvand (bvadd ((_ rotate_right 16) result) result) #x0000FFFF)))    
    result))))))   

(declare-fun badVal () (_ BitVec 32))

(assert (not (= (impl badVal) (spec badVal))))
(check-sat)
(get-model)

; (eval (spec #x00afe0ab))
; (eval (impl #x00afe0ab))
```
