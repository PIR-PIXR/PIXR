package au.edu.rmit.blockchain.crypto.pixr;

import au.edu.rmit.blockchain.crypto.common.data.BlockDataAccess;

public class PIXR {
    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                BlockDataAccess dataAccess = new BlockDataAccess();
                var blocks = dataAccess.load();
                if (blocks.isEmpty()) {
                    dataAccess.downloadBlock();
                } else {
                    dataAccess = new BlockDataAccess(blocks.get(blocks.size() - 1).getNextBlockHashes().get(0));
                    dataAccess.downloadBlock();
                }
            } catch (Exception ex) {
                System.out.println("Error -> Sleep");
                Thread.sleep(5 * 60 * 1000);
            }
        }
    }
}