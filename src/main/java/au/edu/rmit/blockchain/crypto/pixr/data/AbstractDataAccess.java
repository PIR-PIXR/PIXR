package au.edu.rmit.blockchain.crypto.pixr.data;

import au.edu.rmit.blockchain.crypto.pixr.utils.JsonFileManager;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class AbstractDataAccess<T> implements DataAccess<T> {


    abstract String getFileName();

    abstract String getDataFromApi() throws APIException, IOException, InterruptedException;

    abstract T parseData(String s);

    private String checkAndCreateResourceIfNotExist(String fileName) throws IOException {
        String filePath = "/Users/duyhuynh/data/" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return filePath;
    }

    @Override
    public void download() throws IOException, APIException, InterruptedException {
        String fileName = getFileName();
        String filePath = checkAndCreateResourceIfNotExist(fileName);
        JsonFileManager manager = new JsonFileManager(filePath);
        for (int i = 0; i < 5; i++) {
            String jsonData = getDataFromApi();
            manager.write(jsonData);
        }
    }


    @Override
    public ImmutableList<T> load() throws IOException {
        String fileName = getFileName();
        URL url = this.getClass().getClassLoader().getResource(fileName);
        if (url == null) {
            return ImmutableList.of();
        }
        JsonFileManager manager = new JsonFileManager(url.getPath());
        ImmutableList<String> jsonData = manager.read();
        List<T> result = new ArrayList<>();
        for (String s : jsonData) {
            result.add(parseData(s));
        }
        return ImmutableList.copyOf(result);
    }
}
