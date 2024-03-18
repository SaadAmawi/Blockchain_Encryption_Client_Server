import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class client3 {
      public static void main(String [] args){
        
        DatagramSocket aSocket = null;
        File outputFile = new File("C:\\Users\\USER\\Desktop\\DS ASS 1\\outputClient1.txt");
        
        
        try{
            
            aSocket = new DatagramSocket();
            InetAddress serverIP = InetAddress.getByName(args[0]);
            int port = 20000;
            FileWriter fileWriter = new FileWriter(outputFile, true); // FileWriter object

            // byte [] msg = args[1].getBytes();

            for(int i =1;i<=30;i++){
                long startTime = System.nanoTime();
                String mes = new String(i+","+"This is client 3 sending message: "+i);
                DatagramPacket req = new DatagramPacket(mes.getBytes(),mes.length(),serverIP, port);
                aSocket.send(req);
                
                byte [] buffer = new byte[1024];

            DatagramPacket resp = new DatagramPacket(buffer,buffer.length);
            aSocket.receive(resp);

            String output = new String(resp.getData(),0,resp.getLength());
            long ExecutionTime = (System.nanoTime() - startTime)/1000000;
            synchronized (fileWriter) { // Synchronize the fileWriter object
                try {
                    fileWriter.write(output + "\n" + ExecutionTime + "m/s" + "\n\n");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
            System.out.println(new String(resp.getData(),0,resp.getLength()));}

        }catch(SocketException e){}catch(IOException e){}finally{
            if(aSocket != null){
                aSocket.close();
            }
        } 



    }
}
