package Tracer;

import java.util.List;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.StepRequest;

/**
* This class keeps context on events in one thread.
* In this implementation, context is the indentation prefix.
*/
  public class ThreadTrace {
      final ThreadReference thread;
      final String baseIndent;
      static final String threadDelta = " ";
      StringBuffer indent;
      static String nextBaseIndent = ""; // Starting indent for next thread
      VirtualMachine vm; // Running VM
      

      public ThreadTrace(ThreadReference thread, VirtualMachine vm) {
          this.thread = thread;
          this.baseIndent = nextBaseIndent;
          this.vm=vm;
          indent = new StringBuffer(baseIndent);
          nextBaseIndent += threadDelta;
          EventRequestManager mgr = vm.eventRequestManager();
          StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_OVER);
          request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
          request.addCountFilter(1);
          request.enable();
          
      }
      
      public void methodExitEvent(MethodExitEvent event) {
              int numArgs = 0;
                      try {
                              numArgs = event.method().variables().size();
                      } catch (AbsentInformationException e1) {
                              e1.printStackTrace();
                      }
              /**
       * El tipo de valores que devuelve un método en el caso que lo haga.
       */
                      
              if(!event.method().returnTypeName().equals("void"))
                      System.out.println("El método "+ event.method().name() + " devuelve el valor " + event.returnValue() + " del tipo " + event.method().returnTypeName());
              
              
          try{
       List<LocalVariable> variables = event.method().variables();
       StackFrame frame = thread.frame(0);
                              System.out.println("\t Al finalizar " +event.method().name()+":");
                              for (int i=0;i<variables.size();i++)
                                      System.out.println("\t\t"+variables.get(i).name()+ " = " + frame.getValue(variables.get(i)));
                              System.out.println("\n");
                       }
          catch(Exception e){}
          
          //System.out.println("************ " + event.method().name()+ " ************\n");
         
      }

      public void fieldWatchEvent(ModificationWatchpointEvent event){
          Field field = event.field();
          Value value = event.valueToBe();
          System.out.println(field.name() + " = " + value.toString()+" (Variable global)");
      }

      public void exceptionEvent(ExceptionEvent event) {
          
              // Step to the catch
          EventRequestManager mgr = vm.eventRequestManager();
          try {StepRequest req = mgr.createStepRequest(thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
          req.addCountFilter(1); // next step only
          req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
          req.enable();}
          catch(Exception e){
                  
         }
      }

      // Step to exception catch
      public void stepEvent(StepEvent event){
              try {
                      /*VARIABLES VISIBLES EN CADA STEP*/
                      
                      /*StackFrame frame = thread.frame(0);
                              List<LocalVariable> variables = frame.visibleVariables();
                              
                              
                              for (int i=0;i<variables.size();i++){
                                      System.out.println("\tVariable visibles: " + variables.get(i).name());
                              }*/
                              
       // Adjust call depth
       int cnt = 0;
       indent = new StringBuffer(baseIndent);
      
       cnt = thread.frameCount();
      
       while (cnt-- > 0) {
       indent.append("| ");
       }

       EventRequestManager mgr = vm.eventRequestManager();
       mgr.deleteEventRequest(event.request());
      
       StepRequest request = mgr.createStepRequest(this.thread, StepRequest.STEP_LINE, StepRequest.STEP_INTO);
       request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
       request.addCountFilter(1);
       request.enable();
      
                      } catch (Exception e) {
                              
                      }
              
      }

      public void threadDeathEvent(ThreadDeathEvent event) {
          indent = new StringBuffer(baseIndent);
          
         }
       }

