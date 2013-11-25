package com.javatracer.model.writers;

import com.javatracer.model.data.InterfaceInfo;

public interface DataBaseWriter {

	public void writeOutput(InterfaceInfo output);
	public void close();

}
