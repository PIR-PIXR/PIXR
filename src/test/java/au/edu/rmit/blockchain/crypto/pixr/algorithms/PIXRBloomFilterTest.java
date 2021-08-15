package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.CsvFileManager;
import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.common.utils.measurement.Dynamometer;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRBloomFilterResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PIXRBloomFilterTest extends GeneralTest {

    @Test
    void runBloomFilterExperiment() throws IOException, NotMatchException {
        var testCases = new TestSetting[]{
                new TestSetting(FIVE_K, 0.01),
                new TestSetting(FIVE_K, 0.001),
                new TestSetting(FIVE_K, 0.0001),
                new TestSetting(FIVE_K, 0.00001),
                new TestSetting(FIVE_K, 0.000001),
                new TestSetting(FIVE_K, 0.0000001),
                new TestSetting(TEN_K, 0.01),
                new TestSetting(TEN_K, 0.001),
                new TestSetting(TEN_K, 0.0001),
                new TestSetting(TEN_K, 0.00001),
                new TestSetting(TEN_K, 0.000001),
                new TestSetting(TEN_K, 0.0000001),
                new TestSetting(HUNDRED_K, 0.01),
                new TestSetting(HUNDRED_K, 0.001),
                new TestSetting(HUNDRED_K, 0.0001),
                new TestSetting(HUNDRED_K, 0.00001),
                new TestSetting(HUNDRED_K, 0.000001),
                new TestSetting(HUNDRED_K, 0.0000001),
                new TestSetting(FIVE_HUNDRED_K, 0.01),
                new TestSetting(FIVE_HUNDRED_K, 0.001),
                new TestSetting(FIVE_HUNDRED_K, 0.0001),
                new TestSetting(FIVE_HUNDRED_K, 0.00001),
                new TestSetting(FIVE_HUNDRED_K, 0.000001),
                new TestSetting(FIVE_HUNDRED_K, 0.0000001),
                new TestSetting(ONE_M, 0.01),
                new TestSetting(ONE_M, 0.001),
                new TestSetting(ONE_M, 0.0001),
                new TestSetting(ONE_M, 0.00001),
                new TestSetting(ONE_M, 0.000001),
                new TestSetting(ONE_M, 0.0000001)
        };
        TestResult result = new TestResult();
        for (TestSetting test : testCases) {
            simulate(result, testSet.get(test.txSize), test.falsePositiveRate);
        }
        result.exportResult();
    }

    private void simulate(TestResult result, List<String> txs, double FPR) {
        System.out.println("******************************************************************");
        PIXRBloomFilterResult bestPerformMessage = null;
        for (int i = 0; i < MAX_TRY; i++) {
            // Server part
            PIXRBloomFilterResult message = server(txs, FPR);
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

    private PIXRBloomFilterResult server(List<String> txs, double FPR) {
        System.out.println("::::SERVER::::");
        System.out.println("\tNum of transaction:" + txs.size());
        System.out.println("\tFalse positive rate:" + FPR);
        System.out.println("\t>>> Start to build a bloom filter");
        var result = Dynamometer.measure(() -> {
            PIXRBloomFilter filter = new PIXRBloomFilter(txs.size(), FPR);
            for (int idx = 0; idx < txs.size(); idx++) {
                filter.put(Util.parseHex2Binary(txs.get(idx)), idx);
            }
            return filter.getResult();
        });
        PIXRBloomFilterResult message = result.getResult();
        message.setEncodeTime(result.getDuration());
        System.out.println("\t<<< FINISHED");
        System.out.println("\tFilter size:" + message.getFilterInBinString().length() + " bits");
        System.out.println("\tMessage size (txSize-length +  filter-length):" + message.measureResultSize() + " bits");
        return message;
    }

    private void client(PIXRBloomFilterResult message, String checkTxHash, long realIdx) {
        System.out.println("::::CLIENT::::");
        System.out.println("\tChecked transaction: txHash = " + checkTxHash);
        System.out.println("\t>>> Start to import a bloom filter from received message and check a membership of Tx");
        var result = Dynamometer.measure(() -> {
            PIXRBloomFilter clientFilter = PIXRBloomFilter.readFromBinary(message.getFilter());
            List<Long> indexes = new ArrayList<>();
            for (long idx = 0; idx < message.getTxSize(); idx++) {
                if (clientFilter.checkMemberShip(Util.parseHex2Binary(checkTxHash), idx)) {
                    indexes.add(idx);
                }
            }
            return indexes;
        });
        List<Long> potentialIndexes = result.getResult();
        message.setDecodeTime(result.getDuration());
        message.setFoundPositions(potentialIndexes.size());
        System.out.println("\t<<< FINISHED");
        if (potentialIndexes.isEmpty()) {
            System.out.println("\tTx cannot be found");
        } else {
            System.out.println("\tTx was found in " + potentialIndexes.size() + " positions:" + potentialIndexes);
            System.out.println("\tReal index:" + realIdx);
            System.out.println("\tIs real index in the list:" + potentialIndexes.contains(realIdx));
            message.setCorrect(potentialIndexes.contains(realIdx));
        }
    }

    static class TestSetting {
        public int txSize;
        public double falsePositiveRate;

        public TestSetting(int txSize, double falsePositiveRate) {
            this.txSize = txSize;
            this.falsePositiveRate = falsePositiveRate;
        }
    }

    static class TestResult {
        private final CsvFileManager csvFileManager = new CsvFileManager(Setting.RESULT_HOME + Setting.BLOOM_FILTER,
                "transaction_size", "false_positive_rate", "encode_run_time", "decode_run_time", "filter_size", "found_positions", "is_correct");
        private final List<String[]> results = new ArrayList<>();

        public void exportResult() throws IOException, NotMatchException {
            csvFileManager.write(results);
        }

        public void addResult(PIXRBloomFilterResult result) {
            results.add(new String[]{
                    String.valueOf(result.getTxSize()),
                    String.valueOf(result.getFpRate()),
                    String.valueOf(result.getEncodeTime()),
                    String.valueOf(result.getDecodeTime()),
                    String.valueOf(result.getFilterInBinString().length()),
                    String.valueOf(result.getFoundPositions()),
                    String.valueOf(result.isCorrect())
            });
        }
    }
}