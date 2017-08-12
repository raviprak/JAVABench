package com.java.bench.intrinsic;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;

/**
 * This tests the number of times a method can be called, were there no locking involved
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class NoLocking {
	
	@Benchmark
	public void noLockingMethod() {
		//No work to do here
	}
}
