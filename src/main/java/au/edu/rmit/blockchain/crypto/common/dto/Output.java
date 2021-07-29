package au.edu.rmit.blockchain.crypto.common.dto;

import com.google.gson.annotations.SerializedName;

public class Output {
    private final int n;
    private final long value;
    @SerializedName("addr")
    private final String address;
    @SerializedName("tx_index")
    private final long txIndex;
    private final String script;
    private final boolean spent;
    private final boolean spentToAddress;

    public Output(int n, long value, String address, long txIndex, String script, boolean spent) {
        this.n = n;
        this.value = value;
        this.address = address;
        this.txIndex = txIndex;
        this.script = script;
        this.spent = spent;
        this.spentToAddress = !address.equals("");

    }

    public int getN() {
        return n;
    }

    public long getValue() {
        return value;
    }

    public String getAddress() {
        return address;
    }

    public long getTxIndex() {
        return txIndex;
    }

    public String getScript() {
        return script;
    }

    public boolean isSpent() {
        return spent;
    }

    public boolean isSpentToAddress() {
        return spentToAddress;
    }
}
