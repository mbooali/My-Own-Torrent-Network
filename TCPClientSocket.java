/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Maziar
 */

public class TCPClientSocket {

    public Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    public TCPClientSocket(String ip,int port) throws UnknownHostException, IOException
    {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * @return the in
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * @return the out
     */
    public PrintWriter getOut() {
        return out;
    }

    public String ReadString() throws IOException
    {
        return in.readLine();
    }

    public void WriteString(String str)
    {
        out.println(str);
    }
}
