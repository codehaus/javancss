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

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.*;

/**
 * <p> The primary class used to send messages to a window created by
 * the IDebug framework. </p>
 *
 * @version $Revision$ $Date$
 * @author Joseph R. Kiniry <joe@kindsoftware.com>
 * @concurrency (GUARDED) All methods are synchronized.
 * @see Context
 * @see Debug
 *
 * @todo Can we actually provide a valid Writer for getWriter()?
 */

public class WindowOutput extends DebugOutputBase
  implements DebugOutput, Cloneable
{
  // Attributes

  // The text area used by this class.

  private JTextArea textArea = null;

  // Inherited Methods

  public synchronized void printMsg(String category, String message)
  {
    textArea.append("<" + category + ">: " + message);
  }

  public synchronized void printMsg(int level, String message)
  {
    textArea.append("[" + level + "]: " + message);
  }

  public synchronized void printMsg(String message)
  {
    textArea.append(message);
  }

  public synchronized Writer getWriter()
  {
    return null;
  }

  public Object clone()
  {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new RuntimeException(cnse.getMessage());
    }
  }

  // Constructors

  /**
   * Construct a new WindowOutput class.
   *
   * @param debug the Debug class associated with this WindowOutput.
   */

  public WindowOutput(Debug d)
  {
    /** require [d_non_null] (d != null); **/

    this.debug = d;

    // set up swing components.
    createUI();

    /** ensure [debug_valid] (debug == d);
               changeonly{debug}; **/
  }

  // Protected Methods
  // Package Methods
  // Private Methods

  /**
   * Build the user interface.  The UI consists of a JFrame containing a
   * small set of components: a text area with a scrollbar for debugging
   * messages, a "clear" button to clear the current messages, and a "save"
   * button to save the current debugging messages to a textfile.
   */

  private void createUI()
  {
    // Create the top-level container and add contents to it.
    JFrame frame = new JFrame("IDebug Output");

    // Create the text area to write into and a surrounding scrollpane so
    // that the user can look back at text that has scrolled by.
    textArea = new JTextArea(30, 80);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    JScrollPane areaScrollPane =
      new JScrollPane(textArea,
                      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    // Create the "clear" button that clears the text in textArea.
    JButton clearButton = new JButton("Clear");
    clearButton.setToolTipText("Clear debugging log.");
    clearButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // react to clear command
          textArea.setText("");
        }
      });

    // Create the "save" button that lets the user save the contents of
    // textArea to a file.
    JButton saveButton = new JButton("Save");
    saveButton.setToolTipText("Save debugging log to a file.");
    saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // react to save command
          String textOfLog = textArea.getText();
          FileDialog dialog =
            new FileDialog(null, "Save debug log to which file?",
                           FileDialog.SAVE);
          dialog.show();
          String filename = dialog.getFile();
          if (filename == null)
            return;
          File file = new File(filename);
          if (!file.isFile()) {
            System.err.println(filename + " is not a file!");
            return;
          }
          if (!file.canWrite()) {
            System.err.println(filename + " is not a writable file!");
            return;
          }
          try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(textOfLog);
            fileWriter.close();
          } catch (IOException ioe) {
            System.err.println("Error while writing debug log to file " +
                               filename);
          }
        }
      });

    // Create a panel into which we'll put the buttons.
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(
                                                   30, //top
                                                   30, //left
                                                   10, //bottom
                                                   30) //right
                   );
    panel.setLayout(new GridLayout(1, 2));
    panel.add(clearButton);
    panel.add(saveButton);

    frame.getContentPane().add(areaScrollPane, BorderLayout.NORTH);
    frame.getContentPane().add(panel, BorderLayout.SOUTH);

    //Finish setting up the frame, and show it.
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          // react to close event on window
        }
      });
    frame.pack();
    frame.setVisible(true);
  }

} // end of class WindowOutput

/*
 * Local Variables:
 * Mode: Java
 * fill-column: 75
 * End: */
