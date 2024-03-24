import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class Server2{    
   public static void main(String args[]) {     	
    DatagramSocket aSocket = null;
    try{	    	
        aSocket = new DatagramSocket(20002);
	    byte[] buffer = new byte[1000]; 			
	   	System.out.println("Server2 is ready and accepting clients' requests ... ");
		while(true){ 				
            int clientPort = 30000;
            InetAddress clientAddress = InetAddress.getByName("192.168.1.106");
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(request);
            String msg = new String(request.getData(),0,request.getLength());
            String[] parts = msg.split(",");
            int blockNum = Integer.parseInt(parts[2]);
            String data = parts[3];
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            int nonce = findNonce(start, end, blockNum,data);
            System.out.println(nonce);
          
            
            String block = calculateBlockHash(blockNum, data, nonce);

            
            String replymsg = new String("\nServer 2 Reply:\n================================\nBlock Number is: "+ blockNum + " \n================================\nData from block: "+ data+ " \n================================\nNonce is: "+nonce+"\n================================\nHash is:"+block+"\n================================");
            if(nonce!=0){
            DatagramPacket reply = new DatagramPacket(replymsg.getBytes(), replymsg.length(),clientAddress,clientPort);            aSocket.send(reply);}
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
}
