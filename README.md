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

# Benchmarks
1. AtomicInteger performance
2. ConcurrentHashMap performance
3. Collections.synchronizedMap(HashMap) performance


# TO DO
1. Lambda for sorting performance
2. Streams for aggregating
3. Dynamic loading (calling via an abstract reference vs direct reference)

