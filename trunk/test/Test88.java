package boca.corba;

import org.omg.CORBA.ORB;

import java.io.PrintStream;

/**
 * @version     1.14, 05/11/10
 * @since       JDK1.2
 */

public interface Test88
{
    String getCommandName();

    public final static boolean shortHelp = true;
    public final static boolean longHelp  = false;

    void printCommandHelp(PrintStream out, boolean helpType);

    public final static boolean parseError = true;
    public final static boolean commandDone = false;

    boolean processCommand(String[] cmd, ORB orb, PrintStream out);
}
