
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AmirAli
 */
public class UTTorrent {
    Node [] nodes = new Node [100];
    UDPSocket udp = null ;
    TCPServerSocket tcp = null ;
    
    public static void main(String [] args)
    {
        UTTorrent torrent = new UTTorrent ();
        MainLoop mainLoop= new MainLoop(torrent.tcp,torrent.udp);
    }
    
    public UTTorrent ()
    {
        try
        {
            udp = new UDPSocket(20003);
        }
        catch (SocketException ex)
        {
            Logger.getLogger(UTTorrent.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException e)
        {
            
        }
        try
        {
            tcp = new TCPServerSocket(10023);
        }
        catch (UnknownHostException ex)
        {
             Logger.getLogger(UTTorrent.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
             Logger.getLogger(UTTorrent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}