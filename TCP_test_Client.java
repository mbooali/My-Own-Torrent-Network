




import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCP_test_Client {
    private static BufferedReader in = null;
    public static void main(String[] args) {
        String [] hosts = new String [100];
        int counter = 0;
        try {
            TCPClientSocket cs  =  new TCPClientSocket("127.0.0.1", 10002);
            TCPServerSocket ser =  new TCPServerSocket(1001);

            cs.WriteString("DOHAVE salam 127.0.0.1 1001");
            ser.server.setSoTimeout(2000);
            while(true)
            {
                Socket cli=ser.getNextClient();
                in=TCPServerSocket.getInBuf(cli);
                hosts[counter]=in.readLine();
                counter++;
            } 
        } catch (SocketTimeoutException ex){
            for ( int i = 0 ; i < counter ; ++i)
                System.out.println(" " + (i+1) + ": "+hosts[i]);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCP_test_Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TCP_test_Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

}
