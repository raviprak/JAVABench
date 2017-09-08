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
import org.openjdk.jmh.infra.Control;
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
	 * the index of the thread which must wake up next.
	 */
	@State(Scope.Benchmark)
	public static class LockObject {
		//This MUST be volatile because we do want the value to be propagated across all CPUs.
		volatile int nextThreadId = 0;
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

	/**
	 * This is a bad non-deterministic benchmark and it can livelock. Let's say there are 4 threads : A, B, C and D (with increasing thread IDs). Since notify() makes
	 * no guarantees about which thread it will wake up, it may so happen that C wakes up B. B comes out of wait, checks the condition and goes back
	 * to waiting (without even notifying anyone else).
	 * Hence commenting it out. 
	 */
//	@Benchmark
	public void waitRoundRobinNotifyMethod(LockObject lock, ThreadParams threads) throws InterruptedException {
		synchronized(lock) {
			while(threads.getThreadIndex() != lock.nextThreadId) {
				lock.wait();
			}
			lock.incNextThreadId();
			lock.notify();
		}
	}

	/**
	 * This benchmark is not ideal either. After being awoken, all the threads must compete to obtain the lock. On a machine with more threads, this
	 * can be expected to take more time. So the benchmark is flawed in that sense.
	 */
	@Benchmark
	public void waitRoundRobinNotifyAllMethod(LockObject lock, ThreadParams threads, Control control) throws InterruptedException {
		synchronized(lock) {
			//Checking control.stopMeasurement here because we don't know which order JMH stops the threads in. It may be that a jmh-worker thread is in here
			//waiting for nextThreadId to become its index, while all the other threads are in teardown (and so the nextThreadId is not being incremented)
			while(!control.stopMeasurement && threads.getThreadIndex() != lock.nextThreadId) {
				lock.wait(1000);
			}
			lock.incNextThreadId();
			lock.notifyAll();
		}
	}

}
