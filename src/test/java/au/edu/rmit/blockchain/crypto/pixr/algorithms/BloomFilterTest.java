package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.CsvFileManager;
import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.common.utils.measurement.Dynamometer;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRBloomFilterResult;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class BloomFilterTest extends Environment {

    @Test
    void testLoad() throws IOException {
        var dataSets = getDataSets(DataSize.ONE_MILLION, 10);
        Set<String> unique = new HashSet<>();
        for (ImmutableList<String> i : dataSets) {
            System.out.println(i.size());
            unique.addAll(i);
        }
        System.out.println(unique.size());
    }


    @Test
    void runBloomFilterExperiment() throws IOException, NotMatchException {
        for (DataSize size : new DataSize[]{DataSize.ONE_HUNDRED_THOUSAND, DataSize.FIVE_HUNDRED_THOUSAND, DataSize.ONE_MILLION}) {
            System.out.println("::::DATA SET " + size + "::::");
            var dataSets = getDataSets(size, 10);
            for (int id = 0; id < dataSets.size(); id++) {
                TestResult result = new TestResult(size, id + 1);
                for (double i = 1; i >= 0.1; i = i / 10) {
                    simulate(result, dataSets.get(id), i / dataSets.get(id).size());
                }
                result.exportResult();
            }
        }
    }

    private void simulate(TestResult result, ImmutableList<String> txs, double FPR) {

        System.out.println("******************************************************************");
        // Server part
        PIXRBloomFilterResult message = server(txs, FPR);
        // Client part
        for (int idx = 0; idx < txs.size(); idx++) {
            String targetTx = txs.get(idx);
            System.out.println("::::Picking tx in transaction list to check::::");
            System.out.println(targetTx);
            client(message, targetTx, idx);
            result.addResult(message, targetTx);
            System.out.println("******************************************************************");
        }

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

    static class TestResult {
        private final CsvFileManager csvFileManager;

        public TestResult(DataSize dataSize, int id) {
            csvFileManager = new CsvFileManager(
                    Setting.RESULT_HOME + Setting.BLOOM_FILTER_PREFIX + "_" + dataSize + "_" + id + Setting.CSV_EXTENSION,
                    "transaction_size", "target_tx", "false_positive_rate", "encode_run_time", "decode_run_time", "filter_size", "found_positions", "is_correct");

        }

        private final List<String[]> results = new ArrayList<>();

        public void exportResult() throws IOException, NotMatchException {
            csvFileManager.write(results);
        }

        public void addResult(PIXRBloomFilterResult result, String targetTx) {
            results.add(new String[]{
                    String.valueOf(result.getTxSize()),
                    targetTx,
                    String.valueOf(result.getFpRate()),
                    String.valueOf(result.getEncodeTime()),
                    String.valueOf(result.getDecodeTime()),
                    String.valueOf(result.getFilterInBinString().length()),
                    String.valueOf(result.getFoundPositions()),
                    String.valueOf(result.isCorrect())
            });
        }
    }

    @Test
    void compareSizeOfHashMapAndList() throws IOException {
        var dataSets = getDataSets(DataSize.ONE_MILLION, 1);
        List<String> list = dataSets.get(0);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), i);
        }
    }
}
