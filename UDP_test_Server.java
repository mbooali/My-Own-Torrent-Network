/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.io.IOException;

/**
 *
 * @author Maziar
 */
public class UDP_test_Server {

    public static void main(String[] args) throws IOException{
        UDPSocket server = new UDPSocket(7654);

        System.out.println(server.RecieveLine(1024));
    }
}
