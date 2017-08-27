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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

import com.java.bench.util.rng.BenchRandomUtil;

@/**
 * This benchmark tests how frequently operations can be handled by a synchronized HashMap
 * We use XORshift to somewhat pseudo-randomize the access pattern. So multiple threads will still have some conflict.
 * Our HashMap's size is 2^16 (65536)
 *
 * By default:
 * 1. This test spawns as many threads as there are cores in the test environment.
 * 2. Higher numbers mean better performance.
 */
BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class SynchronizedHashMapBench {
	// We will keep the hash map size under this. (We use "modulo" on the pseudoRN)
	final static int MAX_BITS = 16;
	final static int HASH_MAP_SIZE = (int) Math.pow(2, MAX_BITS) - 1;
	final static float LOAD_FACTOR = 0.75f;

	@State(Scope.Thread)
	public static class RNGState {
		/** We're using a simple XORshift for generating pseudo-random numbers : https://en.wikipedia.org/wiki/Xorshift
		 * because it is used in the measurement loop.
		 */
		BenchRandomUtil rng = new BenchRandomUtil();
	}

	@State(Scope.Benchmark)
	public static class MapState {
		/** This is the HashMap that is exercised
		 * The key value pairs are Integer because that is most likely the word size.
		 */
		Map<Integer, Integer> map;

		@Setup
		public void setup() {
			//Initialize the map
			map = Collections.synchronizedMap(new HashMap<>(HASH_MAP_SIZE, LOAD_FACTOR));
			//We'll seed the HashMap with HASH_MAP_SIZE keys so that HM.get actually return data.
			for(int i=0; i<HASH_MAP_SIZE; ++i) {
				map.put(i, i);
			}
		}
	}

	/**
	 *	This benchmark tests how frequently key-value pairs can be added to a ConcurrentHashMap
	 */
	@Benchmark
	public void testMapPut(MapState mapState, RNGState rngState) {
		//This is similar to a modulo operation since HASH_MAP_SIZE is 2^n - 1
		int keyValue = rngState.rng.getNextXorShiftRN() & HASH_MAP_SIZE;
		mapState.map.put(keyValue, keyValue);
	}

	/**
	 *	This benchmark tests how frequently key-value pairs can be retrieved from a ConcurrentHashMap
	 */
	@Benchmark
	public void testMapGet(MapState mapState, RNGState rngState, Blackhole bh) {
		//This is similar to a modulo operation since HASH_MAP_SIZE is 2^n - 1
		int keyValue = rngState.rng.getNextXorShiftRN() & HASH_MAP_SIZE;
		bh.consume(mapState.map.get(keyValue));
	}
}
