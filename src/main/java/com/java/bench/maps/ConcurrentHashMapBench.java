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

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
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
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class ConcurrentHashMapBench {
	final static int OPCOUNT = 10240;

	@State(Scope.Benchmark)
	public static class MapBenchState {
		ArrayList<Integer> array;
		Map<Integer, Integer> map;
		
		@Setup
		public void setup() {
			Random rng = new Random();
			array = new ArrayList<>(OPCOUNT);
			for(int i=0; i<OPCOUNT; ++i) array.add(rng.nextInt());

			map = new ConcurrentHashMap<>();
		}
	}

	@Benchmark
	public void testMap(MapBenchState state, Blackhole bh) {
		for(int i=0; i<state.array.size(); ++i) {
			state.map.put(state.array.get(i), i);
			bh.consume(state.map.get(state.array.get(i)+1));
		}
	}
}
