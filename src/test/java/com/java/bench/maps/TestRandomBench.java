package com.java.bench.maps;

import com.java.bench.util.rng.BenchRandomUtil;

public class TestRandomBench {
	
	/**
	 * We should check that all algorithms implemented in BenchRandomUtil are being benchmarked.
	 * Admittedly this is not ideal.
	 * TODO : create a convention for method names in BenchRandomUtil, follow it and check over here.
	 */
	public void testAllAlgorithmsInBenchRandom() {
		assert BenchRandomUtil.class.getDeclaredMethods().length == 3 :
			"Please check that all implemented BenchRandomUtil algorithms are also added to RandomBench. Found "
			+ BenchRandomUtil.class.getDeclaredMethods().length;
	}

}
