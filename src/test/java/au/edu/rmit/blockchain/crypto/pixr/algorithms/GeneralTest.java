package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.data.BlockDataAccess;
import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralTest {
    public static final Integer FIVE_K = 5_000;
    public static final Integer TEN_K = 10_000;
    public static final Integer HUNDRED_K = 100_000;
    public static final Integer FIVE_HUNDRED_K = 500_000;
    public static final Integer ONE_M = 1_000_000;
    public static final int MAX_TRY = 3;
    public static Map<Integer, List<String>> testSet = new HashMap<>();

    @BeforeAll
    static void init() throws IOException {
        System.gc();
        BlockDataAccess dataAccess = new BlockDataAccess();
        List<String> txs = dataAccess.loadTxCodeOnly();
        if (txs.size() >= FIVE_K)
            testSet.put(FIVE_K, new ArrayList<>(txs.subList(0, FIVE_K )));
        if (txs.size() >= TEN_K)
            testSet.put(TEN_K, new ArrayList<>(txs.subList(0, TEN_K )));
        if (txs.size() >= HUNDRED_K)
            testSet.put(HUNDRED_K, new ArrayList<>(txs.subList(0, HUNDRED_K)));
        if (txs.size() >= FIVE_HUNDRED_K)
            testSet.put(FIVE_HUNDRED_K, new ArrayList<>(txs.subList(0, FIVE_HUNDRED_K)));
        if (txs.size() >= ONE_M)
            testSet.put(ONE_M, new ArrayList<>(txs.subList(0, ONE_M )));
    }

    @AfterAll
    static void clean(){
        testSet = null;
        System.gc();
    }
}


