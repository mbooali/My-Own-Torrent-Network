/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author Maziar
 */
public class Initiator {


    public static int first_port;
    public static int last_port;
    public static UDPSocket broad;
    public static int BFS_Level;
    public static HashMap<String,Integer> neiHash = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException, InterruptedException
    {
        try {
            broad = new UDPSocket(8001);
            UDPSocket udpSocket = new UDPSocket();
            TCPServerSocket tcpservSock = new TCPServerSocket();

            first_port = last_port = tcpservSock.getServerSocket().getLocalPort();

            tcpservSock.getNextClient();
            FindNeighbors(udpSocket, tcpservSock);

            while(true)
            {

            }
        } catch (SocketException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void FindNeighbors(UDPSocket udpSocket, TCPServerSocket tcpservSock)
    {
        boolean weRfirst = false;
        try
        {
            udpSocket.SendLine("ADDUDP" + " " + myip() + " "
                + String.valueOf(first_port) + " " + String.valueOf(first_port)
                ,InetAddress.getByName("255.255.255.255"),8001);
            
            tcpservSock.getServerSocket().setSoTimeout(1000); System.out.println(1);
            tcpservSock.getServerSocket().setSoTimeout(1000); System.out.println(2);
            tcpservSock.getServerSocket().setSoTimeout(1000); System.out.println(3);

            tcpservSock.getServerSocket().setSoTimeout(500);
            Socket firstNeghbor = tcpservSock.getNextClient();
            String response = new String();
            response = TCPServerSocket.getInBuf(firstNeghbor).readLine();
            response = response.split(" ")[1] + " " + response.split(" ")[2]
                    + " "  + response.split(" ")[3];
            if(!neiHash.containsKey(response))
                neiHash.put(response, new Integer(0));
            tcpservSock.getServerSocket().setSoTimeout(0);
        }
        catch(java.net.SocketTimeoutException e1){
            weRfirst = true;
        }
        catch (IOException ex) {
            String[] spltnei;
            Object[] tempNeiList;
            TCPClientSocket cs;
            String s = new String();
            //we are not the first
            BFS_Level = 5;
            int i;
            for(i = 1; i <= 5; i++)
            {
                tempNeiList = neiHash.keySet().toArray();
                for(Object ss: tempNeiList)
                {
                    s = (String)ss;
                    try {
                        if(neiHash.get(s) == i-1)
                        {
                            spltnei = s.split(" ");
                            //procedure to choose port from range

                            cs = new TCPClientSocket(spltnei[0], Integer.parseInt(spltnei[1]));
                            cs.WriteString("GETNEI " + myip() + " " + String.valueOf(first_port));
                            spltnei = cs.ReadString().split(";");
                            for(String x : spltnei)
                                if(!neiHash.containsKey(x))
                                    neiHash.put(x, new Integer(i));
                        }
                    } catch (UnknownHostException ex1) {
                        Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (IOException ex1) {
                        Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }


        }


    }

    public static String myip() throws UnknownHostException
    {
        return InetAddress.getByName("localhost").toString().split("/")[1];
    }
}
