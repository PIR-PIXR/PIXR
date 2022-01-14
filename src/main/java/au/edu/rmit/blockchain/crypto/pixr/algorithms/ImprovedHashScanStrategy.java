package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;
import com.google.common.collect.ImmutableList;

import java.util.LinkedHashSet;
import java.util.List;

public class ImprovedHashScanStrategy implements PIXRDistinctVectorStrategy {
    public ImprovedHashScanStrategy() {

    }

    @Override
    public PIXRDistinctVectorFinderResult run(List<String> txBinaryList, int hashLength) throws NotMatchException {
        int length = 2 * (int) Util.log2(txBinaryList.size());
        int startPosition = 0;
        while (startPosition + length <= hashLength) {
            LinkedHashSet<String> checkSet = new LinkedHashSet<>();
            for (String code : txBinaryList) {
                String subCode = code.substring(0, length + length);
                if (!checkSet.add(subCode)) {
                    break;
                }
            }
            if (checkSet.size() == txBinaryList.size()) {
                return runDecrease(length - 1, hashLength, txBinaryList, checkSet);
            }
            startPosition++;
        }
        return runIncrease(length + 1, hashLength, txBinaryList);
    }

    private PIXRDistinctVectorFinderResult runDecrease(int length, int hashLength, List<String> txBinaryList, LinkedHashSet<String> lastCheckSet) throws NotMatchException {
        int lastStartPosition = 0;
        while (length > 0) {
            int startPosition = 0;
            boolean isAllDuplicated = true;
            while (startPosition + length <= hashLength) {
                LinkedHashSet<String> checkSet = new LinkedHashSet<>();
                for (String code : txBinaryList) {
                    String subCode = code.substring(startPosition, startPosition + length);
                    if (!checkSet.add(subCode)) {
                        break;
                    }
                }
                if (checkSet.size() == txBinaryList.size()) {
                    lastCheckSet = checkSet;
                    lastStartPosition = startPosition;
                    isAllDuplicated = false;
                    break;
                }
                startPosition++;
            }
            if (isAllDuplicated) {
                return new PIXRDistinctVectorFinderResult(txBinaryList.size(), hashLength, List.of(lastStartPosition), ImmutableList.copyOf(lastCheckSet));
            }
            length--;
        }
        throw new NotMatchException("Cannot find set of distinct vectors that match to inputted condition");
    }


    private PIXRDistinctVectorFinderResult runIncrease(int length, int hashLength, List<String> txBinaryList) throws NotMatchException {
        while (length <= hashLength) {
            int startPosition = 0;
            while (startPosition + length <= hashLength) {
                LinkedHashSet<String> checkSet = new LinkedHashSet<>();
                for (String code : txBinaryList) {
                    String subCode = code.substring(startPosition, startPosition + length);
                    if (!checkSet.add(subCode)) {
                        break;
                    }
                }
                if (checkSet.size() == txBinaryList.size()) {
                    return new PIXRDistinctVectorFinderResult(txBinaryList.size(), hashLength, List.of(startPosition), ImmutableList.copyOf(checkSet));
                }
                startPosition++;
            }
            length++;
        }
        throw new NotMatchException("Cannot find set of distinct vectors that match to inputted condition");
    }
}
