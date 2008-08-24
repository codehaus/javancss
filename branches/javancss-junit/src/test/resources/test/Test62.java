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

package idebughc;

import java.util.Enumeration;
import java.util.Hashtable;

/** 
 * <p> Debug is the core class of the IDebug debugging facilities.
 *
 * <p> The Debug class is used as the central facility for configuring
 * debugging for a component.
 *
 * <p> All assertions are handled in the Assert class, all logging is
 * accomplished via the DebugOutput class, and all system monitoring and
 * statistics gathering takes place via the Collect class.
 *
 * <p> This debug package is meant to help, in general, produce high
 * quality, high confidence, code.
 *
 * <p> The Debug facility is non-static.  The first thing your
 * component or application needs to do is construct a new Debug
 * object.  If you wish to install an alternate implementation of the
 * debugging constants (i.e. categories, levels, error messages,
 * etc.), pass your implementation of DebuggingConstantsInterface to
 * the constructor of Debug.
 *
 * <p> Each thread that calls the Debug class has, potentially, a
 * different context.  Elements of this context include a notion of
 * current message level, message types, classes currently under
 * inspection, and whether debugging is turned on or off.
 *
 * <p> Threads create new debugging contexts by constructing a Context
 * object and calling its methods.  This Context is then passed to the
 * Debug object to change the current global debugging context at
 * runtime.
 *
 * <p> In brief, the Debug class is normally used in the following
 * manner.  A more detailed discussion of the use of this class can be
 * found in the full documentation for the Infospheres debugging
 * package.  See the debug package's <a href="../index.html">main
 * index</a> for more information.
 *
 * <p> Each thread needs to construct a debugging context (see the
 * Context class for details) to detail its specific debugging needs.
 * After creating a valid debugging context, encapsulated in the
 * Context object, the object is passed to this class (Debug) via the
 * <code>addContext()</code> method so that the debugging runtime
 * system knows the thread's context.  Note that the debug runtime
 * keeps a reference to the passed Context, it does not make a copy of
 * it.  Thus, you can modify the Context (change debugging levels, add
 * new thread-specific categories, etc.)  after the context is
 * installed and changes will be noted immediately by the debug
 * runtime.
 *
 * <p> Finally, you have to direct the output of the debugging runtime.
 * This is accomplished by constructing an implementation of the
 * <code>DebugOutput</code> interface, e.g. <code>ConsoleOutput</code>.
 * This object is then passed to your Debug object via the
 * <code>Debug.setOutputInterface()</code> method.
 *
 * <p> You're ready to rock and roll.  Call
 * <code>debug.getAssert()</code> to get a reference to your debug
 * runtime's Assert class.  Finally, if you chose not to install your
 * own implementation of <code>DebugConstants</code>, call
 * <code>debug.getDebugConstants()</code> to get a reference to your
 * debug constants.
 *
 * <p> Then, simply use <code>assert.assert()</code>, the
 * <code>print()</code>, <code>println()</code> of your
 * <code>DebugOutput</code> and/or <code>Utilities.dumpStackSafe()</code>
 * methods in your code as necessary.
 *
 * <p> Note that all class-specific debugging is <em>additive</em> and
 * <em>reductive</em>.  You can either remove all classes from the
 * debugging table then add classes one by one, or you can add all
 * <em>potential</em> classes then remove them one by one at this
 * time.  Meaning, when you perform an add of "*", you are
 * <em>not</em> adding all classes currently defined in this VM; you
 * are adding all classes currently defined and all classes that might
 * ever be defined in this VM.
 *
 * @version $Revision$ $Date$
 * @author Joseph R. Kiniry <joe@kindsoftware.com>
 * @history Versions 0.01 through 0.10 were developed as
 * <code>edu.caltech.cs.kiniry.coding.Debug</code>.  New revision
 * history began when class was moved to
 * <code>edu.caltech.cs.infospheres.util.Debug</code>.  Six versions
 * were developed while in this package.  The code was then moved to
 * Joe's PhD repository and refactoring began to take place as of
 * cumulative version 0.17.
 *
 * @todo kiniry Possible future enhancements:
 * <ol>
 * <li> New derivative: persistence mobile debug object.
 * <li> GUI interface to runtime debugging.
 * <li> Garbage collection thread for Debug to clean up stopped
 * threads.
 * <li> Support for ThreadGroup contexts.
 * </ol>
 *
 * @review kiniry To make debugging classes as robust as possible, we
 * need to decide if they should not throw exceptions at all and only
 * return error values if outright failures occur in processing, or
 * throw real exceptions, etc.  Once javap is built, this will be
 * something of a non-issue (since the user will not have to type the
 * exception-handling code at all).
 *
 * @review kiniry Should all precondition/postcondition checks be
 * assertions?  This would lower the robustness of the code.  Perhaps
 * this means that the definitions for @pre/postconditions need be
 * refined (i.e. are they always assertions or not?).
 *
 * @review kiniry Should assertions always call stackDump()?  Should
 * they always call System.exit()?  That's not very nice or robust,
 * and it certainly doesn't support distributed debugging well.
 * Perhaps we can throw some kind of InterruptedException in the
 * thread?
 *
 * @review kiniry Should null or zero-length debugging messages be
 * permitted?  Wouldn't this increase robustness?
 *
 * @review kiniry Are print() and println() methods both necessary?
 * Why not just print()?  Similar to the isOn() controversy.
 *
 * @review kiniry Should calls to println with an ASSERTION_LEVEL
 * cause the calling thread to stop, as in a real assertion?  This
 * complicates the semantics of the print() methods.
 *
 * @review kiniry Addition of an exception stack trace printing
 * mechanism (as per dmz, 15 January).
 *
 * @review kiniry Addition of a system property to turn global
 * debugging on/off (as per dmz, 15 January).
 *
 * @design General Debug design obtained through group consensus in
 * mid November, 1997.
 *
 * @todo kiniry Should the global DebugOutput be public?  Should a
 * client be able to get a reference to it via a call to the
 * appropriate getter instead?  I.e. Detect whether the thread is in a
 * per-thread debugging state and, if not, return the global output
 * interface?  
 */

