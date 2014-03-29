public
 class PerfTest {
    public void startTests() {

        final VO1 vo = new VO1(voSize);

        final class CallsTimerTask extends TimerTask {
            

            public void run() {

                long result = client.timeCallPassVO(vo, callCount);

            }
     

        }

    }
 
}
