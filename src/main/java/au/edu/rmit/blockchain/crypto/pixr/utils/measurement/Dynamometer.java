package au.edu.rmit.blockchain.crypto.pixr.utils.measurement;

public class Dynamometer {

    public static <T> void measure(MeterWrapper<T> wrapper) {
        long start = System.currentTimeMillis();
        try {
            wrapper.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("\tRun time: " + duration + " milliseconds");
    }

}