public class Debug implements Cloneable
{
  // Attributes

  /** 
   * <p> <code>threadHashtable</code> is a hashtable of all threads that
   * have some per-thread specific debugging attributes defined.
   * Per-thread attributes include categories and classes.  A key of this
   * hashtable is a reference to a <code>Thread</code>, while a data value
   * is a <code>Context</code> object which contains the information
   * specific to this thread. </p>
   *
   * <p> <code>threadHashtable</code> always has an two entries, one under
   * the key "GLOBAL_CATEGORIES" and one under the key "GLOBAL_CLASSES".
   * These entries contain all the class-global categories and
   * class-specific information for debugging, respectively. </p>
   *
   * <p> Internal class handling is somewhat complicated.  If the
   * expression "*" is <em>removed</em>, the database is simply cleared.
   * If the expression "*" is <em>added</em>, the entry is inserted in the
   * table.  So, if one removes specific classes after removing "*", or if
   * one adds specific classes after adding "*", there is no change to the
   * database.  <em>But</em>, if you remove specific classes after adding
   * "*", or if you add specific classes after removing "*", your changes
   * will be noted.  I.e. <code>Debug</code> handles both additive and
   * reductive specification of classes. </p>
   */

  Hashtable threadHashtable = null;

  /**
   * <p> The debugging constants for this class. </p>
   *
   * @modifies SINGLE-ASSIGNMENT
   */

  DebugConstants debugConstants = null;

  /**
   * <p> The current "global" (<code>Debug</code> instance scoped) flag
   * indicating if any debugging is enabled.  If (isOn == false), all calls
   * like <code>Assert.assert()</code> and
   * <code>DebugOutput.print()</code>, but for the query and state change
   * functions (like <code>isOn()</code>, <code>turnOn()</code>, etc.)  are
   * short-circuited and do nothing. </p>
   */

  boolean isOn = false;

  /**
   * <p> The current global (<code>Debug</code> instance scoped) debug
   * level of the <code>Debug</code> class. </p>
   *
   * @design Higher valued levels usually indicate higher priorities.
   * E.g. A level 9 message is in the default implementation an asssertion;
   * if it fails, the program exits.  A level 5 message is an error and the
   * user should probably be informed of the problem.  You can override
   * this behavior by subtyping <code>DebugConstants</code> and installing
   * the new constant set when constructing <code>Debug</code>.
   *
   * @values (debugConstants.LEVEL_MIN <= level <=
   * debugConstants.LEVEL_MAX)
   */

  int level = 0;

  /**
   * <p> The <code>Assert</code> object associated with this
   * <code>Debug</code> object, when instantiated. </p>
   *
   * @modifies SINGLE-ASSIGNMENT
   */

  Assert assert = null;

