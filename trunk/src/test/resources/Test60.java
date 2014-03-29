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

import java.util.Hashtable;

/**
 * <p> The core interface to gathering statistics. </p>
 *
 * <p> Users of IDebug wishing to keep statistics on their system need to
 * inherit from this abstract class and implement the protected methods.
 * The simplest means to collect statistics are to use a hashtable keyed on
 * statistic (since their hashCode is valid) and store Double objects
 * corresponding to the current value of that statistic.  See
 * <code>idebughc.SimpleCollect</code> for an example of this
 * implementation which you can reuse. </p>
 *
 * @idea Alternative implementations that have some or all of the following
 * characteristics are encouraged.  Ideas include collectors that:
 * <ul>
 * <li> send logging information at certain time intervals or trigger
 * values. </li> 
 * <li> log statistic trace sets to do data analysis over long time
 * intervals. </li> 
 * <li> compute means and variances so that accurate characterization of
 * the data is available. </li> 
 * <li> detect significant changes in system behavior or performance and
 * can initiate early warning systems or preventive maintenence. </li>
 * <li> utilize system debugging context to log certain statistics and not
 * others (using the level and type of <code>Event</code> and the
 * <code>isValidCategory()</code> and <code>isValidLevel()</code> methods
 * herein). </li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Joseph R. Kiniry <joe@kindsoftware.com>
 * @history BON design for Dali by Donnie, Todd, and Joe.  Adopted during
 * design phase of DALi into IDebug.
 * @see Statistic
 * @see idebug.SimpleCollect 
 */

public abstract class Collect
{
  // Attributes

  /**
   * <p> A <code>Hashtable</code> used to track statistics
   * definitions. </p>
   */

  private Hashtable statistics;
  
  /**
   * <p> The <code>Debug</code> object associated with this
   * <code>Collect</code> object. </p>
   *
   * @modifies SINGLE-ASSIGNMENT
   */

  private Debug debug;

  // Inherited Methods
  // Constructors
  
  /**
   * <p> Construct a new <code>Collect</code> class. </p>
   */

  public Collect()
  {
    this.statistics = new Hashtable();

    /** ensure [statistics_initialized] (statistics != null); **/
  }

  // Public Methods

  /**
   * <p> Checks a debug instance to make sure its <code>collect</code>
   * attribute references this <code>Collect</code> object. </p>
   *
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @param d the debug instance to check.
   */

  public boolean checkDebugCollectRef(Debug d)
  {
    /** require [d_non_null] (d != null); **/

    return (d.collect == this);

    /** ensure [result_correct] (Result == (d.collect == this)); **/
  }

  /**
   * <p> Set the debug instance associated with this collect instance.
   * This method <strong>must</strong> be called with the correct debug
   * instance prior to using <strong>any</strong> of the methods of this
   * <code>Collect</code> instance. </p>
   *
   * @concurrency CONCURRENT
   * @modifies debug
   * @param d the debug object associated with this <code>Collect</code>
   * object.
   */

  public final void setDebug(Debug d)
  {
    /** require [d_non_null] (d != null);
                [correct_debug_instance] checkDebugCollectRef(d); **/
        
    debug = d;

    /** ensure [debug_valid] (debug == d); **/
  }

  /**
   * <p> Register a statistic with the collector. </p>
   *
   * @concurrency CONCURRENT
   * @modifies statistics
   * @param statistic the statistic to register.
   */

  public void register(Statistic statistic)
  {
    /** require [statistic_non_null] (statistic != null);
                [statistic_id_unchanged] checkStatisticID(statistic); **/

    statistics.put(statistic, statistic);

    /** ensure [statistic_registered] isRegistered(statistic); **/
  }

  /**
   * <p> Check the ID of a statistic and make sure that it hasn't changed
   * since it was registered. </p>
   *
   * @concurrency CONCURRENT
   * @modifies QUERY
   * @param statistic the statistic to check.
   */

