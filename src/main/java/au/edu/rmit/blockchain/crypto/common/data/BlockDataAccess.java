package au.edu.rmit.blockchain.crypto.common.data;

import au.edu.rmit.blockchain.crypto.common.api.BlockQuery;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.JsonFileManager;
import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.NotImpletementException;
import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static au.edu.rmit.blockchain.crypto.common.utils.Setting.*;

public class BlockDataAccess extends AbstractDataAccess<Block> {
    private final GsonBuilder parser = new GsonBuilder();
    private final BlockQuery query;

    public BlockDataAccess() {
        query = new BlockQuery(START_BLOCK);
    }

    public BlockDataAccess(String startBlock) {
        query = new BlockQuery(startBlock);
    }

    public ImmutableList<String> loadTxCodeOnly() throws IOException {
        String fileName = getFileName();
        String filePath = Setting.DATA_HOME + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return ImmutableList.of();
        }
        JsonFileManager manager = new JsonFileManager(filePath);
        ImmutableList<String> jsonData = manager.read();
        List<String> result = new ArrayList<>();
        for (String s : jsonData) {
            var b = parseData(s);
            result.addAll(b.getTransactions().stream().map(Transaction::getHash).collect(Collectors.toList()));
        }
        return ImmutableList.copyOf(result);
    }

    public void downloadBlock() throws APIException, IOException, NotImpletementException {
        for (int i = 1; i <= NUM_BLOCK_TO_DOWNLOAD; i++) {
            download();
        }
    }

    @Override
    String getDataFromApi() throws APIException, IOException {
        return query.nextBlockWithCondition().toJson();
    }

    @Override
    List<String> getProcessesData() throws NotImpletementException {
        throw new NotImpletementException("Not implemented!");
    }

    @Override
    Block parseData(String json) {
        return parser.create().fromJson(json, Block.class);
    }

    @Override
    boolean isMultipleWrite() {
        return false;
    }

    @Override
    public String getFileName() {
        return BLOCK_FILE;
    }

}
