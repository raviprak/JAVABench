package com.java.bench.rng;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

/*
 * Copyright (C) 2017 raviprak
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * This benchmark tests how frequently java.util.Random can give us integers.
 * It also compares java.util.Random.nextInt with a XORshift (https://en.wikipedia.org/wiki/Xorshift)
 * Please bear in mind that this is only a performance test. The quality of randomness is not being measured.
 *
 * By default:
 * 1. This test spawns as only 1 thread in the test environment.
 * 2. Higher numbers mean better performance.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(1)
public class RandomBench {
	@State(Scope.Thread)
	public static class RNGState {
		/** This is an integer that we use to distribute data into the ConcurrentHashMap.
		 * We're using a simple XORshift for generating pseudo-random numbers : https://en.wikipedia.org/wiki/Xorshift
		 * because it is used in the measurement loop.
		 * Seeding it with a better RNG (java.util.Random) though
		 */
		Random rng = new Random();
		int pseudoRN = rng.nextInt();
		private int getNextXORShiftRN() {
			// We're using a simple XORshift for generating pseudo-random numbers. Courtesy : https://en.wikipedia.org/wiki/Xorshift
			pseudoRN ^= pseudoRN << 13;
			pseudoRN ^= pseudoRN >> 17;
			pseudoRN ^= pseudoRN << 5;
			return pseudoRN;
		}
	}

	/**
	 *	This benchmark tests how frequently java.util.Random can generate integers.
	 */
	@Benchmark
	public void testJavaUtilRandom(RNGState rngState, Blackhole bh) {
		bh.consume(rngState.rng.nextInt());
	}

	/**
	 *	This benchmark tests how frequently XORShift can generate integers.
	 */
	@Benchmark
	public void testXORShift(RNGState rngState, Blackhole bh) {
		bh.consume(rngState.getNextXORShiftRN());
	}
}