  public boolean checkStatisticID(Statistic statistic)
  {
    /** require [statistic_non_null] (statistic != null); **/

    Object oldValue = statistics.get(statistic);
    if(oldValue != null) {
      // make sure value hasn't changed.
      return (oldValue == statistic);
    }
    return true;
  }

  /**
   * <p> Unregister a statistic with the collector. </p>
   *
   * @concurrency CONCURRENT
   * @modifies statistics
   * @param statistic the statistic to unregister.
   */

  public void unregister(Statistic statistic)
  {
    /** require [statistic_non_null] (statistic != null); **/

    statistics.remove(statistic);

    /** ensure [statistic_unregistered] !isRegistered(statistic); **/
  }

  /**
   * <p> Check to see if a statistic is registered yet. </p>
   *
   * @param statistic the statistic to check.
   * @postcondition (Result == true) iff register(statistic) took place at
   * some point in time in the execution trace of this collect instance.
   */

  public boolean isRegistered(Statistic statistic)
  {
    /** require [statistic_non_null] (statistic != null); **/

    return(statistics.get(statistic) == statistic);
  }

  /**
   * <p> What is the current value for specific statistic? </p>
   *
   * @param statistic the statistic being modified.
   * @return the old value of the statistic.
   */

  abstract public double currentValue(Statistic statistic);

  /**
   * <p> Report on a particular statistic. </p>
   *
   * @param statistic the statistic being reported on.
   * @return a report on the statistic, typically encapsulated in some type
   * of <code>Report</code> object or just a simple <code>String</code>
   * textual report.
   */

  abstract public Object report(Statistic statistic);

  /**
   * <p> Report on all statistics. </p>
   *
   * @return a report on all statistics, typically encapsulated in some
   * type of Report object or just a simple String textual report.
   */

  abstract public Object reportAll();

  /**
   * <p> Increment a statistic by a specified value. </p>
   *
   * @param statistic the statistic being modified.
   * @param value the amount to increment the statistic.
   * @return the old value of the statistic.
   */

  abstract public double increment(Statistic statistic, double value);
  
  /**
   * <p> Increment a statistic by the default value. </p>
   *
   * @param statistic the statistic being modified.
   * @return the old value of the statistic.
   */

  abstract public double increment(Statistic statistic);
 
  /**
   * <p> Decrement a statistic by a specified value. </p>
   *
   * @param statistic the statistic being modified.
   * @param value the amount to decrement the statistic.
   * @return the old value of the statistic.
   */

  abstract public double decrement(Statistic statistic, double value);

  /**
   * <p> Decrement a statistic by the default value. </p>
   *
   * @param statistic the statistic being modified.
   * @return the old value of the statistic.
   */

  abstract public double decrement(Statistic statistic);

  /**
   * <p> Reset a statistic to the default start value. </p>
   *
   * @param statistic the statistic to reset.
   * @return the old value of the statistic.
   */

  abstract public double reset(Statistic statistic);
  
  /**
   * <p> Set a statistic to a specific value. </p>
   *
   * @param statistic the statistic being modified.
   * @param value the new value of the statistic.
   * @return the old value of the statistic.
   */

  abstract public double set(Statistic statistic, double value);

  // Protected Methods

  /**
   * <p> Tests to see if the current debug context is interested in a given
   * category. </p>
   *
   * @param category the category to inspect.
   * @return a boolean indicating if the category in question is valid at
   * this time for this context (i.e. debugging framework state, thread,
   * class invoking the method, etc.)
   * @see Context
   */

  protected final boolean isValidCategory(String category)
  {
    /** require [category_non_null] (category != null); **/

    return debug.debugUtilities.categoryTest(category);
  }

  /**
   * <p> Tests to see if the current debug context is interested in a given
   * level. </p>
   *
   * @param level the level to inspect.
   * @return a boolean indicating if the level in question is valid at this
   * time for this context (i.e. debugging framework state, thread, class
   * invoking the method, etc.)
   * @see Context
   */

  protected final boolean isValidLevel(int level)
  {
    return debug.debugUtilities.levelTest(level);
  }
  
  // Package Methods
  // Private Methods

} // end of class Collect

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End: 
 */
