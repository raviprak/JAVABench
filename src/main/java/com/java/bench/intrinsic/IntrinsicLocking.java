package com.java.bench.intrinsic;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class IntrinsicLocking {
	private synchronized void mySynchronizedMethod() {
		
	}

	@Benchmark
	public void syncMethod() {
		mySynchronizedMethod();
	}
}
