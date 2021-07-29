package au.edu.rmit.blockchain.crypto.common.utils.measurement;

public interface MeterWrapper<T> {
    T execute() throws Exception;
}
