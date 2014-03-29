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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

/**
 * <p> A collection of shared algorithms for the this package. </p>
 *
 * @version $Revision$ $Date$
 * @author Joseph R. Kiniry <joe@kindsoftware.com>
 * @see Debug
 * @see Context
 */

class Utilities
{
  // Attributes

  /**
   * <p> The Debug object associated with this object. </p>
   */

  private Debug debug;

  // Inherited Methods
  // Constructors

  /**
   * Construct a new Utilities class.
   *
   * @param d the debug instance for this utility class.
   */

  Utilities(Debug d)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(d)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "Utilities(idebughc.Debug)", true), jassParameters);


      /* precondition */
      if (!((d!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","Utilities(idebughc.Debug)",77,"d_non_null");

    this.debug = d;
      /* postcondition */
      if (!((debug==d))) throw new jass.runtime.PostconditionException("idebughc.Utilities","Utilities(idebughc.Debug)",81,"debug_is_valid");
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "Utilities(idebughc.Debug)", false), jassParameters);

  }

  // Public Methods

  /**
   * <p> Prints the stack trace of the current thread to the current
   * debugging output stream. </p>
   *
   * @concurrency GUARDED
   */

  public static synchronized void printStackTrace()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "printStackTrace()", true), jassParameters);


    Throwable throwable = new Throwable();
    throwable.printStackTrace();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "printStackTrace()", false), jassParameters);

  }

  /**
   * <p> Dumps the current stack and but <em>doesn't</em> shut down the
   * current thread.  This method can be called from any thread
   * context. </p>
   *
   * @concurrency GUARDED
   * @see java.lang.Thread#dumpStack
   */

  public static synchronized void dumpStackSafe()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "dumpStackSafe()", true), jassParameters);


    Thread currentThread = Thread.currentThread();
    currentThread.dumpStack();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "dumpStackSafe()", false), jassParameters);

  }

  // Protected Methods
  // Package Methods

  /**
   * <p> Adds a class to a hashtable of class-specific enabled debugging.
   * If the current class debugging context is "*", adding a class has no
   * effect.  If adding the context "*", the database is cleared and the
   * "*" in inserted. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@5e3974</b>: (hashtable!=null) &&<br><b>jass.reflect.AssertionLabel@df503</b>: (className!=null)</code></dd></dl>
@concurrency GUARDED
   * @param hashtable the hashtable to which the class is added.
   * @param className the name of the class to add to the set of classes
   * that have debugging enabled.
   */

  static synchronized void addClassToHashtable(Hashtable hashtable,
                                               String className)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(hashtable), new jass.runtime.traceAssertion.Parameter(className)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "addClassToHashtable(java.util.Hashtable,java.lang.String)", true), jassParameters);


      /* precondition */
      if (!((hashtable!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","addClassToHashtable(java.util.Hashtable,java.lang.String)",132,"hashtable_non_null");
      if (!((className!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","addClassToHashtable(java.util.Hashtable,java.lang.String)",133,"className_non_null");

    // If we are adding "*", the tabled should be cleared and the "*"
    // should be inserted.
    if (className.equals("*")) {
      hashtable.clear();
      hashtable.put("*", new Boolean(true));
    } else
      // See if an entry for the passed class exists.
      if (!hashtable.containsKey(className)) {
        // If a "*" is in the table, then don't bother adding at all, just
        // return true.
        if (hashtable.containsKey("*"))
          return;
        else
          // Add a new entry for the passed class.
          hashtable.put(className, null);
      }
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "addClassToHashtable(java.util.Hashtable,java.lang.String)", false), jassParameters);

  }

  /**
   * <p> Removes a class from a database of debugging-enabled classes.
   * Note that a class of "*" means that all classes will be removed
   * and debugging disabled.  There is no way to "undo" such a
   * command.  Adding classes after the removal of "*" works as you
   * would expect. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@50d89c</b>: (hashtable!=null) &&<br><b>jass.reflect.AssertionLabel@3d0dd4</b>: (className!=null)</code></dd></dl>
@concurrency GUARDED
   * @param hashtable the hashtable from which the class is removed.
   * @param className the name of the class to remove.
   */

  static synchronized void removeClassFromHashtable(Hashtable hashtable,
                                                    String className)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(hashtable), new jass.runtime.traceAssertion.Parameter(className)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "removeClassFromHashtable(java.util.Hashtable,java.lang.String)", true), jassParameters);


      /* precondition */
      if (!((hashtable!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","removeClassFromHashtable(java.util.Hashtable,java.lang.String)",168,"hashtable_non_null");
      if (!((className!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","removeClassFromHashtable(java.util.Hashtable,java.lang.String)",169,"className_non_null");

    // If we are removing the class "*", just clear the hashtable.
    if (className.equals("*")) {
      hashtable.clear();
    } else
      // If entry is in the hashtable, remove it.
      if (hashtable.containsKey(className))
        hashtable.remove(className);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(null, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "removeClassFromHashtable(java.util.Hashtable,java.lang.String)", false), jassParameters);

  }

  /**
   * <p> Tests to see if the current debug context warrants output. </p>
   *
   * @concurrency GUARDED
   * @param level The debugging level of this message.
   */

  synchronized boolean levelTest(int level)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(level)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", true), jassParameters);
      boolean jassResult;

    // Get the current thread.
    Thread currentThread = Thread.currentThread();

    // Check to see if global-debugging is enabled.
    if (debug.isOn()) {
      // Global debugging is enabled, so check the current global
      // debugging level and, if it is greater than or equal to the
      // passed debugging level, print out the message.

      if ((level >= debug.getLevel()) && sourceClassValid()) {
          jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


          return jassResult;
      }
      else {
          jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


          return jassResult;
      }
    }

    // Global debugging is not enabled, so check per-thread debugging.

    // Check to see if this thread has a debugging context.
    Context debugContext = debug.getContext(currentThread);

    // If there is no context, we should not give the ok to print.
    if (debugContext == null) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


        return jassResult;
    }

    // Check to see if this thread has debugging enabled.
    if (debugContext.isOn() == false) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


        return jassResult;
    }

    // Now, see the current per-thread debugging level is >= the
    // passed debugging level.  If this condition holds, print the
    // message.

    if ((level >= debugContext.getLevel()) && sourceClassValid()) {
        jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


        return jassResult;
    }
      jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "levelTest(int)", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Tests to see if the current debug context warrants output. </p>
   *
   * <dl><dt><b>Requires:</b></dt><dd><code><b>jass.reflect.AssertionLabel@35f53a</b>: (category!=null)</code></dd></dl>
@concurrency GUARDED
   * @return a boolean indicating if the context warrants output.
   * @param category is the category of this message.
   */

  synchronized boolean categoryTest(String category)
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(category)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", true), jassParameters);
      boolean jassResult;

      /* precondition */
      if (!((category!=null))) throw new jass.runtime.PreconditionException("idebughc.Utilities","categoryTest(java.lang.String)",237,"category_non_null");

    int categoryLevel = 0;

    // Get the current thread.
    Thread currentThread = Thread.currentThread();

    // Check to see if global-debugging is enabled.
    if (debug.isOn()) {
      // Get a reference to the global category hashtable.
      Hashtable categoryHashtable =
        (Hashtable)(debug.threadHashtable.get("GLOBAL_CATEGORIES"));

      // If this category is not defined in the global hashtable,
      // we break out of the global checks and start the per-thread
      // checks.
      if (categoryHashtable.containsKey(category)) {
        // Get the debugging level of this defined global category.

        categoryLevel =
          ((Integer)(categoryHashtable.get(category))).intValue();

        // Global debugging is enabled, the category is defined in the
        // global database, so check the current global debugging level
        // and, if it is greater than or equal to the debugging level of
        // the passed category, print out the message.

        if ((categoryLevel >= debug.getLevel()) && sourceClassValid()) {
            jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


            return jassResult;
        }
        else {
            jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


            return jassResult;
        }
      }
    }

    // Global debugging is not enabled, so check per-thread debugging.

    // Check to see if this thread has a debugging context.
    Context debugContext = debug.getContext(currentThread);


    // If there is no context, we should not give the ok to print.
    if (debugContext == null) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


        return jassResult;
    }

    // Check to see if this thread has debugging enabled.
    if (debugContext.isOn() == false) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


        return jassResult;
    }

    // Check to see if this category is defined for the current thread.
    if (!debugContext.containsCategory(category)) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


        return jassResult;
    }

    // The current thread has context, debugging is enabled, the
    // category is defined, so get the per-thread debugging level of
    // this defined per-thread category.
    categoryLevel = debugContext.getCategoryLevel(category);

    // Now, see the current per-thread debugging level is >= the
    // per-thread category debugging level of the passed category.  If
    // this condition holds, print the message.

    if ((categoryLevel >= debugContext.getLevel()) && sourceClassValid()) {
        jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


        return jassResult;
    }
      jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "categoryTest(java.lang.String)", false), jassParameters);


      return jassResult;
  }

  /**
   * <p> Tests to see whether the object performing the debugging action is
   * permitted to print in the current debugging context. </p>
   *
   * @concurrency GUARDED
   * @return a true if the object performing the debugging action
   * is permitted to print in the current debugging context.
   */

  synchronized boolean sourceClassValid()
  {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", true), jassParameters);
      boolean jassResult;

    int index, startIndex, parenIndex;
    Throwable throwable;
    StringWriter stringWriter;
    PrintWriter printWriter;
    StringBuffer stringBuffer;
    String string, matchString, className;
    Hashtable classHashtable;

    // Create a new Throwable object so that we can get a snapshot of
    // the current execution stack.  Snapshot the stack into a
    // StringBuffer that we can parse.
    throwable = new Throwable();
    stringWriter = new StringWriter();
    printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);

    // Now stringWriter contains a textual snapshot of the current
    // execution stack.  We need to pull lines out of it until we find
    // the last line containing the string "at idebug.".  The very
    // next line is the stack level of the object that called the
    // IDebug method in question.  We need to strip out its name, then
    // compare it with the database of classes that have debugging
    // enabled.

    stringBuffer = stringWriter.getBuffer();
    string = stringBuffer.toString();
    // Match to the last occurance of a idebug stack frame.
    matchString = new String("idebug.");
    index = string.lastIndexOf(matchString);
    // Bump the index past the matched string.
    startIndex = index + matchString.length() + 1;
    // Match forward to the beginning of the classname for the next
    // stack frame (the object that called a idebug method).
    index = string.indexOf("at ", startIndex);
    // Bump up the index past the "at ".
    index += 3;
    // Grab out the class name of the next stack frame.
    parenIndex = string.indexOf("(", index);
    // So, everything between index and parenIndex is the class name
    // that we are interested in.
    className = string.substring(index, parenIndex);
    // Strip off the last part past the last ".", it is a method name.
    index = className.lastIndexOf(".");
    className = className.substring(0, index);

    // Now, we have the name of the class that called the debugging
    // routine.  It is stored in className.

    // See if global debugging is enabled.
    if (debug.isOn()) {
      // It is, so see if this class is included in the list of
      // classes that have debugging enabled.
      classHashtable =
        (Hashtable)(debug.threadHashtable.get("GLOBAL_CLASSES"));

      // If "*" is in the hashtable, then we are testing for reductive
      // specification of classes.  I.e. if the class is not in the table
      // _and_ "*" is in the table, then we _should_ return a true.  If "*"
      // is _not_ in the table and the class _is_, then we _should_ return
      // a true.  We do not return a false here because there is still the
      // possibility that per-thread context will specify that output
      // should appear.

      if ((classHashtable.containsKey("*")) &&
          (!classHashtable.containsKey(className))) {
          jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


          return jassResult;
      }
      else
        if (classHashtable.containsKey(className)) {
            jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


            return jassResult;
        }
    }

    // Either global debugging isn't enabled or the global debugging
    // context didn't specify that output should appear. So, now we check
    // the per-thread context.

    Thread currentThread = Thread.currentThread();

    // If there is no per-thread context for the current thread, return a
    // false.

    if (!debug.threadHashtable.containsKey(currentThread)) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


        return jassResult;
    }

    // The table has the key, so get the record for this thread.
    Context debugContext =
      (Context)(debug.threadHashtable.get(currentThread));

    // Is debugging turned on at all for this thread? If not, return a
    // false.
    if (!debugContext.isOn()) {
        jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


        return jassResult;
    }

    // Debugging is enabled for this thread, so perform the same check as
    // above to see if the calling class should output debugging
    // information.  This time, if we fail, we fail.
    if (debugContext.containsClass("*")) {
      if (debugContext.containsClass(className)) {
          jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


          return jassResult;
      }
      else {
          jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


          return jassResult;
      }
    } else {
      if (debugContext.containsClass(className)) {
          jassResult = ( true);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


          return jassResult;
      }
      else {
          jassResult = ( false);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(jassResult)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "sourceClassValid()", false), jassParameters);


          return jassResult;
      }
    }
  }

    protected void finalize () throws java.lang.Throwable {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "finalize()", true), jassParameters);
        super.finalize();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "finalize()", false), jassParameters);
    }

    public boolean equals (java.lang.Object par0) {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(par0)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "equals(java.lang.Object)", true), jassParameters);
        boolean returnValue = super.equals(par0);
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "equals(java.lang.Object)", false), jassParameters);
        return returnValue;
    }

    public java.lang.String toString () {
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jass.runtime.traceAssertion.Parameter[] jassParameters; jassParameters = new jass.runtime.traceAssertion.Parameter[] {}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "toString()", true), jassParameters);
        java.lang.String returnValue = super.toString();
    jass.runtime.traceAssertion.CommunicationManager.internalAction = true; jassParameters = new jass.runtime.traceAssertion.Parameter[] {new jass.runtime.traceAssertion.Parameter(returnValue)}; jass.runtime.traceAssertion.CommunicationManager.internalAction = false; jass.runtime.traceAssertion.CommunicationManager.communicate(this, new jass.runtime.traceAssertion.MethodReference("idebughc", "Utilities", "toString()", false), jassParameters);
        return returnValue;
    }

  // Private Methods

} // end of class Utilities

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End:
 */

