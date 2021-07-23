package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.api.BlockQuery;
import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import static au.edu.rmit.blockchain.crypto.pixr.utils.Setting.*;

public class BlockDataAccess extends AbstractDataAccess<Block> {
    private final GsonBuilder parser = new GsonBuilder();
    private final BlockQuery query = new BlockQuery(START_BLOCK);

    public void downloadBlock() throws APIException, IOException {
        for (int i = 1; i <= NUM_BLOCK_TO_DOWNLOAD; i++) {
            download();
        }
    }

    @Override
    String getDataFromApi() throws APIException, IOException {
        return query.nextBlockWithCondition().toJson();
    }

    @Override
    Block parseData(String json) {
        return parser.create().fromJson(json, Block.class);
    }

    @Override
    public String getFileName() {
        return BLOCK_FILE;
    }

}
