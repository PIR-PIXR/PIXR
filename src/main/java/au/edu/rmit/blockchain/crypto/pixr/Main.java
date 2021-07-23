package au.edu.rmit.blockchain.crypto.pixr;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.PIXRBloomFilter;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.PIXRDistinctVectorFinder;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.SmartScanStrategy;
import au.edu.rmit.blockchain.crypto.pixr.data.BlockDataAccess;
import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.dto.Transaction;
import au.edu.rmit.blockchain.crypto.pixr.utils.Setting;
import au.edu.rmit.blockchain.crypto.pixr.utils.Util;
import au.edu.rmit.blockchain.crypto.pixr.utils.measurement.Dynamometer;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        BlockDataAccess dataAccess = new BlockDataAccess();
        var blocks = dataAccess.load();
        if (blocks.isEmpty()) {
            dataAccess.downloadBlock();
            blocks = dataAccess.load();
        }
        for (Block b : blocks) {
            System.out.println("Block:" + b.getHash());
            System.out.println("Number of transaction:" + b.getTransactions().size());
            System.out.println("Distinct Vector Finder:");
            Dynamometer.measure(() -> {
                PIXRDistinctVectorFinder finder = new PIXRDistinctVectorFinder(new SmartScanStrategy(), Setting.HASH_CODE_LENGTH);
                List<Transaction> txs = b.getTransactions();
                for (Transaction tx : txs) {
                    finder.put(Util.parseHex2Binary(tx.getHash()));
                }
                var result = finder.find();
                System.out.println("\tVector length:" + result.getLength());
                System.out.println("\tStart position:" + result.getPosition());
                System.out.println("\tSize:" + result.measureResultSize() + " bits");
                return true;
            });

            System.out.println("Bloom Filter:");
            Dynamometer.measure(() -> {
                List<Transaction> txs = b.getTransactions();
                PIXRBloomFilter bloomFilter = new PIXRBloomFilter(txs.size(), 0.01);
                for (int i = 0; i < txs.size(); i++) {
                    bloomFilter.put(txs.get(i), i);
                }
                var result = bloomFilter.getResult();
                System.out.println("\tFalse positive rate:" + result.getFpRate());
                System.out.println("\tSize:" + result.measureResultSize() + " bits");
                return true;
            });
            System.out.println("-----------------------------------------------");
        }
    }
}