package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;
import com.google.common.collect.ImmutableList;

import java.util.HashSet;
import java.util.List;

public class SmartScanStrategy implements PIXRDistinctVectorStrategy {
    @Override
    public PIXRDistinctVectorFinderResult run(List<String> txBinaryList, int hashLength, int step) throws NotMatchException {
        int length = (int) Math.ceil(Math.log(txBinaryList.size()) / Math.log(2));
        while (length <= hashLength) {
            int startPosition = 0;
            while (startPosition + length <= hashLength) {
                HashSet<String> checkSet = new HashSet<>();
                for (String code : txBinaryList) {
                    String subCode = code.substring(startPosition, startPosition + length);
                    if (!checkSet.add(subCode)) {
                        break;
                    }
                }
                if (checkSet.size() == txBinaryList.size()) {
                    return new PIXRDistinctVectorFinderResult(txBinaryList.size(), hashLength, length, startPosition, step, ImmutableList.copyOf(checkSet));
                }
                startPosition++;
            }
            length += step;
        }
        throw new NotMatchException("Cannot find set of distinct vectors that match to inputted condition");
    }
}
