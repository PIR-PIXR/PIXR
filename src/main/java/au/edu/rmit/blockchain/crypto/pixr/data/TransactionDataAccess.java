package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.pixr.dto.Transaction;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import static au.edu.rmit.blockchain.crypto.pixr.utils.Setting.*;

public class TransactionDataAccess extends AbstractDataAccess<Transaction> {
    private final GsonBuilder parser = new GsonBuilder();
    private final BlockChainApi api = new BlockChainApi();
    private final ImmutableList<String> txList;
    private String currentHashCode = "";

    public TransactionDataAccess(List<String> txList) {
        this.txList = ImmutableList.copyOf(txList);
    }

    public void downloadTransaction() throws APIException, IOException {
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
}
