/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AmirAli
 */

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.* ;

public class FileSender extends Thread{

    public int stop;
    public String fileName = null ;
    public TCPClientSocket recieverSocket= null;
    public FileSender(String recieverIp, int receiverPort, String _fileName)
    {
        fileName=_fileName ;
        try
        {
            recieverSocket = new TCPClientSocket(recieverIp,receiverPort);
        }
        catch(UnknownHostException e)
        {
            System.out.println("ERROR");
        }
        catch(IOException io)
        {
            System.out.println("ERROR");
        }
    }
    public void run()
    {
        int stopped = 0;
        File file = new File(fileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
 
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available()!=0) {
                if(stop==1 && stopped==0)
                {
                    System.out.println("--> STOPPING TRANSFER OF FILE: "+ fileName);
                    stopped = 1;
                    continue ;
                }
                if(stop==0 && stopped==1)
                {
                    System.out.println("--> RESUMING TRANSFER OF FILE: "+ fileName);
                    stopped = 0;
                    continue ;
                }

                String dataLine = dis.readLine() ;
                recieverSocket.WriteString(dataLine);
            }
            System.out.println("--> Good Bye ...");
            recieverSocket.socket.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
