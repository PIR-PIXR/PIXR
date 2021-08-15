package au.edu.rmit.blockchain.crypto.common.utils.measurement;

public class Dynamometer {

    public static <T> DynamometerResult<T> measure(MeterWrapper<T> wrapper) {
        long start = System.currentTimeMillis();
        T result = null;
        try {
            result = wrapper.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("\tRun time: " + duration + " milliseconds");
        return new DynamometerResult<>(result, duration);
    }

}
