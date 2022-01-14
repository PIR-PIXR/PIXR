package au.edu.rmit.blockchain.crypto.common.data;

import au.edu.rmit.blockchain.crypto.common.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.NotImpletementException;
import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import static au.edu.rmit.blockchain.crypto.common.utils.Setting.*;

public class TransactionDataAccess extends AbstractDataAccess<Transaction> {
    private final GsonBuilder parser = new GsonBuilder();
    private final BlockChainApi api = new BlockChainApi();
    private final ImmutableList<String> txList;
    private String currentHashCode = "";

    public TransactionDataAccess(List<String> txList) {
        this.txList = ImmutableList.copyOf(txList);
    }

    public void downloadTransaction() throws APIException, IOException, NotImpletementException {
        for (String hash : txList) {
            currentHashCode = hash;
            download();
        }
    }

    @Override
    String getFileName() {
        return TRANSACTION_FILE;
    }

    @Override
    String getDataFromApi() throws APIException, IOException {
        return api.getTransaction(currentHashCode).toJson();
    }

    @Override
    Transaction parseData(String json) {
        return parser.create().fromJson(json, Transaction.class);
    }

    @Override
    boolean isMultipleWrite() {
        return false;
    }


    @Override
    List<String> getProcessesData() throws NotImpletementException {
        throw new NotImpletementException("Not implemented!");
    }

}
