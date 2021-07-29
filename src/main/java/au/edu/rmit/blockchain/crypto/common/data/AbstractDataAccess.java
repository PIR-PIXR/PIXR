package au.edu.rmit.blockchain.crypto.common.data;

import au.edu.rmit.blockchain.crypto.common.utils.JsonFileManager;
import au.edu.rmit.blockchain.crypto.common.utils.Setting;
import au.edu.rmit.blockchain.crypto.common.utils.http.APIException;
import com.google.common.collect.ImmutableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataAccess<T> implements DataAccess<T> {

    /**
     * Get file to write or read data
     *
     * @return file name
     */
    abstract String getFileName();

    /**
     * Fetch data from blockchain API
     *
     * @return data as json string
     */
    abstract String getDataFromApi() throws APIException, IOException;

    /**
     * Parse json to corresponding object
     *
     * @param json json string
     * @return Target object e.g. Block or Transaction
     */
    abstract T parseData(String json);

    /**
     * Check whether the source file exist. If it is not , create a new one
     *
     * @param fileName source file name
     * @return path to the file
     */
    private String checkAndCreateResourceIfNotExist(String fileName) throws IOException {
        String dataPath = Setting.DATA_HOME;
        String filePath = dataPath + fileName;
        File dataHome = new File(dataPath);
        if (!dataHome.exists()) {
            dataHome.mkdir();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return filePath;
    }

    @Override
    public void download() throws IOException, APIException {
        String fileName = getFileName();
        String filePath = checkAndCreateResourceIfNotExist(fileName);
        JsonFileManager manager = new JsonFileManager(filePath);
        String jsonData = getDataFromApi();
        manager.write(jsonData);
    }


    @Override
    public ImmutableList<T> load() throws IOException {
        String fileName = getFileName();
        String filePath = Setting.DATA_HOME + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            return ImmutableList.of();
        }
        JsonFileManager manager = new JsonFileManager(filePath);
        ImmutableList<String> jsonData = manager.read();
        List<T> result = new ArrayList<>();
        for (String s : jsonData) {
            result.add(parseData(s));
        }
        return ImmutableList.copyOf(result);
    }
}
