package au.edu.rmit.blockchain.crypto.common.data;

import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import au.edu.rmit.blockchain.crypto.pixr.algorithms.NotImpletementException;
import com.google.common.collect.ImmutableList;

import java.io.IOException;

public interface DataAccess<T> {
    /**
     * Download blockchain data and save it into file
     */
    void download() throws IOException, APIException, InterruptedException, NotImpletementException;

    /**
     * Load blockchain data from source file
     *
     * @return List of Object
     */
    ImmutableList<T> load() throws IOException;

    /**
     * Load blockchain data from source file
     *
     * @return List of Object
     */
    ImmutableList<ImmutableList<String>>loadBySet(int size, int number) throws IOException;
}
