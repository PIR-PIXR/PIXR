package au.edu.rmit.blockchain.crypto.pixr.algorithms;

import au.edu.rmit.blockchain.crypto.common.data.PIXRDataAccess;
import com.google.common.collect.ImmutableList;

import java.io.IOException;

public class Environment {
    private final PIXRDataAccess dataAccess = new PIXRDataAccess();

    public ImmutableList<ImmutableList<String>> getDataSets(DataSize size, int number) throws IOException {
        return dataAccess.loadBySet(size.getValue(), number);
    }
}
