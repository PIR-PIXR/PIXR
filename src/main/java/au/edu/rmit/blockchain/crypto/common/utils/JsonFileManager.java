package au.edu.rmit.blockchain.crypto.common.utils;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileManager {
    private final String path;

    public JsonFileManager(String path) {
        this.path = path;
    }

    /**
     * Write data to file (append mode)
     *
     * @param data json data
     */
    public void write(String data) throws IOException {
        FileWriter writer = new FileWriter(path, true);
        writer.write(data + System.lineSeparator());
        writer.flush();
        writer.close();
    }

    /**
     * Read data from source file
     *
     * @return List of json
     */
    public ImmutableList<String> read() throws IOException {
        String line;
        List<String> items = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        while ((line = reader.readLine()) != null) {
            items.add(line);
        }
        return ImmutableList.copyOf(items);
    }
}
