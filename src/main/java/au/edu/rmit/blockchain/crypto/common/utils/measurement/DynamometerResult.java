package au.edu.rmit.blockchain.crypto.common.utils.measurement;

public class DynamometerResult<T> {
    private T result;
    private long duration;

    public DynamometerResult(T result, long duration) {
        this.result = result;
        this.duration = duration;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
