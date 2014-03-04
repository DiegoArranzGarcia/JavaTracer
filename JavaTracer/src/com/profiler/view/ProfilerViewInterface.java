package com.profiler.view;

import java.util.HashMap;

public interface ProfilerViewInterface {

	void loadPieChart(HashMap<String, Integer> classes, int numCalledMethods);

}
