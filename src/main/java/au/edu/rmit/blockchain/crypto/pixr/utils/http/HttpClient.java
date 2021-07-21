package au.edu.rmit.blockchain.crypto.pixr.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface HttpClient {
    String get(String resource, Map<String, String> params) throws APIException, IOException;

    String get(String url, String resource, Map<String, String> params) throws APIException, IOException;

}
