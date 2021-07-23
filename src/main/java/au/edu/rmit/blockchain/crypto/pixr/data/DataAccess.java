package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.common.collect.ImmutableList;

import java.io.IOException;

public interface DataAccess<T> {
    /**
     * Download blockchain data and save it into file
     */
    void download() throws IOException, APIException, InterruptedException;

    /**
     * Load blockchain data from source file
     *
     * @return List of Object
     */
    ImmutableList<T> load() throws IOException;
}
