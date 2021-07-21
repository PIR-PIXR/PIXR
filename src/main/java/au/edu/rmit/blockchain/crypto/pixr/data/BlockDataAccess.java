package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.pixr.api.BlockQuery;
import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.gson.GsonBuilder;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockDataAccess extends AbstractDataAccess<Block> {
    private final String startBlock = "00000000000000000002a23d6df20eecec15b21d32c75833cce28f113de888b7";
    private String fileName = "block.json";
    private final GsonBuilder parser = new GsonBuilder();

    @Override
    String getDataFromApi() throws APIException, IOException, InterruptedException {
        BlockQuery query = new BlockQuery(startBlock);
        return query.nextBlockWithCondition().toJson();
    }

    @Override
    Block parseData(String s) {
        return parser.create().fromJson(s, Block.class);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String FILENAME) {
        this.fileName = FILENAME;
    }
}
