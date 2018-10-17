### Java Monitors in RhoLang

Всякий concurrent programming language нуждается в библиотеке concurrency примитивов для частичного или полного упорядочения событий.

Java имеет 
- встроенный механизм: JMM, volatile, synchronized and implicit monitors (Object.wait/notify/notifyAll)
- ???: Thread (new, join, start, currentThread)
- j.u.c: ???

Monitor - это концепция изобретенная hansen/Hoare и используемая в Java, C#, pthreads, ???.

### Зачем нужен Monitor?

Давайте посмотрим на такой примитив синхронизации как CountDownLatch:
```java
???
```

и реализуем его на основе implicit monitor
```java
???
```

### Релизуем CDL на RhoLang
В RhoLang нет implicit monitors как в Java и нет аналога библиотеки j.u.c откуда можно взять Lock/Condition. Однако если мы задумаемся, то нам необходим не wait set, как коллекция, а функционал ожидания наступления определенного условия.
