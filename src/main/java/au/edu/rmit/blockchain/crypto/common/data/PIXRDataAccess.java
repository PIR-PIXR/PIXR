package au.edu.rmit.blockchain.crypto.common.data;

import au.edu.rmit.blockchain.crypto.common.api.BlockQuery;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.NotImpletementException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static au.edu.rmit.blockchain.crypto.common.utils.Setting.PXIR_FILE;
import static au.edu.rmit.blockchain.crypto.common.utils.Setting.START_BLOCK;

public class PIXRDataAccess extends AbstractDataAccess<String> {
    private static String startBlock = START_BLOCK;

    public PIXRDataAccess() {
    }

    @Override
    String getFileName() {
        return PXIR_FILE;
    }

    @Override
    String getDataFromApi() throws NotImpletementException {
        throw new NotImpletementException("Not implemented!");
    }

    @Override
    List<String> getProcessesData() throws APIException, IOException {
        BlockQuery query = new BlockQuery(startBlock);
        Block b = query.nextBlockWithCondition();
        startBlock = b.getNextBlockHashes().get(0);
        return b.getTransactions().stream().map(Transaction::getHash).collect(Collectors.toList());
    }

    @Override
    String parseData(String json) {
        return json;
    }

    @Override
    boolean isMultipleWrite() {
        return true;
    }
}
