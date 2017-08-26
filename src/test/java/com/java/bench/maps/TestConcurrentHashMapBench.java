package com.java.bench.maps;


public class TestConcurrentHashMapBench {

	/**
	 * Test that assertions are on.
	 * Its not ideal to have this test here, since its not related to ConcurrentHashMapBench at all.
	 */
	public void testAssertionsOn() {
		boolean assertOn = false;
		assert assertOn = true;
		if(!assertOn) throw new RuntimeException("Assertions are not on! Tests may pass when they should fail!");
	}

	/**
	 * To avoid an expensive modulo operation, we are constraining the ConcurrentHashMapBench.HASH_MAP_SIZE to be a (power of 2 - 1).
	 * That way, we can use a much cheaper AND operation. This tests that HASH_MAP_SIZE meets that constraint
	 */
	public void testHashMapSizePowerOf2() {
		int temp = ConcurrentHashMapBench.HASH_MAP_SIZE;
		while(temp!= 0) {
			assert((temp & 0x01) == 1) : "HASH_MAP_SIZE is not 2^n-1";
			temp = temp >> 1;
		}
	}
}
