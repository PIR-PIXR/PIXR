package au.edu.rmit.blockchain.crypto.pixr.utils.measurement;

public interface MeterWrapper<T> {
    T execute() throws Exception;
}
