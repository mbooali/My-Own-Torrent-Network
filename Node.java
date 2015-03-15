/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AmirAli
 */
public class Node {
    public String IP = "";
    public int range1 = 0;
    public int range2 = 0;
    public int udpport = 0;
    public int fileSize = 0 ;
    public String fileName = null;
    public void setValues(String _ip,int _range1, int _range2, int _udpport)
    {
        IP=_ip ;
        range1 = _range1 ;
        range2 = _range2 ;
        udpport = _udpport ;

    }
}
