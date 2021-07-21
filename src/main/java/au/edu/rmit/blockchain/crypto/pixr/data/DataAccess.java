package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.common.collect.ImmutableList;

import java.io.IOException;

public interface DataAccess<T> {
    void download() throws IOException, APIException, InterruptedException;

    ImmutableList<T> load() throws IOException;
}
