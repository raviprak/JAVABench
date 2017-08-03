# JAVABench
A suite for understanding the performance of JAVA primitives, Collections, and features.

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

### Benchmarks
1. AtomicInteger performance
2. ConcurrentHashMap performance
3. Collections.synchronizedMap(HashMap) performance


### TO DO
1. Lambda for sorting performance
2. Streams for aggregating
3. Dynamic loading (calling via an abstract reference vs direct reference)
4. How many times can an intrinsic lock be captured and released? (synchronized block / method)
5. JMH_Sample_01 : Just a method call
6. How many times can a wait() notify() be called in a second?
7. Reflection overhead.
8. Dependency injection (Dagger2) overhead.
9. TreeSet vs PriorityQueue https://issues.apache.org/jira/browse/HDFS-12278
10. Native IO vs JAVA IO?
11. Finalized vs non-finalized


### Measurement variables
1. JDK7, JDK8, JDK9. Let's compile and run on the same version and not worry about cross-version testing.
2. Laptop, Intel Desktop, AMD Desktop, DigitalOcean VM, Phone(?)
3. 
