package au.edu.rmit.blockchain.crypto.common.utils;

import au.edu.rmit.blockchain.crypto.pixr.algorithms.NotMatchException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileManager {
    private final String[] headers;
    private final String path;

    public CsvFileManager(String path, String... headers) {
        this.headers = headers;
        this.path = path;
    }

    public void write(List<String[]> data) throws IOException, NotMatchException {
        if (data == null || data.isEmpty() || data.get(0).length != headers.length) {
            throw new NotMatchException("Data size not match to headers");
        }
        FileWriter writer = new FileWriter(path, false);
        writer.write(String.join(",", headers) + System.lineSeparator());
        for (String[] d : data) {
            writer.write(String.join(",", d) + System.lineSeparator());
        }
        writer.flush();
        writer.close();
    }


}
