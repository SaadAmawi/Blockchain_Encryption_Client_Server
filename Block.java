import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {

    int blockNumber;
    String data;
    int nonce;
    String hash;

    public Block(int blockNumber, String data, int nonce,String hash){
        this.blockNumber = blockNumber;
        this.data = data;
        this.nonce = nonce;
        this.hash = hash;
    }
    

    @Override
    public String toString() {
        return "\n==========================\nBlock Number: "+blockNumber+"\n==========================\nData: "+ data+"\n==========================\nNonce: "+ nonce+"\n==========================\nHash: "+hash;
    }

    public String calculateHash() {
        return StringUtil.getJson(blockNumber+data);
    }
    
    
    public static int findNonce(int start, int end, int blockNumber, String data){
        int nonce = start;
        
                String output = calculateBlockHash(blockNumber, data, nonce); 
                while(!output.substring(0, 4).equals("0000") && nonce <= end) {
                    nonce++;
                    output = calculateBlockHash(blockNumber, data, nonce); 
                    }
                    if (nonce == end + 1 ) nonce = 0;
                
                return nonce;
    }
    
    public static String calculateBlockHash(int blockNum,String data ,int nonce) {
        String dataToHash =  
            Integer.toString(blockNum) 
          + Integer.toString(nonce) 
          + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes());
        } catch (NoSuchAlgorithmException ex) {
        System.out.println( ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
        
    }
}
