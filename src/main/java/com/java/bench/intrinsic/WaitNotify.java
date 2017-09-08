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

package com.java.bench.intrinsic;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.ThreadParams;

/**
 * This benchmark tests how frequently wait() notify() can be called.
 * We make sure that the lock round-robins amongst all threads (rather than bounce between only 2) because we want the
 * lock to migrate across all CPUs.
 *
 * By default:
 * 1. This test spawns as many threads as there are cores in the test environment.
 * 2. Higher numbers mean better performance.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
public class WaitNotify {

	/**
	 * This is the Object on which threads lock, wait and notify. It also contains an integer which acts as
	 * the index of the thread which much wake up next.
	 */
	@State(Scope.Benchmark)
	public static class LockObject {
		int nextThreadId = 0;
		int numThreads;

		@Setup
		public void setup(ThreadParams threadParams) {
			numThreads = threadParams.getThreadCount();
		}

		public int getNextThreadId() {
			return nextThreadId;
		}

		public void incNextThreadId() {
			nextThreadId++;
			if(nextThreadId >= numThreads) nextThreadId -= numThreads;
		}
	}

	@Benchmark
	public void waitNotifyMethod(LockObject lock, ThreadParams threads) throws InterruptedException {
		synchronized(lock) {
			while(threads.getThreadIndex() != lock.nextThreadId) {
				lock.wait();
			}
			lock.incNextThreadId();
			lock.notify();
		}
	}

	@Benchmark
	public void waitNotifyAllMethod(LockObject lock, ThreadParams threads) throws InterruptedException {
		synchronized(lock) {
			while(threads.getThreadIndex() != lock.nextThreadId) {
				lock.wait();
			}
			lock.incNextThreadId();
			lock.notifyAll();
		}
	}
}
