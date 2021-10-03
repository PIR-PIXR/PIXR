package au.edu.rmit.blockchain.crypto.pir;

import au.edu.rmit.blockchain.crypto.common.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.LatestBlock;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
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
        System.out.println("Block hash: " + latestBlock.getHash());

        //Single Block
        Block sblock = api.getBlock(latestBlock.getHash());
        //Block sblock = api.getBlock("0000000000000000e067a478024addfecdc93628978aa52d91fabd4292982a50");

        System.out.println("Merkle root: " + sblock.getMerkleRoot());
        System.out.println("Number of transactions: " + sblock.getTransactions().size());

        //Ordering txs in the Merkle tree
        List<String> txHashList = toList(sblock.getTransactions());

        MerkleTrees MT = new MerkleTrees(txHashList);
        MT.merkle_tree();
        System.out.println("Root result : " + MT.getRoot());
    }

    private static TreeMap<Long, String> toTreeMap(List<Transaction> txs) {
        TreeMap<Long, String> txH = new TreeMap<>();
        for (Transaction tx : txs) {
            txH.put(tx.getIndex(), tx.getHash());
        }
        return txH;
    }

    private static List<String> toList(List<Transaction> txs) {
        List<String> txH = new ArrayList<>();
        for (Transaction tx : txs) {
            txH.add(tx.getHash());
        }
        return txH;
    }
}
