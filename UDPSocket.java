/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 *
 * @author Maziar
 */

public class UDPSocket {

    DatagramSocket socket = null;
    public UDPSocket() throws SocketException
    {
        socket = new DatagramSocket();
    }

    public UDPSocket(SocketAddress bindAddr) throws SocketException
    {
        socket = new DatagramSocket(bindAddr);
    }

    public UDPSocket(int port) throws SocketException
    {
        socket = new DatagramSocket(port);
    }

    public UDPSocket(int port,InetAddress iAddr ) throws SocketException
    {
        socket = new DatagramSocket(port,iAddr);
    }

    public void SendLine(String str) throws IOException
    {
        String sstr = new String(str);
        socket.send(new DatagramPacket(sstr.getBytes(), sstr.getBytes().length));
    }

    public void SendLine(String str, SocketAddress address) throws IOException
    {
        String sstr = new String(str);
        socket.send(new DatagramPacket(sstr.getBytes(), sstr.getBytes().length,address));
    }

    public void SendLine(String str,InetAddress iAddr, int port) throws IOException
    {
        String sstr = new String(str);
        socket.send(new DatagramPacket(sstr.getBytes(), sstr.getBytes().length,iAddr,port));
    }

    public String RecieveLine(int length) throws IOException
    {
        byte[] ReadBuf = new byte[length];
        DatagramPacket recpacket = new DatagramPacket(ReadBuf, ReadBuf.length);
        socket.receive(recpacket);
        return new String(recpacket.getData());
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }

}
