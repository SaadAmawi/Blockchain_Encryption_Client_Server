import java.net.*;
import java.io.*;

public class Client{    

    public static void main(String args[]) {  
    // args[0] = message to be sent to the server; 
    // args[1] = IP address of the server
     //new branch comment
    DatagramSocket aSocket = null;
    File outputFile = new File("C:\\Users\\USER\\Desktop\\DS ASS 1 b\\output.txt");
    int count = 10;

        try {
            InetAddress serverAddress = InetAddress.getByName(args[0]);
            int serverPort = 30023;
            aSocket= new DatagramSocket(30000);
            FileWriter fileWriter = new FileWriter(outputFile, true); // FileWriter object
            long startTime = System.nanoTime();


            // for(int i =0; i<count;i++){

               startTime = System.nanoTime();
               String block = new String(3+","+" This is a message "+ 1);
                DatagramPacket request = new DatagramPacket(block.getBytes(),block.length(),serverAddress, serverPort);
                aSocket.send(request);
                
                
                
           
		                        
                byte[] buffer = new byte[1000];

            // for (int i = 0; i < count; i++) {
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                
                long ExecutionTime = (System.nanoTime() - startTime)/1000000;
                startTime = System.nanoTime();
                String output = new String(reply.getData(), 0, reply.getLength());
                try {
                    fileWriter.write(output + "\n" + ExecutionTime + "m/s" + "\n\n");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                
                System.out.println("\n Received Reply: " + output + "\n" + ExecutionTime);
            // } 
        // }
            fileWriter.close();
               

            
        }catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("Error IO: " + e.getMessage());
        }finally { 
            if(aSocket != null) aSocket.close();
        }
    }
}