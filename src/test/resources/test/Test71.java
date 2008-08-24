public class Test71
{
    public static void main(String[] args)
    {
        Runnable run1 = new Runnable() {
                public void run() {
                    System.out.println( "Hi" );
                }
            };
        Runnable run2 = new Runnable() {
                public void run() {
                    System.out.println( "Ho" );
                }
            };

        class TestRun implements Runnable {
            public void run() {
                System.out.println( "Hello" );
            }
        }
        Runnable run3 = new TestRun();

        run1.run();
        run2.run();
        run3.run();
    }

    public static class TestRun implements Runnable {
        public void run() {
            System.out.println( "Hello World" );
        }
    }
}
