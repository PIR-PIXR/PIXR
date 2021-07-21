package au.edu.rmit.blockchain.crypto.pixr;

import au.edu.rmit.blockchain.crypto.pixr.api.BlockChainApi;
import au.edu.rmit.blockchain.crypto.pixr.data.BlockDataAccess;
import au.edu.rmit.blockchain.crypto.pixr.dto.Block;
import au.edu.rmit.blockchain.crypto.pixr.dto.BlockAtHeight;
import au.edu.rmit.blockchain.crypto.pixr.dto.Input;
import au.edu.rmit.blockchain.crypto.pixr.dto.Transaction;
import au.edu.rmit.blockchain.crypto.pixr.utils.http.APIException;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.primitives.Ints;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static au.edu.rmit.blockchain.crypto.pixr.utils.Util.parseHex2Binary;
import static javax.xml.crypto.dsig.DigestMethod.SHA256;

@SuppressWarnings("UnstableApiUsage")
public class Main {
    public static void main(String[] args) throws APIException, IOException, NoSuchAlgorithmException, InterruptedException {
//        Funnel<byte[]> funnel = Funnels.byteArrayFunnel();
//        BloomFilter<byte[]> bf = BloomFilter.create(funnel, 100);
//        for (int i = 0; i < 100; i++) {
//            bf.put(Ints.toByteArray(i));
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bf.writeTo(baos);
//        byte[] bytes = baos.toByteArray();
//        for (byte b : bytes) {
//            System.out.print(Integer.toBinaryString(b & 0xFF));
//        }

        BlockDataAccess dataAccess = new BlockDataAccess();
        dataAccess.download();
    }
}
