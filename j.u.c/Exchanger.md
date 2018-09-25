## java.util.concurrent.Exchanger

[Exchanger](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Exchanger.html) - a synchronization point at which threads can pair and swap elements within pairs.

Usage example:
```java
import java.util.concurrent.*;
import static java.util.concurrent.ForkJoinPool.commonPool;

public class Demo {
    static int N = 6;
    static Exchanger<Object> exchanger = new Exchanger<>();
    static CountDownLatch latch = new CountDownLatch(N);

    public static void main(String[] args) throws Exception {
        for (int my = 0; my < N; my++) {
            exchange(my);
        }
        latch.await();
    }

    private static void exchange(Object my) throws Exception {
        commonPool().execute(() -> {
            try {
                Object other = exchanger.exchange(my);
                System.out.println(my + " -> " + other);
                latch.countDown();
            } catch (InterruptedException e) {/*NOP*/}
        });
    }
}

>> 1 -> 0
>> 0 -> 1
>> 2 -> 3
>> 5 -> 4
>> 4 -> 5
>> 3 -> 2
```

```Exchanger``` на RhoLang может быть реализован следующим образом
```
 1  new Exchanger in {  
 2    contract Exchanger(input) = {
 3      new storage in {
 4        storage!([]) |                         
 5        for (@xItem, @xRet <= input) {
 6          for (@maybePair <- storage) {
 7            match maybePair {
 8              [] =>                            
 9                storage!([xItem, xRet])        
10              [yItem, yRet] => {               
11                storage!([]) |                 
12                @yRet!(xItem) | @xRet!(yItem) 
13              } 
14            }
15          }
16        }
17      }
18    } 
```  
  1-2. Объявление контракта ```Exchanger```, который будет получать аргументы и возвращать значения через канал ```input```. Клиет будет помещать в ```input``` лист из пары ```[item, ret]```, где ```item``` - это елемент, а ```ret``` - канал, по которому вернут результат обмена.  
  3-4. Создаем приватный канал ```storage```, в котором будем хранить ```[item, ret]```, которым пока нет пары. Инициализируем пустым листом ```[]```.    
  5-6. В бесконечном цикле каждому входящему елементу сопоставляем состояние ```storage```.  
  8-9. Если в ```storage``` не было елементов для обмена, то сохраняем входящий элемент в ```storage```.  
  10-12. Если в ```storage``` был елемент для обмена, то совершаем обмен и сохраняем в ```storage``` пустой список ```[]```.  

Пример использования
```
 1  new Exchanger, exchange in {
 2    
 3    contract Exchanger(input) = {...} |
 4  
 5    contract exchange(@my, input) = {
 6      new ret in {
 7        input!(my, *ret) | for (@other <- ret) { 
 8          stdout!([my, other]) 
 9        }
10      }
11    } |
12  
13    // Demo  
14    new input, N in {
15      Exchanger!(*input) |
16      N!(0) |
17      for (@my <= N) {
18        if (my < 6) {
19          exchange!(my, *input) | N!(my + 1)
20        }
21      }
22    }
23  }

>> [2, 0]
>> [5, 1]
>> [0, 2]
>> [1, 5]
>> [4, 3]
>> [3, 4]
```

Этот код эквивалентен следующему

```empty --> [] --> empty --> [xItem, xRet] --> empty --> [] --> empty -->  [xItem, xRet] --> empty --> [] -->  ...``` 

```[] --> [xItem, xRet] --> [] --> [xItem, xRet] --> [] -->  ...``` 

```java
import static java.util.concurrent.ForkJoinPool.commonPool;

public class Exchanger {
    private Channel storage = new Channel();

    public Exchanger(Channel input) throws InterruptedException {
        this.storage.put(new Object[0]);

        commonPool().execute(() -> {
            while (true) {
                try {
                    Object[] x = (Object[]) input.take();
                    Object xItem = x[0];
                    Channel xRet = (Channel) x[1];

                    Object[] maybePair = (Object[]) storage.take();
                    if (maybePair.length == 0) {
                        storage.put(x);
                    } else {
                        Object yItem = maybePair[0];
                        Channel yRet = (Channel) maybePair[1];
                        storage.put(new Object[0]);
                        xRet.put(yItem);
                        yRet.put(xItem);
                    }
                } catch (InterruptedException e) {/*NOP*/}
            }
        });
    }
}
```

