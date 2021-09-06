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

class ImprovedHashScanStrategyTest extends GeneralTest {
    @Test
    void hashScanTest() throws IOException, NotMatchException {
        var testCases = new TestSetting[]{
                new TestSetting(FIVE_K),
                new TestSetting(TEN_K),
                new TestSetting(HUNDRED_K),
                new TestSetting(FIVE_HUNDRED_K),
                new TestSetting(ONE_M),
        };

        TestResult result = new TestResult();
        for (TestSetting test : testCases) {
            simulate(result, testSet.get(test.txSize));
        }
        result.exportResult();
    }

    private void simulate(TestResult result, List<String> txs) {
        System.out.println("******************************************************************");
        PIXRDistinctVectorFinderResult bestPerformMessage = null;
        for (int i = 0; i < MAX_TRY; i++) {
            // Server part
            PIXRDistinctVectorFinderResult message = server(txs);
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

    private PIXRDistinctVectorFinderResult server(List<String> txs) {
        System.out.println("::::SERVER::::");
        System.out.println("\tNumber of transaction:" + txs.size());
        System.out.println("\t>>> Start to build distinct vectors");
        var result = Dynamometer.measure(() -> {
            PIXRDistinctVectorFinder finder = new PIXRDistinctVectorFinder(new ImprovedHashScanStrategy(), Setting.HASH_CODE_LENGTH);
            for (String tx : txs) {
                finder.put(Util.parseHex2Binary(tx));
            }
            return finder.find();
        });
        var message = result.getResult();
        message.setEncodeTime(result.getDuration());
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

        public TestSetting(int txSize) {
            this.txSize = txSize;
        }
    }

    static class TestResult {
        private final CsvFileManager csvFileManager = new CsvFileManager(Setting.RESULT_HOME + Setting.DISTINCT_VECTOR_HASH_2,
                "transaction_size", "initial_length", "encode_run_time", "decode_run_time", "vector_length", "start_position", "message_size");
        private final List<String[]> results = new ArrayList<>();

        public void exportResult() throws IOException, NotMatchException {
            csvFileManager.write(results);
        }

        public void addResult(PIXRDistinctVectorFinderResult result) {
            results.add(new String[]{
                    String.valueOf(result.getTxSize()),
                    String.valueOf(2 * (int) Util.log2(result.getTxSize())),
                    String.valueOf(result.getEncodeTime()),
                    String.valueOf(result.getDecodeTime()),
                    String.valueOf(result.getVectors().get(0).length()),
                    String.valueOf(result.getPositions().get(0)),
                    String.valueOf(result.measureResultSize())
            });
        }
    }

}