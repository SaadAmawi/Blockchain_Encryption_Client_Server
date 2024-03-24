import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class Server6{    
   public static void main(String args[]) {     	
    DatagramSocket aSocket = null;
    try{	    	
        aSocket = new DatagramSocket(20006);
	    byte[] buffer = new byte[1000]; 			
	   	System.out.println("Server6 is ready and accepting clients' requests ... ");
		while(true){ 				
            int clientPort = 30000;
            InetAddress clientAddress = InetAddress.getByName("192.168.1.106");
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(request);

            String msg = new String(request.getData(),0,request.getLength());
            String[] parts = msg.split(",");
            int blockNum = Integer.parseInt(parts[0]);
            String data = parts[1];
            
            int nonce = findNonce(blockNum,data);
            System.out.println(nonce);
          
            
            String block = calculateBlockHash(blockNum, data, nonce);

            
            String replymsg = new String("\nServer 6 Reply:\n================================\nBlock Number is: "+ blockNum + " \n================================\nData from block: "+ data+ " \n================================\nNonce is: "+nonce+"\n================================\nHash is:"+block+"\n================================");

            if(nonce!=0){
            DatagramPacket reply = new DatagramPacket(replymsg.getBytes(), replymsg.length(),clientAddress,clientPort);            
            aSocket.send(reply);
            }
		}		
 	}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
	}finally {
		if(aSocket != null) aSocket.close();
	}
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
   public static int findNonce(int blockNumber, String data){
    int nonce = 250001;
    
            String output = calculateBlockHash(blockNumber, data, nonce); 
            while(!output.substring(0, 4).equals("0000")&& nonce<300001) {
                nonce++;
                output = calculateBlockHash(blockNumber, data, nonce); 
                }
                if (nonce == 300001 ) nonce = 0;
            
            return nonce;
}
}
