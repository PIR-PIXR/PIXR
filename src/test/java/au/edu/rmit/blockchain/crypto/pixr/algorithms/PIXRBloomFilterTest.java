package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.data.BlockDataAccess;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.BloomFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class PIXRBloomFilterTest {
    private static ImmutableList<Block> blocks;

    @BeforeAll
    static void init() throws IOException {
        BlockDataAccess dataAccess = new BlockDataAccess();
        blocks = dataAccess.load();
    }

    @Test
    void falsePositiveTest() throws IOException {
        double FPR = 0.000001;
        // Server part
        List<Transaction> txs = blocks.get(1).getTransactions();
        System.out.println("Num of transaction:" + txs.size());
        PIXRBloomFilter filter = new PIXRBloomFilter(txs.size(), FPR);
        for (Transaction tx : txs) {
            filter.put(Util.parseHex2Binary(tx.getHash()), tx.getIndex());
        }
        var result = filter.getResult();
        System.out.println("False positive rate:" + result.getFpRate());
        System.out.println("Filter size:" + result.measureResultSize() + " bits");
        // Client part
        Transaction vTx = txs.get(new Random().nextInt(txs.size()));
        List<Long> indexes = txs.stream().map(Transaction::getIndex).collect(Collectors.toList());
        System.out.println("Pick random tx in transaction list:" + vTx.toJson());
        PIXRBloomFilter clientFilter = new PIXRBloomFilter(txs.size(), FPR);
        clientFilter.readFromBinary(result.getFilter());
        long idx = -1;
        for (Long i : indexes) {
            if (clientFilter.checkMemberShip(Util.parseHex2Binary(vTx.getHash()), i)) {
                idx = i;
                break;
            }
        }
        if (idx != -1) {
            System.out.println("Check result is " + (vTx.getIndex() == idx));
            System.out.println("Real result is " + clientFilter.checkMemberShip(Util.parseHex2Binary(vTx.getHash()),vTx.getIndex()));
        } else {
            System.out.println("Not Found");
        }
    }

    @Test
    void tempTest() throws IOException {
        PIXRBloomFilter filter = new PIXRBloomFilter(3);
        filter.put("a", 1);
        filter.put("b", 2);
        filter.put("c", 3);

        System.out.println(filter.checkMemberShip("a", 1));
    }

}