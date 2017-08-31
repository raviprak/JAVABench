package com.java.bench.util.environ;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This utility class detects various benchmark environment properties and writes them out to a JSON file.
 * The JSON file is inserted into benchmark-index on the JAVABench homepage.
 * (e.g. one is hosted here : https://www.raviprak.com/research/java/JAVABench/JAVABench.html).
 *
 * Ideally, given all the environment properties, test results should be exactly reproducible.
 * Realistically though, there will variations and we yearn to describe the environment accurately to reduce that variation.
 *
 * Using Jackson even though GSON is a worthy alternative.
 * TODO: Maybe use Jackson-jr https://github.com/FasterXML/jackson-jr
 */
public class Environment {
	// These come from inside the JVM
	public int numProcessors;
	public long jvmFreeMemory;
	public long jvmMaxMemory;
	public String jvmDetails;
	public String osArch;
	public String osName;

	// These come from the os specific interfaces. e.g. on Linux, it comes from /proc
	public String processorType;

	public Environment() throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		numProcessors = runtime.availableProcessors();
		jvmFreeMemory = runtime.freeMemory();
		jvmMaxMemory = runtime.maxMemory();

		Properties prop = System.getProperties();
		jvmDetails = prop.getProperty("java.runtime.version");
		osArch = prop.getProperty("os.arch");
		osName = prop.getProperty("os.name");

		if(prop.getProperty("os.name").contains("nux")) {
			deduceLinuxEnvironment(runtime);
		}
	}

	/**
	 * This method returns the output of running a command.
	 * It does not work when the output contains unicode.
	 * It does not return the return code of the command.
	 * 
	 * @param runtime The runtime in which to execute the command
	 * @param command The command to execute. 
	 * @return The output of running the command. 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	String getCommandOutput(Runtime runtime, String command) throws IOException, InterruptedException {
		Process proc = runtime.exec(new String[] {"/bin/bash", "-c", command});
		proc.waitFor();
		InputStream inStream = proc.getInputStream();
		
		StringBuilder sb = new StringBuilder();
		while(inStream.available() != 0) {
			sb.append((char) inStream.read());
		}
		inStream.close();
		System.out.println(sb);
		return sb.toString();
	}
	
	private void deduceLinuxEnvironment(Runtime runtime) throws IOException, InterruptedException {
//		processorType = getCommandOutput(runtime, "cat /proc/cpuinfo | grep model | grep name | cut -d: -f2- | head -n 1");
		processorType = getCommandOutput(runtime, "cat /proc/cpuinfo");
		
	}

	public static void main(String args[]) throws IOException, InterruptedException {
		Environment env = new Environment();
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.writeValue(new File("environment.json"), env);
	}

}
