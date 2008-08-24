/* -*- java  -*- */
import java.io.*;
import java.awt.Color;

import ccl.util.*;

public class Init extends Object {
    public static final int FRAME_WIDTH = 201;
    public static final int XSPACE = 0;
    public static final int YSPACE = 0;
    private static final String STR_INITFILE = "jamecs.ini";

    private static Color _clrBackground = Color.lightGray;
    private static Color _clrListBackground = Color.lightGray;
    private static Color _clrTextBackground = Color.white;

    private static String _sProjectFullName;
    private static String _sProjectPath;

    private static void _setProjectPath() {
        Util.panicIf(_sProjectFullName == null);

        int index = _sProjectFullName.lastIndexOf('\\');
        Util.debug(new Integer(index));
        _sProjectPath = _sProjectFullName.substring(0, index+1);
        Util.debug(_sProjectFullName);
    }

    public static void start() {
        _sProjectFullName = null;
        String sFile = null;

        try {
            sFile = Util.readFile(STR_INITFILE);
        } catch(Exception e) {
        }

        if (sFile == null) {
            return;
        }

        int indexEnd = sFile.indexOf('\n');
        if (indexEnd > 0) {
            _sProjectFullName = sFile.substring(0, indexEnd);
        }
        _setProjectPath();
        //Util.panicIf(_sDataPath == null); nicht schlimm
    }

    public static void setProjectFullName(String sName) {
        Util.panicIf(sName == null);

        _sProjectFullName = sName;
        _setProjectPath();

        // Abspeichern
        try {
            Util.writeFile(STR_INITFILE, _sProjectFullName + "\n");
        } catch (Exception e) {
            Util.println(e);
        }
    }

    public static String getProjectFullName() {
        return _sProjectFullName;
    }

    public static String getProjectPath() {
        return _sProjectPath;
    }

    public static Color getBackground() {
        return(_clrBackground);
    }

    public static Color getTextBackground() {
        return(_clrTextBackground);
    }

    public static Color getListBackground() {
        return(_clrTextBackground);
    }
}
