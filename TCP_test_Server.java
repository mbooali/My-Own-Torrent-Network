




import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


class TCP_test_Server {

    public static void main(String[] args) throws IOException{
            TCPServerSocket s = new TCPServerSocket(6666);
            Socket client = s.getNextClient();
            BufferedReader in = TCPServerSocket.getInBuf(client);
            PrintWriter out = TCPServerSocket.getPrintWriter(client);

            out.println("man serveram va raziam azat");
            System.out.println(in.readLine());
    }

}
