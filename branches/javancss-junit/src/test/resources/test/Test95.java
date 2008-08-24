package steve.jobs;

import java.awt.Event;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test95 extends InputEvent {
    /**
     * @deprecated as of JDK1.1 
     */
    @Deprecated
    public KeyEvent(Component source, int id, long when, int modifiers,
                    int keyCode) {
        this(source, id, when, modifiers, keyCode, (char)keyCode);
    }
}

