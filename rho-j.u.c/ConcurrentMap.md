## java.util.concurrent.ConcurrentMap&lt;K,V&gt; in RhoLang

A Map providing thread safety and atomicity guarantees ([javadoc](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/ConcurrentMap.html)).

- [State / Operations Model](#state--operations-model)
- [Explanation](#explanation)
- [Complete source code (with demo)](#complete-source-code-with-demo)
- [Exercise](#exercise)

**java.util.concurrent.ConcurrentMap** (short version)   
```java
public interface ConcurrentMap<K, V> {

  V put(K key, V value);

  V get(Object key);

  V remove(Object key);
}

```

<details><summary><b>java.util.concurrent.ConcurrentMap</b> (long version)</summary><p>
  
```java
public interface ConcurrentMap<K, V> {

  // Returns true if this map contains a mapping for the specified key.
  boolean containsKey(Object key);

  // Returns a Set view of the mappings contained in this map.
  Set<Map.Entry<K,V>> entrySet();

  // Performs the given action for each entry in this map until all 
  // entries have been processed or the action throws an exception.
  default void forEach(BiConsumer<? super K,? super V> action) {...}

  // Returns the value to which the specified key is mapped, 
  // or null if this map contains no mapping for the key.
  V get(Object key);

  // Returns a Set view of the keys contained in this map.
  Set<K> keySet();

  // Associates the specified value with the specified key in this map (optional operation).
  V put(K key, V value);

  // Removes the mapping for a key from this map if it is present (optional operation).
  V remove(Object key);

  // Returns the number of key-value mappings in this map.
  int size();

  // Returns a Collection view of the values contained in this map.
  Collection<V> values();

  // Replaces each entry's value with the result of invoking the given function 
  // on that entry until all entries have been processed or the function throws an exception.
  default void replaceAll(BiFunction<? super K,? super V,? extends V> function) {...}
}
```
</p></details><br/>

### State / Operations Model
TBD

### Explanation
TBD

### Complete source code (with demo)
TBD

### Exercise
TBD
