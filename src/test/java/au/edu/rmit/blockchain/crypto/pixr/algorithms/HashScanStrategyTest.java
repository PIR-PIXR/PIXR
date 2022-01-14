package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.CsvFileManager;
import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.common.utils.measurement.Dynamometer;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HashScanStrategyTest extends GeneralTest {
    @Test
    void hashScanTest() throws IOException, NotMatchException {
        var testCases = new TestSetting[]{
                new TestSetting(FIVE_K, 1),
                new TestSetting(FIVE_K, 2),
                new TestSetting(FIVE_K, 3),
                new TestSetting(FIVE_K, 5),
                new TestSetting(FIVE_K, 8),
                new TestSetting(FIVE_K, 10),
                new TestSetting(FIVE_K, 13),
                new TestSetting(TEN_K, 1),
                new TestSetting(TEN_K, 2),
                new TestSetting(TEN_K, 3),
                new TestSetting(TEN_K, 5),
                new TestSetting(TEN_K, 8),
                new TestSetting(TEN_K, 10),
                new TestSetting(TEN_K, 13),
                new TestSetting(HUNDRED_K, 1),
                new TestSetting(HUNDRED_K, 2),
                new TestSetting(HUNDRED_K, 3),
                new TestSetting(HUNDRED_K, 5),
                new TestSetting(HUNDRED_K, 8),
                new TestSetting(HUNDRED_K, 10),
                new TestSetting(HUNDRED_K, 13),
                new TestSetting(FIVE_HUNDRED_K, 1),
                new TestSetting(FIVE_HUNDRED_K, 2),
                new TestSetting(FIVE_HUNDRED_K, 3),
                new TestSetting(FIVE_HUNDRED_K, 5),
                new TestSetting(FIVE_HUNDRED_K, 8),
                new TestSetting(FIVE_HUNDRED_K, 10),
                new TestSetting(FIVE_HUNDRED_K, 13),
                new TestSetting(ONE_M, 1),
                new TestSetting(ONE_M, 2),
                new TestSetting(ONE_M, 3),
                new TestSetting(ONE_M, 5),
                new TestSetting(ONE_M, 8),
                new TestSetting(ONE_M, 10),
                new TestSetting(ONE_M, 13)
        };

        TestResult result = new TestResult();
        for (TestSetting test : testCases) {
            simulate(result, testSet.get(test.txSize), test.initialStep);
        }
        result.exportResult();
    }

    private void simulate(TestResult result, List<String> txs, int initialStep) {
        System.out.println("******************************************************************");
        PIXRDistinctVectorFinderResult bestPerformMessage = null;
        for (int i = 0; i < MAX_TRY; i++) {
            // Server part
            PIXRDistinctVectorFinderResult message = server(txs, initialStep);
            // Client part
            int txIdx = new Random().nextInt(txs.size());
            String vTx = txs.get(txIdx);
            System.out.println("::::Picking random tx in transaction list to check::::");
            client(message, vTx, txIdx);
            System.out.println("******************************************************************");
            if (null == bestPerformMessage || message.getEncodeTime() < bestPerformMessage.getEncodeTime()) {
                bestPerformMessage = message;
            }
        }
        result.addResult(bestPerformMessage);
    }

    private PIXRDistinctVectorFinderResult server(List<String> txs, int initialStep) {
        System.out.println("::::SERVER::::");
        System.out.println("\tNumber of transaction:" + txs.size());
        System.out.println("\tInitial step:" + initialStep);
        System.out.println("\t>>> Start to build distinct vectors");
        var result = Dynamometer.measure(() -> {
            PIXRDistinctVectorFinder finder = new PIXRDistinctVectorFinder(new HashScanStrategy(initialStep), Setting.HASH_CODE_LENGTH);
            for (String tx : txs) {
                finder.put(Util.parseHex2Binary(tx));
            }
            return finder.find();
        });
        var message = result.getResult();
        message.setEncodeTime(result.getDuration());
        message.setInitialStep(initialStep);
        System.out.println("\t<<< FINISHED");
        System.out.println("\tVector length:" + message.getVectors().get(0).length());
        System.out.println("\tStart position:" + message.getPositions().get(0));
        System.out.println("\tMessage size (start-position-length + list-vector-length):" + message.measureResultSize() + " bits");
        return message;
    }

    private void client(PIXRDistinctVectorFinderResult message, String checkTxHash, long realIdx) {
        System.out.println("::::CLIENT::::");
        System.out.println("\tChecked transaction: txHash = " + checkTxHash);
        System.out.println("\t>>> Start to extract a vector from tx and check its index in the list");
        var result = Dynamometer.measure(() -> {
            int startPosition = message.getPositions().get(0);
            int length = message.getVectors().get(0).length();
            String vector = Util.parseHex2Binary(checkTxHash).substring(startPosition, startPosition + length);
            return message.getVectors().indexOf(vector);
        });
        System.out.println("\t<<< FINISHED");
        int idx = result.getResult();
        message.setDecodeTime(result.getDuration());
        System.out.println("\tReal Idx: " + realIdx);
        if (idx == realIdx) {
            System.out.println("\tTx was found correctly in position: " + idx);
        } else {
            System.out.println("\tTx idx was not correct with value: " + idx);
        }
    }

    static class TestSetting {
        public int txSize;
        public int initialStep;

        public TestSetting(int txSize, int initialStep) {
            this.txSize = txSize;
            this.initialStep = initialStep;
        }
    }

    static class TestResult {
        private final CsvFileManager csvFileManager = new CsvFileManager(Setting.RESULT_HOME + Setting.DISTINCT_VECTOR_HASH,
                "transaction_size", "initial_length", "constant_c", "encode_run_time", "decode_run_time", "vector_length", "start_position", "message_size");
        private final List<String[]> results = new ArrayList<>();

        public void exportResult() throws IOException, NotMatchException {
            csvFileManager.write(results);
        }

        public void addResult(PIXRDistinctVectorFinderResult result) {
            results.add(new String[]{
                    String.valueOf(result.getTxSize()),
                    String.valueOf(Util.log2(result.getTxSize())),
                    String.valueOf(result.getInitialStep()),
                    String.valueOf(result.getEncodeTime()),
                    String.valueOf(result.getDecodeTime()),
                    String.valueOf(result.getVectors().get(0).length()),
                    String.valueOf(result.getPositions().get(0)),
                    String.valueOf(result.measureResultSize())
            });
        }
    }
}
