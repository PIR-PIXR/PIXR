package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.dto.Transaction;
import com.google.common.collect.ImmutableList;

public class TransactionDataAccess implements DataAccess<Transaction> {


    @Override
    public void download() {

    }

    @Override
    public ImmutableList<Transaction> load() {
        return null;
    }
}
