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
package com.java.bench.maps;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

/**
 * This benchmark tests how frequently operations can be handled by a java.util.concurrent.ConcurrentHashMap
 *
 * By default:
 * 1. This test spawns as many threads as there are cores in the test environment.
 * 2. Higher numbers mean better performance.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class ConcurrentHashMapBench {
	final static int HASH_MAP_SIZE = 1023;
	final static float LOAD_FACTOR = 0.75f;

	@State(Scope.Thread)
	public static class CounterState {
		/** This is the key that we use to distribute data into the ConcurrentHashMap.
		 * Even though we only increment it, hopefully with multiple threads, the distribution of accesses
		 * over the ConcurrentHashMap is "randomized"
		 */
		int counter;

		@Setup
		public void setup() {
			counter = 0;
		}
	}
	@State(Scope.Benchmark)
	public static class MapState {
		/** This is the CHM that is exercised
		 * The key value pairs are Integer because that is most likely the word size.
		 */
		Map<Integer, Integer> map;

		@Setup
		public void setup() {
			// TODO : Ideally there should be another benchmark that tests the improvement when we also inform CHM about concurrencyLevel.
			// However its not really prevalent.
			map = new ConcurrentHashMap<>(HASH_MAP_SIZE, LOAD_FACTOR);
			//We'll seed the CHM with HASH_MAP_SIZE keys so that gets actually return data.
		}
	}

	/**
	 *	This benchmark tests how frequently key-value pairs can be added to a ConcurrentHashMap
	 */
	@Benchmark
	public void testMapPut(MapState mapState, CounterState ctrState) {
		//TODO : Should we do something about auto-boxing here?
		mapState.map.put(ctrState.counter, ctrState.counter);
		//Increment the counter;
		ctrState.counter++;
		//This is similar to a modulo operation since HASH_MAP_SIZE is a power of 2
		ctrState.counter &= 0xFF;
	}
}
