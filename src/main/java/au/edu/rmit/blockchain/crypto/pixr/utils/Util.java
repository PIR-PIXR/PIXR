package au.edu.rmit.blockchain.crypto.pixr.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Util {

    public static String parseHex2Binary(String hex) {
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        StringBuilder binaryString = new StringBuilder();

        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            if (d == 0) binaryString.append("0000");
            else binaryString.append(Integer.toBinaryString(d));
        }
        return binaryString.toString();
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder responseStringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            responseStringBuilder.append(line);
        }

        reader.close();

        return responseStringBuilder.toString();
    }


    public static String urlEncodeParams(Map<String, String> params) {
        String result = "";

        if (params != null && params.size() > 0) {
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> kvp : params.entrySet()) {
                if (data.length() > 0) {
                    data.append('&');
                }

                data.append(URLEncoder.encode(kvp.getKey(), StandardCharsets.UTF_8));
                data.append('=');
                data.append(URLEncoder.encode(kvp.getValue(), StandardCharsets.UTF_8));
            }
            result = data.toString();
        }

        return result;
    }


}