  /**
   * <p> The <code>Collect</code> object associated with this
   * <code>Debug</code> object, when instantiated. </p>
   *
   * @modifies SINGLE-ASSIGNMENT
   */

  Collect collect = null;

  /**
   * <p> Private debugging utility class that encapsulates several helpful
   * algorithms. </p>
   *
   * @modifies SINGLE-ASSIGNMENT
   */
  
  Utilities debugUtilities = null;

  /**
   * <p> The class used by this thread to control debugging output device.
   * All global debugging messages will use this interface for output. </p>
   */

  transient public DebugOutput debugOutputInterface;

  // Inherited Methods

  public Object clone()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "clone()", true), jassParameters);
      java.lang.Object jassResult;

    try {
          jassResult = ( super.clone());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "clone()", false), jassParameters);


          return jassResult;
    } catch (CloneNotSupportedException cnse) {
      throw new RuntimeException(cnse.getMessage());
    }
  }

  // Constructors

  /**
   * <p> Construct a new <code>Debug</code> class.  Note that the method
   * <code>setOutputInterface</code> need be called on the newly
   * constructed <code>Debug</code> object before it can be used. </p>
   */

  public Debug()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "Debug()", true), jassParameters);


    init(new DefaultDebugConstants(), null);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "Debug()", false), jassParameters);

  }
  
  /**
   * <p> Construct a new <code>Debug</code> class.  Note that the method
   * <code>setOutputInterface</code> need be called on the newly
   * constructed <code>Debug</code> object before it can be used. </p>
   *
   * @param dc an implementation of the <code>DebugConstants</code>
   * interface that defines the semantics of this debug context.
   * @param c an implementation of the <code>Collect</code> class.
   * @see idebug.SimpleCollect
   */

  public Debug(DebugConstants dc, Collect c)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(dc), new jass.runtime.traceAssertion.Parameter(c)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "Debug(idebughc.DebugConstants,idebughc.Collect)", true), jassParameters);


      /* precondition */
      if (!((dc!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","Debug(idebughc.DebugConstants,idebughc.Collect)",321,"dc_non_null");
      if (!((c!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","Debug(idebughc.DebugConstants,idebughc.Collect)",322,"c_non_null");

    init(dc, c);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "Debug(idebughc.DebugConstants,idebughc.Collect)", false), jassParameters);

  }

  // Public Methods

  /**
   * <p> Set the global output interface to a new
   * <code>DebugOutput</code>. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@6b017e</b>: (d!=null)</code></dd></dl> 
<dl><dt><b>Ensures:</b></dt><dd><code><b>jass.reflect.AssertionLabel@2effdf</b>: (getOutputInterface()==d)</code></dd></dl> 
@concurrency CONCURRENT
   * @modifies debugOutputInterface
   * @param d the new output interface.
   */

  public void setOutputInterface(DebugOutput d)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(d)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "setOutputInterface(idebughc.DebugOutput)", true), jassParameters);


      /* precondition */
      if (!((d!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","setOutputInterface(idebughc.DebugOutput)",340,"d_not_null");

    this.debugOutputInterface = d;
      /* postcondition */
      if (!((jassInternal_getOutputInterface()==d))) throw new jass.runtime.PostconditionException("idebughc.Debug","setOutputInterface(idebughc.DebugOutput)",344,"output_interface_set");
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "setOutputInterface(idebughc.DebugOutput)", false), jassParameters);

  }

  /**
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @return the <code>Assert</code> object associated with this
   * <code>Debug</code> object.
   */

  public Assert getAssert()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getAssert()", true), jassParameters);
      idebughc.Assert jassResult;

      jassResult = ( assert);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getAssert()", false), jassParameters);


      return jassResult;
  }


  /**
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @return the <code>Collect</code> object associated with this
   * <code>Debug</code> object.
   */

  public Collect getCollect()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getCollect()", true), jassParameters);
      idebughc.Collect jassResult;

      jassResult = ( collect);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getCollect()", false), jassParameters);


      return jassResult;
  }

  /**
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @return the <code>DebugOutput</code> corresponding to the invoking
   * thread or, if that thread has no interface, the global output
   * interface.
   */

  public DebugOutput getOutputInterface()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getOutputInterface()", true), jassParameters);
      idebughc.DebugOutput jassResult;

    Thread currentThread = Thread.currentThread();
    
    if (threadHashtable.containsKey(currentThread)) {
      Context debugContext = 
        (Context)(threadHashtable.get(currentThread));
            jassResult = ( debugContext.getOutputInterface());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getOutputInterface()", false), jassParameters);


            return jassResult;
    } else {
        jassResult = ( this.debugOutputInterface);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getOutputInterface()", false), jassParameters);


        return jassResult;
    }
  }

  /**
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @return the <code>DebugConstants</code> for this <code>Debug</code>
   * object.
   */

  public DebugConstants getDebugConstants()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getDebugConstants()", true), jassParameters);
      idebughc.DebugConstants jassResult;

      jassResult = ( debugConstants);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getDebugConstants()", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Returns a boolean indicating if any debugging is turned on. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return a boolean indicating if any debugging is turned on.
   */

  public synchronized boolean isOn()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOn()", true), jassParameters);
      boolean jassResult;

      jassResult = ( isOn);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOn()", false), jassParameters);


      return jassResult;
  }
 
  /**
   * <p> Returns a boolean indicating whether any debugging facilities are
   * turned on for a particular thread. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@20a47e</b>: (thread!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies QUERY
   * @ensures Debugging turned on for passed thread.
   * @param thread is the thread that we are interested in.
   * @return a boolean indicating whether any debugging facilities are
   * turned on for a particular thread.
   */

  public synchronized boolean isOn(Thread thread)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(thread)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOn(java.lang.Thread)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((thread!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","isOn(java.lang.Thread)",430,"thread_non_null");

    // Make sure that there is a legal entry in the threadHashtable
    // for this particular thread.
    if (threadHashtable.containsKey(thread))
    {
      // Get the object that describes the per-thread debugging state.
      Context debugContext = 
        (Context)(threadHashtable.get(thread));
            jassResult = ( debugContext.isOn());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOn(java.lang.Thread)", false), jassParameters);


            return jassResult;
    }
    else {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOn(java.lang.Thread)", false), jassParameters);


        return jassResult;
    }
  }

  /**
   * <p> Returns a boolean indicating if any debugging is turned off. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return a boolean indicating if any debugging is turned on.
   * @review kiniry Are the isOff() methods necessary at all?
   */

  public synchronized boolean isOff()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOff()", true), jassParameters);
      boolean jassResult;

      jassResult = ( (!isOn()));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOff()", false), jassParameters);


      return jassResult;
  }
  

  /**
   * <p> Returns a boolean indicating whether any debugging facilities are
   * turned off for a particular thread. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@789144</b>: (thread!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies QUERY
   * @param thread is the thread that we are interested in.
   * @return a boolean indicating whether any debugging facilities are
   * turned off for a particular thread.
   * @review kiniry Are the isOff() methods necessary at all?
   */

  public synchronized boolean isOff(Thread thread)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(thread)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOff(java.lang.Thread)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((thread!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","isOff(java.lang.Thread)",474,"thread_non_null");
      jassResult = ( (!isOn(thread)));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "isOff(java.lang.Thread)", false), jassParameters);


      return jassResult;
  }
  
  /**
   * <p> Turns on class-global debugging facilities. </p>
   *
   * @concurrency GUARDED
   * @modifies isOn
   */

  public synchronized void turnOn()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "turnOn()", true), jassParameters);


    isOn = true;
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "turnOn()", false), jassParameters);

  }

  /**
   * <p> Turns off class-global debugging facilities. </p>
   *
   * @concurrency GUARDED
   * @modifies isOn
   */

  public synchronized void turnOff()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "turnOff()", true), jassParameters);


    isOn = false;
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "turnOff()", false), jassParameters);

  }

  /**
   * <p> Adds a category to the database of legal debugging categories.
   * Once a category exists in the database, its debugging level cannot be
   * changed. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@93efe</b>: (category!=null) &&<br><b>jass.reflect.AssertionLabel@6c6b2</b>: (category.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, categoryHashtable
   * @param category the category to add to the global set of 
   * categories.
   * @param level the debugging level associated with the passed
   * category.
   * @return a boolean indicating if the category was sucessfully
   * added to the database.  A false indicates either the category was
   * already in the database at a different level or the parameters
   * were invalid.
   */

  public synchronized boolean addCategory(String category, 
                                          int level)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(category), new jass.runtime.traceAssertion.Parameter(level)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategory(java.lang.String,int)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addCategory(java.lang.String,int)",523,"category_non_null");
      if (!((category.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","addCategory(java.lang.String,int)",524,"category_nonzero_length");

    // Get a reference to the global category hashtable.
    Hashtable categoryHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CATEGORIES"));
      jassResult = ( addCategoryToHashtable(categoryHashtable, category, level));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategory(java.lang.String,int)", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Removes a category to the database of legal debugging
   * categories. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@5ee671</b>: (category!=null) &&<br><b>jass.reflect.AssertionLabel@6b13c7</b>: (category.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, categoryHashtable
   * @param category the category to remove.
   * @return a boolean indicating if the category was sucessfully
   * removed from the database.  A false indicates that the parameters
   * were invalid.
   */

  public synchronized boolean removeCategory(String category)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(category)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeCategory(java.lang.String)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeCategory(java.lang.String)",547,"category_non_null");
      if (!((category.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeCategory(java.lang.String)",548,"category_nonzero_length");

    // Get a reference to the global category hashtable.
    Hashtable categoryHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CATEGORIES"));
      jassResult = ( removeCategoryFromHashtable(categoryHashtable, category));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeCategory(java.lang.String)", false), jassParameters);


      return jassResult;
  }


  /**
   * <p> Returns a boolean indicating if a category is in the class-global
   * category database. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@5f8f5e</b>: (category!=null) &&<br><b>jass.reflect.AssertionLabel@3d93f4</b>: (category.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies QUERY
   * @param category is the category to lookup.
   * @return a boolean indicating if a category is in the class-global
   * category database.
   */

  public synchronized boolean containsCategory(String category)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(category)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "containsCategory(java.lang.String)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","containsCategory(java.lang.String)",571,"category_non_null");
      if (!((category.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","containsCategory(java.lang.String)",572,"category_nonzero_length");

    // Get global category hashtable.
    Hashtable hashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CATEGORIES"));
      jassResult = ( (hashtable.containsKey(category)));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "containsCategory(java.lang.String)", false), jassParameters);

// If entry exists, return a true; otherwise return a false.

      return jassResult;
  }
  
  /**
   * <p> Returns an <code>Enumeration</code> that is the list of
   * class-global debugging categories that are currently in the category
   * database. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return an <code>Enumeration</code> that is the list of class-global
   * debugging categories that are currently in the category database.
   * @see Hashtable#elements
   */

  public synchronized Enumeration listCategories()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listCategories()", true), jassParameters);
      java.util.Enumeration jassResult;

    // Get global category hashtable.
    Hashtable hashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CATEGORIES"));
      jassResult = ( (hashtable.elements()));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listCategories()", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Adds a class the the class-global database of classes that
   * have debugging enabled. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@3ca5f1</b>: (classRef!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, classHashtable
   * @param classRef the class to add to the global table of classes
   * that have debugging enabled.
   */

  public synchronized void addClass(Class classRef)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(classRef)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addClass(java.lang.Class)", true), jassParameters);


      /* precondition */
      if (!((classRef!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addClass(java.lang.Class)",615,"classRef_non_null");

    //  Get a reference to the global class hashtable.
    Hashtable classHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CLASSES"));

    Utilities.addClassToHashtable(classHashtable, 
                                  classRef.getName());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addClass(java.lang.Class)", false), jassParameters);

  }

  /**
   * <p> Adds a class the the class-global database of classes that have
   * debugging enabled. Note that a class of "*" means that all classes
   * will now have debugging enabled.  There is no way to "undo" such a
   * command short of manually adding the individual classes back to the
   * database. (Or, equivalently, removing the complement.) </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@329f3d</b>: (className!=null) &&<br><b>jass.reflect.AssertionLabel@749757</b>: (className.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, classHashtable
   * @param className the name of the class to add.
   */

  public synchronized void addClass(String className)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(className)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addClass(java.lang.String)", true), jassParameters);


      /* precondition */
      if (!((className!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addClass(java.lang.String)",639,"className_non_null");
      if (!((className.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","addClass(java.lang.String)",640,"className_nonzero_length");

    //  Get a reference to the global class hashtable.
    Hashtable classHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CLASSES"));

    Utilities.addClassToHashtable(classHashtable, className);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addClass(java.lang.String)", false), jassParameters);

  }
  
  /**
   * <p> Removes a class the the class-global database of classes that have
   * debugging enabled. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@7bd6a1</b>: (classRef!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, classHashtable
   * @param classRef the class to remove.
   */

  public synchronized void removeClass(Class classRef)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(classRef)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeClass(java.lang.Class)", true), jassParameters);


      /* precondition */
      if (!((classRef!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeClass(java.lang.Class)",660,"classRef_non_null");

    //  Get a reference to the global class hashtable.
    Hashtable classHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CLASSES"));

    Utilities.removeClassFromHashtable(classHashtable, 
                                       classRef.getName());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeClass(java.lang.Class)", false), jassParameters);

  }

  /**
   * <p> Removes a class the the class-global database of classes that have
   * debugging enabled.  Removes a class from a database of
   * debugging-enabled classes.  Note that a class of "*" means that all
   * classes will be removed and debugging disabled.  There is no way to
   * "undo" such a command. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@47ee05</b>: (className!=null) &&<br><b>jass.reflect.AssertionLabel@5b9e68</b>: (className.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, classHashtable
   * @param className the name of the class to remove.
   */

  public synchronized void removeClass(String className)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(className)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeClass(java.lang.String)", true), jassParameters);


      /* precondition */
      if (!((className!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeClass(java.lang.String)",684,"className_non_null");
      if (!((className.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeClass(java.lang.String)",685,"className_nonzero_length");

    //  Get a reference to the global class hashtable.
    Hashtable classHashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CLASSES"));

    Utilities.removeClassFromHashtable(classHashtable, 
                                       className);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeClass(java.lang.String)", false), jassParameters);

  }


  /**
   * <p> Get the context for a specific thread. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@7cf0ce</b>: (thread!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies QUERY
   * @modifies threadHashtable
   * @param thread the thread that we are interested in.
   * @return the <code>Context</code> corresponding to thread, or
   * <code>null</code> if no such context exists.
   */

  public synchronized Context getContext(Thread thread)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(thread)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getContext(java.lang.Thread)", true), jassParameters);
      idebughc.Context jassResult;

      /* precondition */
      if (!((thread!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","getContext(java.lang.Thread)",709,"thread_non_null");

    if (threadHashtable.containsKey(thread)) {
        jassResult = ( (Context)(threadHashtable.get(thread)));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getContext(java.lang.Thread)", false), jassParameters);


        return jassResult;
    }
    else {
        jassResult = ( null);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getContext(java.lang.Thread)", false), jassParameters);


        return jassResult;
    }
  }

  /**
   * <p> Adds a context to the the class-global database of threads that
   * have debugging context. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@256ea2</b>: (debugContext!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable
   * @param debugContext is the context that we are interested in
   * adding.
   * @return a boolean indicating if the context was added to the
   * database sucessfully or that the thread was already in the
   * database.  A false indicates that the context was invalid.
   */

  public synchronized boolean addContext(Context debugContext)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(debugContext)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addContext(idebughc.Context)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((debugContext!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addContext(idebughc.Context)",731,"context_non_null");

    if (debugContext != null) {
      threadHashtable.put(debugContext.getThread(), debugContext);
            jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addContext(idebughc.Context)", false), jassParameters);


            return jassResult;
    } else {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addContext(idebughc.Context)", false), jassParameters);


        return jassResult;
    }
  }

  /**
   * <p> Removes a context from the the class-global database of
   * threads that have debugging context. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@2701e</b>: (debugContext!=null)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable
   * @param debugContext is the context that we are interested in
   * removing.
   * @return a boolean indicating if the context was removed from
   * the database sucessfully or that the thread was not in the
   * database at all.  A false indicates that the context was
   * invalid or not in the table.
   */

  public synchronized boolean removeContext(Context debugContext)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(debugContext)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeContext(idebughc.Context)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((debugContext!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeContext(idebughc.Context)",755,"context_non_null");

    if ((debugContext != null) && 
        (threadHashtable.containsKey(debugContext))) {
      threadHashtable.remove(debugContext);
            jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeContext(idebughc.Context)", false), jassParameters);


            return jassResult;
    } else {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeContext(idebughc.Context)", false), jassParameters);


        return jassResult;
    }
  }

  /**
   * <p> Returns an <code>Enumeration</code> that is the list of
   * class-global classes that have debugging enabled. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return an <code>Enumeration</code> that is the list of class-global
   * classes that currently have debugging enabled (they are in the class
   * database). Returns a null if a null is passed, otherwise a zero-length
   * Enumeration will be returned if there is no information on the thread
   * at all.
   * @see Hashtable#elements
   */

  public synchronized Enumeration listClasses()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listClasses()", true), jassParameters);
      java.util.Enumeration jassResult;

    // Get global category hashtable.
    Hashtable hashtable = 
      (Hashtable)(threadHashtable.get("GLOBAL_CLASSES"));
      jassResult = ( (hashtable.elements()));
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listClasses()", false), jassParameters);


      return jassResult;
  }
  
  /**
   * <p> Set a new class-global debugging level. </p>
   *
   * @concurrency GUARDED
   * @modifies level
   * @param level the new debugging level.
   * @return a boolean indicating whether the level change succeeded.
   * The only reason why a setLevel might fail is if the level passed
   * is out of range.
   */

  public synchronized boolean setLevel(int level)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(level)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "setLevel(int)", true), jassParameters);
      boolean jassResult;

    if ((level >= debugConstants.LEVEL_MIN) && 
        (level <= debugConstants.LEVEL_MAX)) {
      this.level = level;
            jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "setLevel(int)", false), jassParameters);


            return jassResult;
    } else {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "setLevel(int)", false), jassParameters);


        return jassResult;
    }
    
  }

  /**
   * <p> Returns the current class-global debugging level. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return the current class-global debugging level.
   */

  public synchronized int getLevel()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getLevel()", true), jassParameters);
      int jassResult;

      jassResult = ( level);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "getLevel()", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Returns an <code>Enumeration</code> that is the list of
   * class-global threads that have debugging enabled. </p>
   *
   * @concurrency GUARDED
   * @modifies QUERY
   * @return an <code>Enumeration</code> that is the list of class-global
   * threads that currently have debugging enabled (they are in the thread
   * database).
   * @see Hashtable#keys
   */

  public synchronized Enumeration listThreads()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listThreads()", true), jassParameters);
      java.util.Enumeration jassResult;

      jassResult = ( threadHashtable.keys());
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "listThreads()", false), jassParameters);


      return jassResult;
  }

  // Protected Methods
  // Package Methods
  // Private Methods

  /**
   * <p> Initialize all the static data-structures used by the
   * <code>Debug</code> class.  Note that the <code>initCategories()</code>
   * method is automatically called as necessary to initialize the default
   * categories database of the <code>Debug</code> class. </p>
   *
   * <dl><dt><b>Ensures:</b></dt><dd><code><b>jass.reflect.AssertionLabel@1a0c7c</b>: (threadHashtable!=null) &&<br><b>jass.reflect.AssertionLabel@29ae05</b>: (getAssert()!=null) &&<br><b>jass.reflect.AssertionLabel@5ff3a2</b>: (getCollect()==c) &&<br><b>jass.reflect.AssertionLabel@742b49</b>: (debugUtilities!=null) &&<br><b>jass.reflect.AssertionLabel@45e044</b>: (getDebugConstants()==dc)</code></dd></dl> 
@concurrency CONCURRENT
   * @modifies threadHashtable, debugConstants, assert, collect,
   *           debugUtilities
   * @param dc an implementation of the <code>DebugConstants</code> that
   * defines the semantics of this debug context.
   * @param collect an implementation of the <code>Collect</code> class.
   */

  private void init(DebugConstants dc, Collect c)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(dc), new jass.runtime.traceAssertion.Parameter(c)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "init(idebughc.DebugConstants,idebughc.Collect)", true), jassParameters);


      idebughc.Debug jassOld = (idebughc.Debug)this.clone();
    threadHashtable = new Hashtable();
    Hashtable categoryHashtable = new Hashtable();
    threadHashtable.put("GLOBAL_CATEGORIES", categoryHashtable);
    this.debugConstants = dc;
    debugConstants.initCategories(categoryHashtable);
    Hashtable classHashtable = new Hashtable();
    threadHashtable.put("GLOBAL_CLASSES", classHashtable);
    classHashtable.put("*", new Boolean(true));

    // Note that we need to actually initialize our own debugging context!
    this.assert = new Assert(this);
    this.collect = c;

    debugUtilities = new Utilities(this);
      /* postcondition */
      if (!((threadHashtable!=null))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",873,"threadHashtable_valid");
      if (!((jassInternal_getAssert()!=null))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",874,"assert_valid");
      if (!((jassInternal_getCollect()==c))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",875,"collect_valid");
      if (!((debugUtilities!=null))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",876,"debugUtilities_valid");
      if (!((jassInternal_getDebugConstants()==dc))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",877,"debugConstants_valid");
      if (!(isOn == jassOld.isOn && level == jassOld.level && jass.runtime.Tool.referenceEquals(debugOutputInterface,jassOld.debugOutputInterface))) throw new jass.runtime.PostconditionException("idebughc.Debug","init(idebughc.DebugConstants,idebughc.Collect)",-1,"Method has changed old value.");
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "init(idebughc.DebugConstants,idebughc.Collect)", false), jassParameters);

  }

  /**
   * <p> Adds a category to a hashtable of legal debugging categories.
   * Once a category exists in the database, its debugging level cannot be
   * changed without removing and re-adding the category to the
   * database. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@160f05</b>: (hashtable!=null) &&<br><b>jass.reflect.AssertionLabel@342cbf</b>: (category!=null) &&<br><b>jass.reflect.AssertionLabel@65b723</b>: (category.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies QUERY
   * @param hashtable the hashtable to remove the class from.
   * @param category the category to add to the set of defined
   * categories.
   * @param level the debugging level associated with the passed
   * category.
   * @return a boolean indicating if the category was sucessfully
   * added to the database.  A false indicates either the category was
   * already in the database at a different level or the parameters
   * were invalid.
   */

  private synchronized boolean 
    addCategoryToHashtable(Hashtable hashtable, String category, int level)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(hashtable), new jass.runtime.traceAssertion.Parameter(category), new jass.runtime.traceAssertion.Parameter(level)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((hashtable!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)",904,"hashtable_non_null");
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)",905,"category_non_null");
      if (!((category.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)",906,"category_nonzero_length");

    // See if an entry for the passed category exists.
    if (hashtable.containsKey(category)) {
      if (((Integer)(hashtable.get(category))).intValue() != level) {
          jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)", false), jassParameters);


          return jassResult;
      }
      else {
          jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)", false), jassParameters);


          return jassResult;
      }
    }

    // Add a new entry for the passed category.
    hashtable.put(category, new Integer(level));
      jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "addCategoryToHashtable(java.util.Hashtable,java.lang.String,int)", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Removes a category from a database of legal debugging
   * categories. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@5a8767</b>: (hashtable!=null) &&<br><b>jass.reflect.AssertionLabel@6f7ce9</b>: (category!=null) &&<br><b>jass.reflect.AssertionLabel@71bbc9</b>: (category.length()>0)</code></dd></dl> 
@concurrency GUARDED
   * @modifies threadHashtable, categoryHashtable
   * @param hashtable is the thread that we are interested in.
   * @param category the category to remove.
   * @return a boolean indicating if the category was sucessfully
   * removed from the database.  A false indicates that the parameters
   * were invalid.
   */

  private synchronized boolean 
    removeCategoryFromHashtable(Hashtable hashtable, String category)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(hashtable), new jass.runtime.traceAssertion.Parameter(category)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeCategoryFromHashtable(java.util.Hashtable,java.lang.String)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((hashtable!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeCategoryFromHashtable(java.util.Hashtable,java.lang.String)",937,"hashtable_non_null");
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeCategoryFromHashtable(java.util.Hashtable,java.lang.String)",938,"category_non_null");
      if (!((category.length()>0))) throw new jass.runtime.PreconditionException("idebughc.Debug","removeCategoryFromHashtable(java.util.Hashtable,java.lang.String)",939,"category_nonzero_length");

    // If is in the hashtable, remove it.
    if (hashtable.containsKey(category))
      hashtable.remove(category);
      jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "removeCategoryFromHashtable(java.util.Hashtable,java.lang.String)", false), jassParameters);


      return jassResult;
  }

    /* --- The following methods of class idebughc.Debug are generated by JASS --- */


  

  public DebugOutput jassInternal_getOutputInterface()
  {
    Thread currentThread = Thread.currentThread();
    
    if (threadHashtable.containsKey(currentThread)) {
      Context debugContext = 
        (Context)(threadHashtable.get(currentThread));
      return debugContext.getOutputInterface();
    } else return this.debugOutputInterface;
  }


  

  public Assert jassInternal_getAssert()
  {
    return assert;
  }



  

  public Collect jassInternal_getCollect()
  {
    return collect;
  }


  

  public DebugConstants jassInternal_getDebugConstants()
  {
    return debugConstants;
  }


    protected void finalize () throws java.lang.Throwable {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "finalize()", true), jassParameters);
        super.finalize();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "finalize()", false), jassParameters);
    }

    public boolean equals (java.lang.Object par0) {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(par0)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "equals(java.lang.Object)", true), jassParameters);
        boolean returnValue = super.equals(par0);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "equals(java.lang.Object)", false), jassParameters);
        return returnValue;
    }

    public java.lang.String toString () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "toString()", true), jassParameters);
        java.lang.String returnValue = super.toString();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Debug", "toString()", false), jassParameters);
        return returnValue;
    }
  
} // end of class Debug

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End:
 */

