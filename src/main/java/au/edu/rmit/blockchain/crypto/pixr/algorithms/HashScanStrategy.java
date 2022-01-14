package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.results.PIXRDistinctVectorFinderResult;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class HashScanStrategy implements PIXRDistinctVectorStrategy {
    int initStep = 0;

    public HashScanStrategy() {

    }

    public HashScanStrategy(int initStep) {
        this.initStep = initStep;
    }

    @Override
    public PIXRDistinctVectorFinderResult run(List<String> txBinaryList, int hashLength) throws NotMatchException {
        int length = (int) Util.log2(txBinaryList.size()) + initStep;

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
