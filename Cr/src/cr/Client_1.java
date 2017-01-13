/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client_1 
{
    public static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss"); 
    public static long Timer; 
    private static String serverName;
    private static int serverPort;
    private static long NewTime;
    public static class InternalInaccuratClock extends Thread 
    {
       
        private long Drift;   
        public InternalInaccuratClock(long how_inaacurate)  
        {
            Drift=how_inaacurate;
            Timer = System.currentTimeMillis();
        }
        public void run()
        {
            
            while(true)
            {
                try 
                    {
                        Thread.sleep(1000+Drift);    
                        Timer+=1000;  
                        System.out.println(SDF.format(Timer)); 
                
                    } 
                catch (InterruptedException ex) 
                    {
                        Logger.getLogger(Client_1.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        }
    }
    public Client_1(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        
        
    }
    public static class Update  
    {
        public void run() throws IOException
        {
            Socket client;
            client = new Socket(serverName, serverPort);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);  
            int i=10;  
            out.writeInt(10);
            for(int j=0;j<i;j++)
            {
                NewTime+=in.readLong();  
            }
          
            NewTime/=i;  
       
            Timer=NewTime; 
            NewTime=0;
            System.out.println("New Time is "+SDF.format(Timer)); 
            client.close();
        }
    }
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        long clockinaccuracy;
        System.out.println("How inaccurate is the clock");
        clockinaccuracy = sc.nextLong();
        String serverName = "localhost";
        int serverPort = 1333;
        Client_1 client = new Client_1(serverName, serverPort);
        Client_1.InternalInaccuratClock C = new InternalInaccuratClock(clockinaccuracy);
        Client_1.Update U = new Update();
     
        System.out.println("How often check the clock");
        long CC=sc.nextLong();
        C.start();
        while(true)
        {
            Thread.sleep(CC);
            U.run();
        }
    }
    
}