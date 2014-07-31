package org.edorasframework.core.util;

public class ClassDescriberTest {
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface AMethodInherited {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface AMethod {
    }

    private static class SuperBean {
        @Version
        private int x;

        @Column
        private int i;

        @Column
        public String getS() {
            return null;
        }

        @AMethodInherited
        public void inherit0() {}

        @AMethodInherited
        public void inherit1() {}

        @AMethod
        public void inherit2() {}
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Constr {
    }
}
