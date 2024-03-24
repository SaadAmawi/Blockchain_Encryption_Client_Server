import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.io.*;

public class Server{ 
	static Hashtable<Integer, Block> Blocks = new Hashtable<Integer, Block> ();
   public static void main(String args[]) {     	
    DatagramSocket aSocket = null;
    try{	    	
        aSocket = new DatagramSocket(20001);
	    byte[] buffer = new byte[1000]; 			
	   	System.out.println("Server is ready and accepting clients' requests ... ");
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
            int nonce = Block.findNonce(start, end, blockNum,data);
            System.out.println(nonce);
            String hash = Block.calculateBlockHash(blockNum, data, nonce);
            String replyMsg = "";
            
            Block e = new Block(blockNum,data,nonce,hash);
                        
            	if (Blocks.get(e.blockNumber) == null) {
            		Blocks.put(e.blockNumber,e );
            		 replyMsg = "scheduled succesfully";
            	}
            	else  replyMsg = "exists already";
            
            	System.out.println(Blocks);
//            String replymsg = new String("\nServer 1 Reply:\n================================\nBlock Number is: "+ blockNum + " \n================================\nData from block: "+ data+ " \n================================\nNonce is: "+nonce+"\n================================\nHash is:"+block+"\n================================");

            if(nonce!=0){
                DatagramPacket reply = new DatagramPacket(replyMsg.getBytes(), replyMsg.length(),clientAddress,clientPort);            
                aSocket.send(reply);
            }
		}		
 	}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
	}finally {
		if(aSocket != null) aSocket.close();
	}
   }


}
