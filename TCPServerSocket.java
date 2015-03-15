/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maziar
 */
public class TCPServerSocket {
    
    public ServerSocket server = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private List<Socket> accepted_client = new ArrayList<Socket>();

    public  TCPServerSocket()
    {
    }

    public TCPServerSocket(int port)throws IOException
    {
        server = new ServerSocket(port);
    }

    public ServerSocket getServerSocket()
    {
        return server;
    }
    public Socket getNextClient() throws IOException
    {
        Socket accepted = server.accept();
        accepted.setSoTimeout(2000);
        accepted_client.add(accepted);
        return accepted;
    }

    public static BufferedReader getInBuf(Socket client) throws IOException
    {
        return new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public static PrintWriter getPrintWriter(Socket client) throws IOException
    {
        return new PrintWriter(client.getOutputStream(), true);
    }

    public void cleaner() throws IOException
    {
        server.close();
    }
}
