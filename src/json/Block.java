package json;

import java.util.HashMap;

// https://blockchain.info/rawblock/$block_hash
// example: https://blockchain.info/rawblock/00000000000000000003e122f026df359efa0cac0871d7d8adac137dff314faa

public class Block {

	private String prev_block;
	private String hash;
	private String next_block;
	private String mrkl_root;
	private long nonce;
	private HashMap<Double,String> txhashes = new HashMap<Double, String>();
	private int numtx;
	
	public Block(String prev_block, String hash, String next_block, String mrkl_root, long nonce, HashMap<Double,String> txhashes) {
		this.prev_block = prev_block;
		this.hash =  hash;
		this.next_block = next_block;
		this.mrkl_root = mrkl_root;
		this.nonce = nonce;
		this.txhashes = txhashes;
	}
	
	public String get_prev_block() {
		return prev_block;
	}
	
	public String get_hash() {
		return hash;
	}
	
	public String next_block() {
		return next_block;
	}
	
	public String get_mrkl_root() {
		return mrkl_root;
	}
	
	public long get_nonce() {
		return nonce;
	}
	
	public HashMap<Double,String> get_txhashes() {
		
		return txhashes;
	}
	
	public int get_numtx() {
		return numtx;
	}
}
