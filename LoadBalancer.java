import java.net.*; 
import java.io.*;

public class LoadBalancer {

    
    
    public static void main(String[] args)
    {

        DatagramSocket aSocket = null;
        // args[0] = "3";
//        int numOfServers = Integer.parseInt(args[0]);
         int numOfServers = 1;
        try{
            aSocket = new DatagramSocket(30023);
            int[] portArr = new int[numOfServers];
            byte[] buffer = new byte[1000];
            InetAddress serverAddress = InetAddress.getByName("192.168.1.106");		

            for (int i = 0; i < numOfServers; i++) {
                portArr[i] = 20000 + i + 1;

            }

          

           //if num = 3: (int) 500000/3 = 16666 = jump
           /*1: strt 0 , end jump
           2: start 1xjump, end 2xjump
           3: start 2xjump, end 3xjump + (5000000 - 3xjump)
           */
          int[] nonceStart = new int[numOfServers];
          int[] nonceEnd = new int[numOfServers];
          int jump = 500000/numOfServers;
          int remainder = 500000 - jump*numOfServers;
          for (int i = 0; i < numOfServers; i++) {
            nonceStart[i] = jump * i;
            nonceEnd[i] = jump*(i+1);
            if (i == numOfServers -1) {
                nonceEnd[i] += remainder;
            }
          }
            System.out.println("Load Balancer is ready and accepting clients' requests ... ");
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                for (int i = 0; i < numOfServers; i++) {
                    byte[] message = ("" + nonceStart[i] + "," + nonceEnd[i] + "," + new String(request.getData(), 0, request.getLength())).getBytes();
                    System.out.println(("\n\n" + nonceStart[i] + "," + nonceEnd[i] + "," + new String(request.getData(), 0, request.getLength())));
                    DatagramPacket reply = new DatagramPacket(message, message.length,serverAddress,portArr[i]);
                    aSocket.send(reply);
                }
            }
            		

            
        }
        // int numOfServers = 5;

        // try{	    	
        //     aSocket = new DatagramSocket(20000);
        //     int serverPort1 = 20001;
        //     int serverPort2 = 20002;
        //     int serverPort3 = 20003;
        //     int serverPort4 = 20004;
        //     int serverPort5 = 20005;
        //     int serverPort6 = 20006;
        //     int serverPort7 = 20007;
        //     int serverPort8 = 20008;
        //     int serverPort9 = 20009;
        //     int serverPort10 = 20010;
        //     InetAddress serverAddress = InetAddress.getByName("192.168.1.106");
            
        //     int[] serverPort;
        //     byte[] buffer = new byte[1000]; 			
        //        System.out.println("Load Balancer is ready and accepting clients' requests ... ");
        //     while(true){ 				
        //         DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        //         aSocket.receive(request);
    
        //         DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort1);
        //         aSocket.send(reply);
        //         DatagramPacket reply2 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort2);
        //         aSocket.send(reply2);
        //         DatagramPacket reply3 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort3);
        //         aSocket.send(reply3);
        //         DatagramPacket reply4 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort4);
        //         aSocket.send(reply4);
        //         DatagramPacket reply5 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort5);
        //         aSocket.send(reply5);
        //         DatagramPacket reply6 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort6);
        //         aSocket.send(reply6);
        //         DatagramPacket reply7 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort7);
        //         aSocket.send(reply7);
        //         DatagramPacket reply8 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort8);
        //         aSocket.send(reply8);
        //         DatagramPacket reply9 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort9);
        //         aSocket.send(reply9);
        //         DatagramPacket reply10 = new DatagramPacket(request.getData(), request.getLength(),serverAddress,serverPort10);
        //         aSocket.send(reply10);
        //     }		
         catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
         }catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
        }finally {
            if(aSocket != null) aSocket.close();
        }
       } 
    }