```
new Exchanger in {
  
  contract Exchanger(input) = {
    new storage in {
      storage!([]) |                         
      for (@xItem, @xRet <= input) {
        for (@maybePair <- storage) {
          match maybePair {
            [] =>                            
              storage!([xItem, xRet])        
            [yItem, yRet] => {               
              storage!([]) |                 
              @yRet!(xItem) | @xRet!(yItem) 
            } 
          }
        }
      }
    }
  } |

  // Demo
  new exchange in {
    Exchanger!(*exchange) |
    new ret in {exchange!(0, *ret) | for (@other <- ret) {stdout!([0, other])}} |
    new ret in {exchange!(1, *ret) | for (@other <- ret) {stdout!([1, other])}} |
    new ret in {exchange!(2, *ret) | for (@other <- ret) {stdout!([2, other])}} |
    new ret in {exchange!(3, *ret) | for (@other <- ret) {stdout!([3, other])}} |
    new ret in {exchange!(4, *ret) | for (@other <- ret) {stdout!([4, other])}} |
    new ret in {exchange!(5, *ret) | for (@other <- ret) {stdout!([5, other])}}
  }
}

>> [1, 5]
>> [5, 1]
>> [0, 3]
>> [2, 4]
>> [3, 0]
>> [4, 2]
```

Каждое обращение к контракту Exchanger создает замыкание включающее аргумент контракта ```input``` и новый приватный канал ```storage```.
Каждое обращение к конструктору Exchanger создает экземпляр класса включающий аргумент конструктора ```input``` и приватное поле ```storage```.
```
new Exchanger in {                      ~           public class Exchanger {
  contract Exchanger(input) = {         ~             public Exchanger(Channel input) throws InterruptedException {...}
    new storage in {                    ~             private Channel storage = new Channel();
      ...                               ~             ...
    }                                   ~             ...
  }                                     ~             ...
}                                       ~           }
```

Создание каждого замыкание включает инициализацию приватного канала ```storage``` пустым сообщнием. Создание каждого экземпляра класса включает инициализацию приватного поля ```storage``` пустым сообщением.
```
storage!([])                             ~           this.storage.put(new Object[0]);
```

Исполнение контракта представляет собой неограниченной чтение из канала ```input``` в пару переменных ```xItem``` и ```xRet```. Создание экземпляра включает создание потока, неограниченно читающего из очереди ```input```.
```
for (@xItem, @xRet <= input) {           ~           commonPool().execute(() -> {
                                         ~             while (true) {
                                         ~               Object[] x = (Object[]) input.take();
                                         ~               Object xItem = x[0];
                                         ~               Channel xRet = (Channel) x[1];                                        
  ...                                    ~               ...
                                         ~             }
}                                        ~           }
```

```
for (@maybePair <- storage) {            ~           Object[] maybePair = (Object[]) storage.take();
  match maybePair {                      ~           
    [] =>                                ~           if (maybePair.length == 0) {
      ...                                ~             ... 
    [yItem, yRet] => {                   ~           } else if (maybePair.length == 2) {              
      ...                                ~             Object yItem = maybePair[0];
      ...                                ~             Channel yRet = (Channel) maybePair[1];
      ...                                ~             ...
    }                                    ~           }
  }
}
```

```
storage!([xItem, xRet])                  ~           storage.put(x);
```

```
storage!([]) |                           ~           storage.put(new Object[0]);   
@yRet!(xItem) | @xRet!(yItem)            ~           xRet.put(yItem); yRet.put(xItem);
```
