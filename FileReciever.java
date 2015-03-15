/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AmirAli
 */
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class FileReciever extends Thread{
    public String fileName = null ;
    public TCPServerSocket mainSock = null ;
    public Socket client = null ;
    public FileReciever (String _fileName,TCPServerSocket socket)// ListofNeighbours)
    {
        fileName=_fileName ;
        mainSock=socket;
    }
    public void run()
    {
        int stopped = 0 ;
        String dataLine = null ;
        BufferedReader   in          =    null;
        FileWriter out = null;
        try
        {
            //mainSock.server.setSoTimeout(10000);
            client = mainSock.getNextClient();
            client.setSoTimeout(2000);
            in=TCPServerSocket.getInBuf(client);
            out = new FileWriter("out.txt", false);
            while(true)
            {
                dataLine=in.readLine();
                if(dataLine==null)
                    break ;
                out.write(dataLine);
                out.write("\n");
            }
        }
        catch(java.net.SocketTimeoutException e1){
        }
        catch (SocketException ex){
        }
        catch (IOException ex)
        {
        }
        if(client!=null)
        {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(FileReciever.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("--> End of transmission, file saved to output ...");
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(FileReciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        //}
}

