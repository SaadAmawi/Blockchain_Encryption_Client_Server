
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class Server{    
    
   public static void main(String args[]) {     	
    DatagramSocket aSocket = null;
    try{	    	
        aSocket = new DatagramSocket(20000);
	    byte[] buffer = new byte[1000]; 			
	   	System.out.println("Server is ready and accepting clients' requests ... ");
		while(true){ 	
            			
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(request);

            String msg = new String(request.getData(),0,request.getLength());
            String[] parts = msg.split(",");
            String blockNum = parts[0];
            String data = parts[1];
            int blockNumber = Integer.parseInt(parts[0]);

            int nonce = findNonce(blockNumber,data);
            System.out.println(nonce);
          
            
            String block = calculateBlockHash(blockNumber, data, nonce);
            

            String replymsg = new String("================================\nBlock Number is: "+ blockNum + " \n================================\nData from block: "+ data+ " \n================================\nNonce is: "+nonce+"\n================================\nHash is:"+block+"\n================================");

            DatagramPacket reply = new DatagramPacket(replymsg.getBytes(), replymsg.length(),request.getAddress(),request.getPort());
            aSocket.send(reply);
		}		
 	}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
	}finally {
		if(aSocket != null) aSocket.close();
	}
   }

   private String hash;
   private String data;
   private int blockNum;
   private int nonce;

   public Server(String data, int blockNum) {
    this.data = data; 
    this.blockNum = blockNum;
    this.hash = calculateBlockHash(blockNum,data,nonce);
}
   public static String calculateBlockHash( int blockNum, String data, int nonce ) {
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
public static int findNonce(int blockNumber, String data){
    int nonce = 0;
            String output = "ssodifhjgsoihygosidhgsodi";
            while(!output.substring(0, 4).equals("0000")) {
                nonce++;
                output = calculateBlockHash(blockNumber, data, nonce); 
                }
            
            return nonce;
}

}
