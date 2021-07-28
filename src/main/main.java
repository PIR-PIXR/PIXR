package main;

import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;
import json.*;
import merkletree.*;

public class main {
	
	final static String URLLatestBlock = "https://blockchain.info/latestblock";
	final static String URLSingleBlock = "https://blockchain.info/rawblock/";
	final static String URLSingleTransaction = "https://blockchain.info/rawtx/";


	public static void main(String[] args) {
		
		LatestBlock lb;
		Block b;
		Utility u = new Utility();
		DataAPI lbapi = new DataAPI();
		DataAPI sbapi = new DataAPI();
		//Latest Block
		JSONObject block = lbapi.dataAPI(URLLatestBlock);
		
		System.out.println("Latest Block Hash: " + block.getString("hash"));
		
		//Single Block
		JSONObject sblock = sbapi.dataAPI(URLSingleBlock + block.getString("hash"));
		
		//Hash Map: key = tx_index; value = tx hash
		TreeMap<Long, String> txHash = new TreeMap<Long, String>();
		txHash = sbapi.txHash();
		
		
		System.out.println("merkle root: " + sblock.getString("mrkl_root"));
		System.out.println("number of transactions: " + sblock.getInt("n_tx"));
		
  
        //  # of leaves 2^x - full merkletree
        long size = (long) Math.pow(2, u.log2(sblock.getInt("n_tx")) + 1);
        System.out.println("test : " + size);
		
		List<String> tempTxList = new ArrayList<String>();
		tempTxList = sbapi.listTxHash();
 
        Merkletrees merkleTrees = new Merkletrees(tempTxList);
        merkleTrees.merkle_tree(size);
        System.out.println("root : " + merkleTrees.getRoot());
        
        
        //Test Merkle tree with 4 transation hashes
        List<String> testTxList = new ArrayList<String>();
        testTxList.add("b7dc35d70edd340636dee8995d542008a1f149015ec37804d48c9cf21f31d5c6");
        testTxList.add("685865651c0928c891a1ef42f8613b92e7741df2696debbf6aaf0732eabd06b6");
        testTxList.add("57e2ca5c42caf7f0d29c9e9105e918150ff74a34005919a27aa1f059b7ba15dc");
        testTxList.add("5836e7095761dcfa3fd998b900feff31e41f22d0f6f0a1e629cb9f7e29db7706");
        
        Merkletrees MT = new Merkletrees(testTxList);
        MT.merkle_tree();
        System.out.println("root test : " + MT.getRoot());
		
	}

}
