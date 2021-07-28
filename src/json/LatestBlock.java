package json;

import org.json.*;

// https://blockchain.info/latestblock
// {"height":692261,"hash":"0000000000000000000f14c35b2d841e986ab5441de8c585d5ffe55ea1e395ad",
// "time":1627028660,"block_index":1627028660,"txIndexes":[6097006902098300,...]}

public class LatestBlock {
	
	private int height;
	private String hash;
	private int time;
	private long block_indexes;
	private JSONArray txIndexes;
	private int numtx;
	
	public LatestBlock(int height, String hash, int time, long block_indexes, JSONArray txIndexes) {
		this.height = height;
		this.hash = hash;
		this.time = time;
		this.block_indexes = block_indexes;
		this.txIndexes = txIndexes;
		numtx = txIndexes.length();
	}
	
	public int get_height() {
		return height;
	}
	
	public String get_hash() {
		return hash;
	}
	
	public int get_time() {
		return time;
	}
	
	public long get_block_indexes() {
		return block_indexes;
	}
	
	public JSONArray get_txIndexes() {
		return txIndexes;
	}
	
	public int get_numtx() {
		return numtx;
	}

}
