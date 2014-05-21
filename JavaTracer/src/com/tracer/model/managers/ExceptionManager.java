package com.tracer.model.managers;

import java.util.ArrayList;
import java.util.List;

import com.general.model.data.variables.Data;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.event.ExceptionEvent;
import com.tracer.model.ClassUtils;
import com.tracer.model.TraceWriter;
import com.tracer.model.data.methods.MethodExitInfo;

public class ExceptionManager {

	private ClassUtils utils;
	private TraceWriter writer;

	public ExceptionManager(TraceWriter writer, ClassUtils utils) {
		this.utils = utils;
		this.writer = writer;
	}

	public void exceptionEvent(ExceptionEvent event) {

		ThreadReference thread = event.thread();

		Location lo = event.catchLocation();

		String classException = event.location().declaringType().name();

		if (!classException.contains("ClassLoader") && lo == null) {

			try {
				List<StackFrame> list = null;
				list = thread.frames();

				int i = 0;

				while (list.size() > i) {

					StackFrame frame = list.get(i);

					String className = frame.location().declaringType().name();
					
					if (!utils.isExcludedClass(className)){
						String method = frame.location().method().name();
						
						List<Data> arguments = processArguments(frame.location()
								.method(), frame.thread());

						Data returnObject = utils.getObjectFromObjectReference(
								false, "Exception", event.exception(),
								new ArrayList<Long>());

						ReferenceType ref = event.location().method()
								.declaringType(); // "class" where is declaring the
													// method
						Data object_this = processThis(event, ref, thread);

						MethodExitInfo info = new MethodExitInfo(method, className,
								returnObject, arguments, object_this);
						writer.writeMethodExitInfo(info);
					}
					
					i++;
				}

			} catch (IncompatibleThreadStateException e) {

			}

		}

	}

	private Data processThis(ExceptionEvent event, ReferenceType ref,
			ThreadReference thread) {

		StackFrame stack = null;

		try {
			stack = thread.frame(0);
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}

		Data valueThis = utils.getObj("this", stack.thisObject(),
				new ArrayList<Long>());

		return valueThis;
	}

	private List<Data> processArguments(Method method, ThreadReference thread) {

		List<Data> arguments = new ArrayList<>();
		StackFrame stack = null;
		
		try {
			stack = thread.frame(0);
		} catch (IncompatibleThreadStateException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			List<LocalVariable> variables = method.arguments();
			for (int i = 0; i < variables.size(); i++) {
				LocalVariable var = variables.get(i);
				String nameVar = var.name();

				Data varObj = null;
				if (nameVar.equals("args"))
					varObj = utils.getObj(nameVar, null, new ArrayList<Long>());
				else
					varObj = utils.getObj(nameVar, stack.getValue(var),new ArrayList<Long>());

				arguments.add(varObj);

			}
		} catch (Exception e) {
			e.printStackTrace();
			
			List<Value> variables = stack.getArgumentValues();
			for (int i = 0; i < variables.size(); i++) {
				Value var = variables.get(i);
				String nameVar = "arg " + i; 

				Data varObj = null;
				if (nameVar.equals("args"))
					varObj = utils.getObj(nameVar, null, new ArrayList<Long>());
				else
					varObj = utils.getObj(nameVar,var,new ArrayList<Long>());

				arguments.add(varObj);

			}
			
			
		}
		return arguments;

	}

}
