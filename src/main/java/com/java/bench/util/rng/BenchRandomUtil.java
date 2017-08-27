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

package com.java.bench.util.rng;

import java.security.SecureRandom;
import java.util.Random;

/**
 * This is a utility class that exposes different algorithms for Random Number Generation. So far we have :
 * <ol>
 * 	<li>java.util.Random</li>
 * 	<li>XORshift (https://en.wikipedia.org/wiki/Xorshift)</li>
 * 	<li>java.security.SecureRandom</li>
 * </ol>
 * 
 * Ideally java.util.Random would have exposed different algorithms, but since it doesn't.
 * We copy pastad the implementation of XORShift from Wikipedia (https://en.wikipedia.org/wiki/Xorshift)
 *
 * TODO : Mersenne-Twister (https://en.wikipedia.org/wiki/Mersenne_Twister)
 *
 */
public class BenchRandomUtil {
	private Random rng;
	private int xorShiftRN;
	private SecureRandom secureRandom;

	public BenchRandomUtil() {
		rng = new Random();
		xorShiftRN = rng.nextInt();
		secureRandom = new SecureRandom();
	}

	public BenchRandomUtil(int seed) {
		rng = new Random(seed);
		xorShiftRN = rng.nextInt();
		secureRandom = new SecureRandom();
		secureRandom.setSeed(seed);
	}

	/**
	 * Get the next integer returned by java.util.Random
	 */
	public int getNextJavaUtilRandomInt() {
		return rng.nextInt();
	}

	/**
	 * Get the next integer returned by XORShift
	 */
	public int getNextXorShiftRN() {
		// We're using a simple XORshift for generating pseudo-random numbers. Courtesy : https://en.wikipedia.org/wiki/Xorshift
		xorShiftRN ^= xorShiftRN << 13;
		xorShiftRN ^= xorShiftRN >> 17;
		xorShiftRN ^= xorShiftRN << 5;
		return xorShiftRN;
	}
	
	/**
	 * Get the next integer returned by SecureRandom
	 */
	public int getNextSecureRandom() {
		return secureRandom.nextInt();
	}

}
