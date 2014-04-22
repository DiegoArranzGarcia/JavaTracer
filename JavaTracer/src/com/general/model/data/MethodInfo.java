package com.general.model.data;

import java.util.ArrayList;
import java.util.List;

import com.general.model.data.variables.Data;
import com.tracer.model.ChangeDetector;
import com.tracer.model.data.methods.MethodEntryInfo;
import com.tracer.model.data.methods.MethodExitInfo;

	
public class MethodInfo {
	
	private MethodEntryInfo entry;
	private MethodExitInfo exit;

	public MethodInfo(MethodEntryInfo entry,MethodExitInfo exit){
		this.entry = entry;
		this.exit = exit;
	}

	public List<Data> getArguments() {
		return entry.getArguments();
	}

	public Data getThis_data() {
		return entry.getThis_data();
	}

	public Data getReturn_data() {
		if (exit != null)
			return exit.getReturnData();
		else
			return null;
	}

	public List<ChangeInfo> getChanges() {
		List<ChangeInfo> changes = new ArrayList<ChangeInfo>();
		
		if (exit != null){
			ChangeDetector detector = new ChangeDetector();
			List<Data> entryArguments = entry.getArguments();
			List<Data> exitArguments = exit.getArguments();
			for (int i=0;i<entryArguments.size();i++){
				changes.addAll(detector.getChangesBetween(entryArguments.get(i),exitArguments.get(i)));
			}
			
			if ((entry.getThis_data() != null) && (exit.getThis_data() != null))
				changes.addAll(detector.getChangesBetween(entry.getThis_data(), exit.getThis_data()));
		}
		return changes;
	}

	public String getMethodName() {
		return entry.getMethodName();
	}
	
}