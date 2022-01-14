package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.dto.Block;
import au.edu.rmit.blockchain.crypto.common.dto.Transaction;
import au.edu.rmit.blockchain.crypto.common.utils.Util;
import au.edu.rmit.blockchain.crypto.common.utils.measurement.Dynamometer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AStarScanStrategyTest extends GeneralTest {

    @Test
    void AStart() {
        List<String> txs = testSet.get(FIVE_K);
        System.out.println("Number of transaction:" + txs.size());
        System.out.println("Distinct Vector Finder:");
        Dynamometer.measure(() -> {
            PIXRDistinctVectorFinder finder = new PIXRDistinctVectorFinder(new AStarScanStrategy());
            for (String tx : txs) {
                finder.put(Util.parseHex2Binary(tx));
            }
            var result = finder.find();
            System.out.println("\tVector length:" + result.getVectors().get(0).length());
            System.out.println("\tPositions:" + result.getPositions());
            System.out.println("\tSize:" + result.measureResultSize() + " bits");
            return true;
        });
    }

    @Test
    void JerryExample() throws NotMatchException {
        PIXRDistinctVectorFinder finder = new PIXRDistinctVectorFinder(new AStarScanStrategy(), 4);
        finder.put("0000");
        finder.put("0001");
        finder.put("1010");
        finder.put("1011");
        finder.put("1100");
        finder.put("1101");
        finder.put("1110");
        finder.put("1111");
        var result = finder.find();
        System.out.println("\tVector length:" + result.getVectors().get(0).length());
        System.out.println("\tPositions:" + result.getPositions());
        System.out.println("\tSize:" + result.measureResultSize() + " bits");
    }

}