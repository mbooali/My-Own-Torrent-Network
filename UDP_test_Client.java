/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author Maziar
 */
public class UDP_test_Client {
    public static void main(String[] args) throws IOException{
        UDPSocket client = new UDPSocket();
        client.SendLine("Maziar",InetAddress.getByName("255.255.255.255"), 8001);
    }

}
