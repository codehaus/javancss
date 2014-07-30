package ho.ha.hi;

import java.io.IOException;
import java.util.Set;
import java.util.WeakHashMap;
import javax.imageio.stream.ImageInputStream;

public class Test78 {

    private static WeakHashMap<ImageInputStream, Object> toCloseQueue = null;
    private static Thread streamCloser = null;

    public static void addToQueue(ImageInputStream iis) {
        synchronized (StreamCloser.class) {
            if (toCloseQueue == null) {
                toCloseQueue = new WeakHashMap<ImageInputStream, Object>();
            }
            
            toCloseQueue.put(iis, null);

            if (streamCloser == null) {
                final Runnable streamCloserRunnable = new Runnable() {
                    public void run() {
                        if (toCloseQueue != null) {
                            synchronized (StreamCloser.class) {
                                Set<ImageInputStream> set = toCloseQueue.keySet();
                                ImageInputStream[] streams = 
                                    new ImageInputStream[set.size()];
                                streams = set.toArray(streams);
                                for (ImageInputStream is : streams) {
                                    if (is != null) {
                                        try {
                                            is.close();
                                        } catch (IOException e) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                };
                
                java.security.AccessController.doPrivileged(
                    new java.security.PrivilegedAction() {
                        public Object run() {
                            /* The thread must be a member of a thread group
                             * which will not get GCed before VM exit.
                             * Make its parent the top-level thread group.
                             */
                            ThreadGroup tg =
                                Thread.currentThread().getThreadGroup();
                            for (ThreadGroup tgn = tg;
                                 tgn != null;
                                 tg = tgn, tgn = tg.getParent());
                            streamCloser = new Thread(tg, streamCloserRunnable);
                            Runtime.getRuntime().addShutdownHook(streamCloser);
                            return null;
                        }
                    });
            }
        }
    }

    public static void removeFromQueue(ImageInputStream iis) {
        if (toCloseQueue != null) {
            toCloseQueue.remove(iis);
        }
    }
}
