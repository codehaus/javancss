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

import java.io.Serializable;
import java.util.Date;

/**
 * <p> Event is a utility base class from which all log/monitoring events
 * can derive or have embedded. </p>
*
 * @version $Revision$ $Date$
 * @history BON design for Dali by Donnie, Todd, and Joe.  Adopted during
 * design phase of Dali into IDebug.
 * @author Joseph Kiniry <joe@kindsoftware.com <joe@kindsoftware.com>
* @bon Represents a single important event of any kind. The event includes
 * a source, description, importance, and time, among other things.
 *
 * @invariant ((sourceHost != null) && (sourceComponent != null) &&
* (creationDate != null) && (description != null) && (type != null)) 
 * All fields of event are set only once, at construction-time.
 * @invariant (true) level must be a legal level.
 * @invariant (true) Two events are considered to be identical if all
 * fields of both events have identical values.
 * @concurrency (CONCURRENT) All methods are getters, thus there are no
 * concurrency issues. 
 * @modifies (SINGLE-ASSIGNMENT-FIELDS) All attributes are set only on
 * construction. 
 * @modifies (QUERY-METHODS) All methods are functions.
 *
 * @design The class invariants covers the legal values of all attributes,
 * thus no values tag is necessary in the specification.
 * @bon Events cannot be cloned. 
 */

public abstract class Event extends Object implements Serializable
{
  // Attributes

  /**
   * The source host for this event.
   *
   * @see #getSourceHost
   * @example sourceHost = "joe.kindsoftware.com"
   */

  private String sourceHost;

  /**
   * The source component for this event.
   *
   * @see #getSourceComponent
   * @example sourceComponent = "Monitoring System, version 0.1.0"
   */

  private String sourceComponent;

  /**
   * The date on which this event was created.
   *
   * @see #getCreationDate
   */

  private Date creationDate;  

  /**
   * The text description of this event.
   *
   * @see #getDescription
   * @example description = "Available memory is beneath 1.0 MB."
   */

  private String description;

  /**
   * The "type" of this event.
   *
   * @see #getType
   * @see Monitoring.txt Monitoring system overview and specification.
   * @example type = "MEMORY_WARNING"
   */

  private String type;

  /**
   * The "level" of this event.
   *
   * @see #getLevel
   * @see Monitoring.txt Monitoring system overview and specification.
   * @see IDebug System documentation.
   * @see DebugConstants Look for the monitoring system's IDebug constants and
   * specification. 
   * @example level = debugConstants.WARNING
   */

  private int level;

  // Inherited methods

  /**
   * <p> What is a printable representation of this event? </p>
*
   * @pre -- none
   * @postcondition Result = "[short-date/time-form | sourceHost | 
   *                           sourceComponent] type : level -\n\tDescription"
   * @overrides java.lang.Object.toString()
   * @return a printable representation of the event.
   * @complexity O(1) - Note that String catenation is very expensive, so use
   * this method sparingly.  Also, the length of the output is directly
   * proportional to the event description. 
   */

  public String toString()
  {
    return "[" + creationDate.toString() + " | " + sourceHost + " | " +
      sourceComponent + "] " + type + ":" + level + " - " + description;
  }

  /**
   * <p> Is this event equal to another one? </p>
*
   * @return if two Events are equal.
   * @pre ((obj != null) && (obj instanceof Event))
   * @ensures (Result == true) iff (for_all attributes of Event : attributes of
   * the objects being compared have identical values)
   * @overrides java.lang.Object.equals()
   */

  public boolean equals(Object obj)
  {
    if (obj instanceof Event)
    {
      Event e = (Event)obj;
      return(sourceHost.equals(e.getSourceHost()) &&
sourceComponent.equals(e.getSourceComponent()) && creationDate.equals(e.getCreationDate()) &&
description.equals(e.getDescription()) && type.equals(e.getType()) && level == e.getLevel());
    }
    else return false;
  }
  
  /*
   * The default semantics for the following methods have been confirmed as
   * being correct for the class Event:
   *
   * protected void finalize()
   * int hashCode()
   */

  // Constructors

  /**
   * <p> This the standard constructor for Event.  No other constructor can
   * be legally used. </p>
*
   * @param sourceH The source host for the event.
   * @param sourceC The source component for the event.
   * @param desc An English description of the event.
   * @param t The type of the event.
   * @param l The level of the event.
   *
   * @bon Create a new event with the following specification.
   * @see dali.monitoring.DebugConstants
   * @pre ((sourceH != null) && (sourceH != null) && (sourceH != null) &&
* (sourceH != null)) 
   * @postcondition (Result.getSourceHost().equals(sourceH) &&
*                 Result.getSourceComponent().equals(sourceC) && *                 Result.getDescription().equals(desc) && *                 Result.getType().equals(t) && *                 Result.getLevel() == l)
   * @ensures Result.getCreationDate() returns a time that is between the
   * time in which this method is called and it returns.
   * @generates A new, valid instance of Event.
   */

  public Event(String sourceH, String sourceC, String desc, String t, int l)
  {
    this.sourceHost = sourceH;
    this.sourceComponent = sourceC;
    this.creationDate = new Date();
    this.description = desc;
    this.type = t;
    this.level = l;
  }
  
  // Public Methods

  /**
   * <p> What is the source system of this event? </p>
*
   * @design Original examples show source host being a textual machine name
   * and/or port number, but this isn't a requirement.
   * @pre -- none
   * @postcondition (Result.equals(sourceHost))
   * @return the source system for this event.
   */
  
  public String getSourceHost()
  {
    return sourceHost;
  }
  
  /**
   * <p> What is the source component of this event? </p>
*
   * @design Original examples show source component being a textual name
   * of a component and a version number, but this isn't a requirement.
   * @pre -- none
   * @postcondition (Result.equals(sourceComponent))
   * @return the source component of this event.
   */
  
  public String getSourceComponent()
  {
    return sourceComponent;
  }

  /**
   * <p> When was this event generated? </p>
*
   * @pre --none
   * @postcondition (Result.equals(creationDate))
   * @return the time at which this event was generated.
   */
  
  public Date getCreationDate()
  {
    return creationDate;
  }

  /**
   * <p> What is the description of this event? </p>
*
   * @pre --none
   * @postcondition (Result.equals(description))
   * @return the description of this event.
   */
  
  public String getDescription()
  {
    return description;
  }

  /**
   * <p> What type of event is this? </p>
*
   * @pre --none
   * @postcondition (Result.equals(type))
   * @return the type of this event.
   * @see Monitoring.txt Monitoring system overview and specification.
   * @see IDebug System documentation.
   * @see DebugConstants Look for the monitoring system's IDebug constants and
   * specification. 
   */
  
  public String getType()
  {
    return type;
  }

  /**
   * <p> How important is this event? </p>
*
   * @pre --none
   * @postcondition (Result == level)
   * @return the level of importance of this event.
   * @see Monitoring.txt Monitoring system overview and specification.
   * @see IDebug System documentation.
   * @see DebugConstants Look for the monitoring system's IDebug constants and
   * specification. 
   */
  
  public int getLevel()
  {
    return level;
  }

  // Protected Methods
  // Package Methods
  // Private Methods
} // end of class Event

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End:
 */
