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

package com.java.bench.lambda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import com.java.bench.util.rng.BenchRandomUtil;

/**
 * This benchmark tests how frequently lambda functions can be called.
 * 
 * https://www.youtube.com/watch?v=UKuFqAhDEN4
 *
 * By default:
 * 1. This test spawns as many threads as there are cores in the test environment.
 * 2. Higher numbers mean better performance.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class Lambdas {

	@State(Scope.Thread)
	public static class RNGState {
		/** We're using a simple XORshift for generating pseudo-random numbers : https://en.wikipedia.org/wiki/Xorshift
		 * because it is used in the measurement loop.
		 */
		BenchRandomUtil rng = new BenchRandomUtil();
	}

	@State(Scope.Benchmark)
	public static class CollectionState {
		//The size of the Collection. MUST BE 2^n-1 since our modulo operation depends on it.
		@Param({"255", "8191", "16383", "32767"})
		int DATA_SIZE;

		@Param({"HashSet", "ArrayList"})
		String dataType;

		/** This is the Collection that is exercised. Depending on dataType it is initialized to different instances.
		 * The value type is Integer because that is most likely the word size.
		 */
		Collection<Integer> data;

		//Run the setup only once for all iterations of the benchmark.
		@Setup(Level.Trial)
		public void setup() {
			//TODO: We are not specifying LOAD_FACTOR. Maybe we should?
			if(dataType.equals("HashSet")) {
				data = new HashSet<>(DATA_SIZE);
			} else if(dataType.equals("ArrayList")) {
				data = new ArrayList<>(DATA_SIZE);
			}

			//We'll seed the collection with numbers so that Collection.contains() actually does work.
			for(int i=0; i<DATA_SIZE; ++i) {
				data.add(i);
			}
		}
	}

	/**
	 * TODO : Compare with anonymous classes.
	 * 1. Comparators for sorting anonymous, concrete and lambda.
	 */

	/**
	 *	This benchmark tests how frequently lookups can be done on different Collections.
	 */
	@Benchmark
	public boolean testCollectionContains(CollectionState collectionState, RNGState rngState) {
		//This is similar to a modulo operation since DATA_SIZE is 2^n - 1
		int keyValue = rngState.rng.getNextXorShiftRN() & collectionState.DATA_SIZE;
		return collectionState.data.contains(keyValue);
	}

}
