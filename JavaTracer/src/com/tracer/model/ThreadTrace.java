package com.tracer.model;

import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

/**
* This class keeps context on events in one thread.
* In this implementation, context is the indentation prefix.
*/

public class ThreadTrace {
      final ThreadReference thread;
      VirtualMachine vm; // Running VM      

      public ThreadTrace(ThreadReference thread, VirtualMachine vm) {
          this.thread = thread;
          this.vm=vm;
          EventRequestManager mgr = vm.eventRequestManager();
          StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
          request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
          request.addCountFilter(1);
          request.enable();
      }
      
      public ThreadReference getThreadReference(){
              return thread;
      }
      
      public VirtualMachine getVm() {
    	  return vm;
      }
}