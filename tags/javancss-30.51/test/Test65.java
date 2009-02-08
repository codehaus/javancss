/*
 * Software Engineering Tools.
 *
 * $Id$
 *
 * Copyright (c) 1997-2001 Joseph Kiniry
 * Copyright (c) 2000-2001 KindSoftware, LLC
 * Copyright (c) 1997-1999 California Institute of Technology
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Joseph Kiniry, KindSoftware, nor the
 * California Institute of Technology, nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS
 * IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL KIND SOFTWARE OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package idebughc.testsuite;

import idebughc.*;

/**
 * <p> TestSuite is the black-box testsuite for the Debug class. </p>
 *
 * @version $Date$
 * @author Joseph R. Kiniry <joe@kindsoftware.com>
 *
 * @note The actual code of the IDebug test suite.
 */

public class TestSuiteThread extends Thread
{
  // Attributes

  private boolean value;
  private boolean success = true;
  private String testMode = null;

  // Constructors

  /**
   * Create a new TestSuiteThread with the specified test mode.
   *
   * @param tm the test mode for this test suite thread.  Exactly one
   * of the following strings: "console", "servletlog", "window", "writer".
   */

  TestSuiteThread(String tm)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(tm)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "TestSuiteThread(java.lang.String)", true), jassParameters);


      /* precondition */
      if (!((tm.equals("console")||tm.equals("servletlog")||tm.equals("window")||tm.equals("writer")))) throw new jass.runtime.PreconditionException("idebughc.testsuite.TestSuiteThread","TestSuiteThread(java.lang.String)",73,"tm_valid");

    this.testMode = tm;
      /* postcondition */
      if (!((testMode==tm))) throw new jass.runtime.PostconditionException("idebughc.testsuite.TestSuiteThread","TestSuiteThread(java.lang.String)",78,"testMode_is_valid");
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "TestSuiteThread(java.lang.String)", false), jassParameters);

  }

  // Inherited Methods
  // Public Methods

  public void run()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "run()", true), jassParameters);


    Debug debug = new Debug();

    System.out.println("TESTING IDEBUG PACKAGE.\n");
    System.out.println("Using test mode " + testMode + ".\n");

    System.out.println("Class-global testing\n" +
                       "====================");

    // Collect all the necessary references to the debugging modules.

    // Assert assert = debug.getAssert();
    DebugConstants debugConstants = debug.getDebugConstants();

    // Build the appropriate DebugOutput implementation depending upon the
    // value of testMode.
    DebugOutput debugOutput = null;
    if (testMode.equals("console")) {
      debugOutput = new ConsoleOutput(debug);
    } else if (testMode.equals("servletlog")) {
      debugOutput = new ServletLogOutput(debug);
    } else if (testMode.equals("window")) {
      debugOutput = new WindowOutput(debug);
    } else if (testMode.equals("writer")) {
      debugOutput = new WriterOutput(debug);
    } else
      throw new RuntimeException("Illegal test mode: " + testMode);

    // Set up the output interface of our debug instance.
    debug.setOutputInterface(debugOutput);

    // First we will test the default configuration (console output,
    // no new levels or categories.

    // Class-global testing.

    // Test 0
    success &= (!debugOutput.println(debugConstants.ASSERTION_LEVEL,
                                     "FAILED"));
    if (!success)
      System.err.println("FALURE #0");
    // Test 1
    success &= (!debugOutput.println(debugConstants.ASSERTION, "FAILED"));
    if (!success)
      System.err.println("FALURE #1");

    debug.turnOn();
    // Test 2
    success &= debugOutput.println(debugConstants.FAILURE_LEVEL, "PASSED");
    if (!success)
      System.err.println("FALURE #2");

    // Test 3
    success &= debugOutput.println(debugConstants.FAILURE, "PASSED");
    if (!success)
      System.err.println("FALURE #3");

    // Test 4
    debug.setLevel(debugConstants.LEVEL_MIN - 1);
    success &= (debug.getLevel() != (debugConstants.LEVEL_MIN - 1));
    if (!success)
      System.err.println("FALURE #4");

    debug.setLevel(debugConstants.ERROR_LEVEL);

    // Test 5
    success &= (!debugOutput.println(debugConstants.ERROR_LEVEL-1, "FAILED"));
    if (!success)
      System.err.println("FALURE #5");

    // Test 6
    success &= (!debugOutput.println(debugConstants.WARNING, "FAILED"));
    if (!success)
      System.err.println("FALURE #6");

    // Test 7
    success &= debugOutput.println(debugConstants.ERROR_LEVEL, "PASSED");
    if (!success)
      System.err.println("FALURE #7");

    // Test 8
    success &= debugOutput.println(debugConstants.ERROR, "PASSED");
    if (!success)
      System.err.println("FALURE #8");

    // Test 9
    success &= debugOutput.println(debugConstants.ERROR_LEVEL+1, "PASSED");
    if (!success)
      System.err.println("FALURE #9");

    // Test 10
    success &= debugOutput.println(debugConstants.CRITICAL, "PASSED");
    if (!success)
      System.err.println("FALURE #10");

    // Test 11
    success &= debugOutput.println(debugConstants.ASSERTION_LEVEL, "PASSED");
    if (!success)
      System.err.println("FALURE #11");

    // Test 12
    success &= debugOutput.println(debugConstants.ASSERTION, "PASSED");
    if (!success)
      System.err.println("FALURE #12");

    // Test 13
    debug.setLevel(debugConstants.LEVEL_MAX + 1);
    success &= (debug.getLevel() != (debugConstants.LEVEL_MIN + 1));
    if (!success)
      System.err.println("FALURE #13");

    debug.setLevel(0);

    // Test 14
    success &= debugOutput.println(0, "PASSED");
    if (!success)
      System.err.println("FALURE #14");

    // Test 15
    success &= debugOutput.println(debugConstants.NOTICE_LEVEL, "PASSED");
    if (!success)
      System.err.println("FALURE #15");

    // Test 16
    success &= debugOutput.println(debugConstants.NOTICE, "PASSED");
    if (!success)
      System.err.println("FALURE #16");

    // Test 17
    success &= debugOutput.println(debugConstants.ASSERTION_LEVEL, "PASSED");
    if (!success)
      System.err.println("FALURE #17");

    // Test 18
    success &= debugOutput.println(debugConstants.ASSERTION, "PASSED");
    if (!success)
      System.err.println("FALURE #18");

    // Test 19
    success &= debug.addCategory("NETWORK_6", 6);
    if (!success)
      System.err.println("FALURE #19");

    // Test 20
    success &= debug.addCategory("NETWORK_5", 5);
    if (!success)
      System.err.println("FALURE #20");

    // Test 21
    success &= debug.addCategory("NETWORK_4", 4);
    if (!success)
      System.err.println("FALURE #21");

    // Test 22
    debug.setLevel(5);
    // System.err.println("FALURE #22");

    // Test 23
    success &= debugOutput.println(5, "PASSED");
    if (!success)
      System.err.println("FALURE #23");

    // Test 24
    success &= debugOutput.println("NETWORK_5", "PASSED");
    if (!success)
      System.err.println("FALURE #24");

    // Test 25
    success &= (!debugOutput.println("NETWORK_4", "FAILED"));
    if (!success)
      System.err.println("FALURE #25");

    // Test 26
    success &= debugOutput.println("NETWORK_6", "PASSED");
    if (!success)
      System.err.println("FALURE #26");

    // Test 27
    success &= debug.removeCategory("NETWORK_5");
    if (!success)
      System.err.println("FALURE #27");

    // Test 28
    success &= (!debugOutput.println("NETWORK_5", "FAILED"));
    if (!success)
      System.err.println("FALURE #28");

    // Test 29
    success &= !debugOutput.println(debugConstants.LEVEL_MIN - 1, "FAILED");
    if (!success)
      System.err.println("FALURE #29");

    // Test 30
    success &= !debugOutput.println(debugConstants.LEVEL_MAX + 1, "FAILED");
    if (!success)
      System.err.println("FALURE #30");

    debug.turnOff();

    System.out.println("\nPer-thread testing\n" +
                       "====================");

    // Per-thread testing begins.

    Context debugContext =
      new Context(new DefaultDebugConstants(),
                  new ConsoleOutput(debug));

    // Note that we have turned off global debugging, so all of the
    // following is testing the case when a thread has debugging on
    // and global debugging is off.  A bit later, we'll turn global
    // debugging back on and the various "fall-back" scenarios.

    debugContext.turnOn();

    debugContext.setLevel(debugConstants.ERROR_LEVEL);

    // Test 31
    success &= debugContext.addCategory("PERTHREAD-1",
                                        debugConstants.ERROR_LEVEL-1);
    if (!success)
      System.err.println("FALURE #31");

    // Test 32
    success &= debugContext.addCategory("PERTHREAD+1",
                                        debugConstants.ERROR_LEVEL+1);
    if (!success)
      System.err.println("FALURE #32");

    // Install the new context.

    debug.addContext(debugContext);

    // Test 33
    success &= debugOutput.println(debugConstants.ERROR_LEVEL, "SUCCESS");
    if (!success)
      System.err.println("FALURE #33");

    // Test 34
    success &= debugOutput.println(debugConstants.ERROR_LEVEL+1, "SUCCESS");
    if (!success)
      System.err.println("FALURE #34");

    // Test 35
    success &= (!debugOutput.println(debugConstants.ERROR_LEVEL-1, "FAILURE"));
    if (!success)
      System.err.println("FALURE #35");

    // Test 36
    success &= (!debugOutput.println("PERTHREAD-1", "FAILURE"));
    if (!success)
      System.err.println("FALURE #36");

    // Test 37
    success &= debugOutput.println("PERTHREAD+1", "SUCCESS");
    if (!success)
      System.err.println("FALURE #37");

    // Test 38
    debugContext.setLevel(debugConstants.ERROR_LEVEL-1);
    // System.err.println("FALURE #38");

    // Test 39
    success &= debugOutput.println(debugConstants.ERROR_LEVEL+1, "SUCCESS");
    if (!success)
      System.err.println("FALURE #39");

    // Test 40
    success &= debugOutput.println(debugConstants.ERROR_LEVEL-1, "SUCCESS");
    if (!success)
      System.err.println("FALURE #40");

    // Test 41
    success &= debugOutput.println("PERTHREAD-1", "SUCCESS");
    if (!success)
      System.err.println("FALURE #41");

    // Test 42
    success &= debugOutput.println("PERTHREAD+1", "SUCCESS");
    if (!success)
      System.err.println("FALURE #42");

    // Now, we'll turn back on global debugging and try some tricky
    // combinations.

    debug.turnOn();

    // Global level is where we left it (5).  Current thread level is
    // ERROR_LEVEL-1, which is 4.  So, let's change the global to
    // ERROR_LEVEL and the per-thread to CRITICAL_LEVEL and see if we
    // can still get a rise out of the system.

    debug.setLevel(debugConstants.ERROR_LEVEL);
    debugContext.setLevel(debugConstants.CRITICAL_LEVEL);

    // Test 43
    success &= debugOutput.println(debugConstants.CRITICAL, "SUCCESS");
    if (!success)
      System.err.println("FALURE #43");

    // Test 44
    success &= (!debugOutput.println(debugConstants.NOTICE, "FAILURE"));
    if (!success)
      System.err.println("FALURE #44");

    // Test 45
    // This should succeed because the global level is ERROR_LEVEL.
    success &= debugOutput.println(debugConstants.ERROR, "SUCCESS");
    if (!success)
      System.err.println("FALURE #45");

    // Test 46
    success &= debugOutput.println(debugConstants.FAILURE, "SUCCESS");
    if (!success)
      System.err.println("FALURE #46");

    // End of tests
    System.out.println("Testing concluded.");

    if (success) {
      System.out.println("Debugging tests succeeded!\n\n");
      System.exit(0);
    } else {
      System.out.println("Debugging tests failed!\n\n");
      System.exit(-1);
    }
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "run()", false), jassParameters);


  }

    protected void finalize () throws java.lang.Throwable {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "finalize()", true), jassParameters);
        super.finalize();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "finalize()", false), jassParameters);
    }

    public boolean equals (java.lang.Object par0) {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(par0)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "equals(java.lang.Object)", true), jassParameters);
        boolean returnValue = super.equals(par0);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "equals(java.lang.Object)", false), jassParameters);
        return returnValue;
    }

    public java.lang.String toString () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "toString()", true), jassParameters);
        java.lang.String returnValue = super.toString();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "toString()", false), jassParameters);
        return returnValue;
    }

    public void interrupt () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "interrupt()", true), jassParameters);
        super.interrupt();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "interrupt()", false), jassParameters);
    }

    public boolean isInterrupted () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "isInterrupted()", true), jassParameters);
        boolean returnValue = super.isInterrupted();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "isInterrupted()", false), jassParameters);
        return returnValue;
    }

    public void destroy () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "destroy()", true), jassParameters);
        super.destroy();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "destroy()", false), jassParameters);
    }

    public java.lang.ClassLoader getContextClassLoader () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "getContextClassLoader()", true), jassParameters);
        java.lang.ClassLoader returnValue = super.getContextClassLoader();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "getContextClassLoader()", false), jassParameters);
        return returnValue;
    }

    public void setContextClassLoader (java.lang.ClassLoader par0) {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(par0)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "setContextClassLoader(java.lang.ClassLoader)", true), jassParameters);
        super.setContextClassLoader(par0);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc.testsuite", "TestSuiteThread", "setContextClassLoader(java.lang.ClassLoader)", false), jassParameters);
    } // end of inner class DummyServletContext

  // Protected Methods
  // Package Methods
  // Private Methods
  // Inner Classes

}
// end of class TestSuiteThread

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End:
 */

