package au.edu.rmit.blockchain.crypto.pir;

import au.edu.rmit.blockchain.crypto.common.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.LatestBlock;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import au.edu.rmit.blockchain.crypto.pir.algorithms.MerkleTrees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PIR {
    public static void main(String[] args) throws APIException, IOException {
        BlockChainApi api = new BlockChainApi();

        //Latest Block
        LatestBlock latestBlock = api.getLatestBlock();
        System.out.println("Latest Block: " + latestBlock.getHash());

        //Single Block
        Block sblock = api.getBlock(latestBlock.getHash());

        //Hash Map: key = tx_index; value = tx hash
        TreeMap<Long, String> txHash = toTreeMap(sblock.getTransactions());

        System.out.println("merkle root: " + sblock.getMerkleRoot());
        System.out.println("number of transactions: " + sblock.getTransactions().size());

        //  # of leaves 2^x - full MerkleTree
        long size = (long) Math.pow(2, Util.log2(txHash.size()) + 1);
        System.out.println("test : " + size);

        List<String> tempTxList = new ArrayList<>(txHash.values());
        MerkleTrees merkleTrees = new MerkleTrees(tempTxList);
        merkleTrees.merkle_tree(size);
        System.out.println("root : " + merkleTrees.getRoot());

        //Test Merkle tree with 4 transaction hashes
        List<String> testTxList = new ArrayList<>();
        testTxList.add("b7dc35d70edd340636dee8995d542008a1f149015ec37804d48c9cf21f31d5c6");
        testTxList.add("685865651c0928c891a1ef42f8613b92e7741df2696debbf6aaf0732eabd06b6");
        testTxList.add("57e2ca5c42caf7f0d29c9e9105e918150ff74a34005919a27aa1f059b7ba15dc");
        testTxList.add("5836e7095761dcfa3fd998b900feff31e41f22d0f6f0a1e629cb9f7e29db7706");

        MerkleTrees MT = new MerkleTrees(testTxList);
        MT.merkle_tree();
        System.out.println("Root test : " + MT.getRoot());
    }

    private static TreeMap<Long, String> toTreeMap(List<Transaction> txs) {
        TreeMap<Long, String> txH = new TreeMap<>();
        for (Transaction tx : txs) {
            txH.put(tx.getIndex(), tx.getHash());
        }
        return txH;
    }
}
