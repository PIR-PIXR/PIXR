package json;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.*;

public class DataAPI {
	
	private HttpURLConnection connection;
	private BufferedReader reader;
	private String line;
	private StringBuffer data = new StringBuffer();
	private JSONObject block;

	public JSONObject dataAPI(String urlAPI) {
		
		try {
			URL url = new URL(urlAPI);
			connection = (HttpURLConnection) url.openConnection();
			
			//Request setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			if (status >= 300) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
				reader.close();
			}
			else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = reader.readLine()) != null) {
					data.append(line);
				}
				reader.close();
			}
			
			block = new JSONObject(data.toString());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		return block;
	}
	
	public TreeMap<Long, String> txHash(){
		
		JSONArray txs = block.getJSONArray("tx");
		TreeMap<Long, String> txH = new TreeMap<Long, String>();
		
		for (int i = 0; i < txs.length(); i++) {
			JSONObject tx = txs.getJSONObject(i);
			
			String hash = tx.getString("hash");
			long tx_index = tx.getLong("tx_index");
			
			txH.put(tx_index, hash);
		}
		
		return txH;
	}
	
	public List<String> listTxHash(){
		
		JSONArray txs = block.getJSONArray("tx");
		List<String> ltxH = new ArrayList<String>();
		
		for (int i = 0; i < txs.length(); i++) {
			JSONObject tx = txs.getJSONObject(i);
			String hash = tx.getString("hash");
			ltxH.add(hash);
		}
		
		return ltxH;
	}
	
}
