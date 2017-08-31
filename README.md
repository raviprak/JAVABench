# JAVABench
A suite for understanding the performance of JAVA primitives, Collections, and features.

Please see the performance graphs here : https://www.raviprak.com/research/java/JAVABench/JAVABench.html

To build, please run 
```
$ mvn package
```

To get JMH options, please run
```
$ java -jar target/javabench.jar -help
```

To run the entire benchmark suite, please run
```
$ java -jar target/javabench.jar
```

To run the benchmark with a specific JVM (as shown in help) :
```
$ java -jar target/javabench.jar -jvm /usr/java/latest/bin/java
```

To run the benchmark with specific JVM arguments (as shown in help) :
```
$ java -jar target/javabench.jar -jvm /usr/java/latest/bin/java -jvmArgs -XX:AllocatePrefetchLines=3
```

To quickly run a benchmark while you are developing (as shown in help) :
```
$ java -jar target/javabench.jar -f 1 -wi 1 -i 1 <TheNameOfTheBenchMark>
```

### Benchmarks
1. NoLocking : How often can an unsynchronized method be called?
2. IntrinsicLocking : How often can a synchronized method be called?
3. AtomicInteger : How often can an AtomicInteger be incremented
4. Random : How often can different Random Number Generators produce integers?
5. ConcurrentHashMap performance : How often can get and put be called on a ConcurrentHashMap
6. Collections.synchronizedMap(HashMap) performance


### TO DO
1. Lambda for sorting performance
2. Streams for aggregating
3. Dynamic loading (calling via an abstract reference vs direct reference)
4. How many times can a wait() notify() be called in a second?
5. Reflection overhead.
6. Dependency injection (Dagger2) overhead.
7. TreeSet vs PriorityQueue https://issues.apache.org/jira/browse/HDFS-12278
8. Native IO vs JAVA IO?
9. Finalized vs non-finalized
10. Varargs overhead
11. Boxing overhead
12. Eclipse collections? 

### Measurement variables
1. JDK7, JDK8, JDK9. Let's compile and run on the same version and not worry about cross-version testing.
2. Laptop, Intel Desktop, AMD Desktop, DigitalOcean VM, Phone(?)
3. 
