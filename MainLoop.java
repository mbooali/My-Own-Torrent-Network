//BUG IN UDP
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author AmirAli
 */


public class MainLoop extends Thread{
    
        public  Integer  []      stops       =    new Integer   [100];
        private String   []      files       =    new String    [20];
        public  Node     []      neighbours  =    new Node      [3];
        public  TCPServerSocket  mainTcpSock =    null;
        private BufferedReader   in          =    null;
        public String            transferFileName    =    null;
        Vector <Node>            readyForTransfer = null ;
        public static int first_port;
        public static int last_port;
        public static int udpport;
        public static UDPSocket broad;
        public static int BFS_Level;
        public static HashMap<String,Integer> neiHash = new HashMap<String, Integer>();
        public static UDPSocket mainudp;
        public static boolean weRfirst = false;
        int neighbourCount = 0 ;
        int nextFreePort = 0 ;
        public FileSender [] fileSenders = new FileSender[10];
        public FileReciever [] fileRecievers = new FileReciever[10];
        public int senderCount= 0 ;
        public int recieverCount= 0 ;
        
        public void run()
        {
             Scanner sc = new Scanner(System.in);
             while(true)
             {
                transferFileName=sc.nextLine();
             }
        }
        
        public MainLoop(TCPServerSocket mainSock, UDPSocket udp)
        {
            mainTcpSock = mainSock ;
            mainudp=udp;
            first_port=mainSock.getServerSocket().getLocalPort();
            last_port=first_port+10;
            nextFreePort = first_port+1;
            Socket client = null ;
            String dataLine = null;
            String input = null;
            int counter = 0;
            int turn = 0 ;
            Scanner sc = new Scanner(System.in);
            System.out.println("Input Filenames which you want this node to have ... (END for end of input)");
            while(true)
            {
                input=sc.next();
                if(input.equals("END"))
                    break ;
                files[counter]=input ;
                counter ++;
            }
            FindNeighbors(mainSock);
            udpport = udp.socket.getLocalPort() ;
            try {
                mainSock.server.setSoTimeout(100);
                mainudp.socket.setSoTimeout(100);
            } catch (SocketException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("--> Now Online ...");
            this.start();
            while(true)
            {
                dataLine=null;
                turn++;
                try
                {
                    if(transferFileName!=null)
                    {
                        dataLine=transferFileName;
                        transferFileName=null ;
                        StringTokenizer tokenizer = new StringTokenizer(dataLine);
                        handleStdinRequest(tokenizer);
                        continue;
                    }
                    if((turn%2)==0)
                    {
                        client=mainSock.getNextClient();
                        in=TCPServerSocket.getInBuf(client);
                        dataLine=in.readLine();
                        client.close();
                    }
                    else // inja nabayad gheire markazi biyan dar yek comp
                    {
                        dataLine = mainudp.RecieveLine(1024);
                    }
                }
                catch(java.net.SocketTimeoutException e1){   
                }
                catch (SocketException ex){
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(dataLine!=null && !dataLine.equals(""))
                {
                    StringTokenizer tokenizer = new StringTokenizer(dataLine);
                    Tokenizer(tokenizer);
                }
           }
        }
        public void handleStdinRequest(StringTokenizer tokenizer)
        {
            List <Node> queue = new ArrayList <Node> () ;
            String dataLine = "" ;
            String fileName = "" ;
            int fileSize = 0 ;
            String IP= "";
            int portR1 = 0 ;
            int number = 0 ;
            TCPServerSocket server = null;
            TCPClientSocket client = null;
            Socket tempClient = null;
            BufferedReader yo = null;
            if(tokenizer.hasMoreTokens())
                dataLine = tokenizer.nextToken();
            if(dataLine.equals("Request"))
            {
                readyForTransfer = new Vector <Node> () ;
                fileName = tokenizer.nextToken();
                for (int i = 0 ; i < 3 ; ++i)
                {
                    if(neighbours[i]!=null)
                    {
                        queue.add(neighbours[i]);
                    }
                }
                try {
                    server = new TCPServerSocket(nextFreePort);
                } catch (IOException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
                for ( int i = 0 ; i < 5 ; ++i)
                {
                    int xx = queue.size();
                    for (int j = 0 ; j < xx;++j)
                    {
                        try {
                            client = new TCPClientSocket(queue.get(j).IP, queue.get(j).range1);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            client.WriteString("DOHAVE "+ fileName + " " + myip() + " " + nextFreePort);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try
                        {
                            server.server.setSoTimeout(20);
                            while(true)
                            {
                                    tempClient = server.getNextClient();
                                    tempClient.setSoTimeout(20);
                                    yo = TCPServerSocket.getInBuf(tempClient);
                                    try
                                    {
                                        while(true)
                                        {
                                            dataLine = yo.readLine();
                                            //System.out.println("HERE WITH " + dataLine);
                                            StringTokenizer tk = new StringTokenizer(dataLine);
                                            dataLine = tk.nextToken();
                                            if(dataLine.equals("HAVFIL"))
                                            {
                                                //System.out.println("HE HAD FILE");
                                                IP = tk.nextToken();
                                                portR1 = Integer.parseInt(tk.nextToken());
                                                fileName = tk.nextToken();
                                                fileSize = Integer.parseInt(tk.nextToken());
                                                Node x = new Node () ;
                                                x.setValues(IP ,portR1 ,0 ,0 );
                                                x.fileName=fileName ;
                                                boolean alreadyThere= false ;
                                                for (int m = 0; m < readyForTransfer.size();++m)
                                                {
                                                    if(readyForTransfer.elementAt(m).IP.equals(IP) && readyForTransfer.elementAt(m).range1==portR1)
                                                        alreadyThere = true ;
                                                }
                                                if(alreadyThere==false)
                                                {
                                                    readyForTransfer.add(x);
                                                    //System.out.println("ADDED");
                                                }
                                            }
                                            if(dataLine.equals("SEARCH"))
                                            {
                                                IP = tk.nextToken();
                                                portR1 = Integer.parseInt(tk.nextToken());
                                                Node x = new Node () ;
                                                x.setValues(IP ,portR1 ,0 ,0 );
                                            }
                                        }
                                    }
                                    catch(SocketTimeoutException ex)
                                    {
                                        //System.out.println("TIME UP 1");
                                    }

                                    
                            }
                        }
                        catch (SocketTimeoutException ex)
                        {
                            //System.out.println("TIMES UP"); 
                        }
                        catch (IOException ex) {
                                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                System.out.println("--> Searching Done ...");
                nextFreePort++;
                if(nextFreePort>=first_port+10)
                    nextFreePort=first_port+1;

                return;
            }
            if(dataLine.equals("Show"))
            {
                if(readyForTransfer==null)
                    return ;
                for (int i = 0 ; i < readyForTransfer.size() ; ++i)
                    System.out.println("->" + (i+1) + ": "+readyForTransfer.elementAt(i).IP + " " + readyForTransfer.elementAt(i).range1);
                return;
            }
            if(dataLine.equals("Stop"))
            {
                fileName = tokenizer.nextToken();
                for (int i = 0 ; i < 10 ; ++i)
                {
                    if(fileSenders[i]!=null)
                    {
                        if(fileSenders[i].fileName.equals(fileName))
                        {
                            fileSenders[i].stop=1;
                        }
                    }
                }
                return;
            }
            if(dataLine.equals("Resume"))
            {
                fileName = tokenizer.nextToken();
                for (int i = 0 ; i < 10 ; ++i)
                {
                    if(fileSenders[i]!=null)
                    {
                        if(fileSenders[i].fileName.equals(fileName))
                        {
                            fileSenders[i].stop=0;
                        }
                    }
                }
                return;
            }

            if(dataLine.equals("Choose"))
            {
                number = Integer.parseInt(tokenizer.nextToken());
                number--;
                try {
                    client = new TCPClientSocket(readyForTransfer.elementAt(number).IP,readyForTransfer.elementAt(number).range1);
                    server = new TCPServerSocket(nextFreePort);
                    fileRecievers[recieverCount] = new FileReciever (readyForTransfer.elementAt(number).fileName,server);
                    fileRecievers[recieverCount].start();
                    recieverCount++ ;
                    if(recieverCount>=10)
                    {
                        recieverCount = 0;
                    }

                } catch (UnknownHostException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    client.WriteString("TRAFIL "+ readyForTransfer.elementAt(number).fileName + " " + myip() + " " + nextFreePort);
                    
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
                nextFreePort++;
                if(nextFreePort>=last_port)
                {
                    nextFreePort=first_port+1;
                }
                readyForTransfer = null;
                return;
            }
        }
        public void Tokenizer(StringTokenizer tokenizer)
        {
            TCPClientSocket cs = null;
            String dataLine="";
            String fileName="";
            String IP="" ;
            int fileSize=0;
            int portR1 =0;
            int portR2 =0;
            int tempudpport = 0 ;
            //----------------------------------------------------------------------------------------
            if(tokenizer.hasMoreTokens())
                dataLine = tokenizer.nextToken();
            //----------------------------------------------------------------------------------------START OF DOHAVE
            if(dataLine.equals("DOHAVE"))
            {
                //System.out.println("--> Got file request ...");
                fileName = tokenizer.nextToken();
                IP=tokenizer.nextToken();
                portR1 = Integer.parseInt(tokenizer.nextToken());
                //System.out.println("HERE "+fileName+ " "+ IP + " "+portR1);
                boolean found = false;
                for ( int i = 0 ; i < 20 ; ++i)
                    if(files[i]!=null)
                        if(files[i].equals(fileName))
                            found =true ;
                if (found==false)
                {
                    for (int i = 0 ; i < 3 ; ++i)
                    {
                        if(neighbours[i]!=null)
                            cs.WriteString("SEARCH "+ neighbours[i].IP +" "+ neighbours[i].range1);
                    }
                    return ;
                }

                try
                {
                    cs = new TCPClientSocket(IP, portR1);
                }
                catch (UnknownHostException ex)
                {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
                catch (SocketException ex)
                {
                    System.out.println("--> ERROR: INVALID PORT AND IP COMBINATION");
                    return ;
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
                try
                {
                    //File f = new File(fileName);
                    for (int i = 0 ; i < 3 ; ++i)
                    {
                        if(neighbours[i]!=null)
                            cs.WriteString("SEARCH "+ neighbours[i].IP +" "+ neighbours[i].range1);
                    }
                    //System.out.println("I DID FIND IT" + fileName);
                    cs.WriteString("HAVFIL " + myip() +" "+ mainTcpSock.server.getLocalPort() +" " + fileName + " " + "20");//****************************************************************
                }
                catch (UnknownHostException ex)
                {
                   Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
                return ;
            }
            //---------------------------------------------------------------------------------------- END OF DOHAVE
            //---------------------------------------------------------------------------------------- START OF ADDUDP
            if(dataLine.equals("ADDUDP"))
            {
                System.out.println("--> Got broadcasted request ...");
                IP = tokenizer.nextToken();
                portR1 = Integer.parseInt(tokenizer.nextToken());
                portR2 = Integer.parseInt(tokenizer.nextToken());
                if(neighbourCount<3)
                {
                    neighbours[neighbourCount]= new Node() ;
                    neighbours[neighbourCount].setValues(IP, portR1, portR2, tempudpport);
                    neighbourCount++;
                }
                try
                {
                    cs = new TCPClientSocket(IP, portR1);
                }
                catch (UnknownHostException ex)
                {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
                catch (SocketException ex)
                {
                    System.out.println("--> ERROR: INVALID PORT AND IP COMBINATION");
                    return ;
                }
                catch (IOException ex)
                {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                    return ;
                }
                try
                {
                    cs.WriteString("ADDTCP " + myip() + " " + first_port + " " + first_port+ " " + udpport);
                    for (int i = 0 ; i < 3 ; ++i)
                    {
                        if(neighbours[i]!=null)
                        {
                            if(neighbours[i].range1!=portR1)
                            {
                                cs.WriteString("SEARCH " + neighbours[i].IP + " " + neighbours[i].udpport);
                            }
                        }
                    }
                }
                catch (UnknownHostException ex)
                {
                   Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            //------------------------------------------------------------------------------------------HAVEFILE COMMANAD
            //-------------------------------------------------------------------------------------------END OF HAVEFILE COMMANDS
            if(dataLine.equals("TRAFIL"))
            {
                System.out.println("--> Got transfer request ...");
                fileName=tokenizer.nextToken();
                IP = tokenizer.nextToken();
                portR1 = Integer.parseInt(tokenizer.nextToken());
                fileSenders[senderCount]= new FileSender (IP,portR1,fileName);
                fileSenders[senderCount].start();
                senderCount++;
                if(senderCount>=10)
                {
                    senderCount=0;
                }
                return;
            }
            //END OF IFS
            System.out.println("--> Unknown Command ...");
        }
        public void FileReciever (String fileName,int level, int threadCommandPort, int transferPort)// ListofNeighbours)
        {
            //for ( int i = 0 ; i < level ; ++i)
                //Print on their first TCP range. The Command is DOHAVE FILENAME IP PORT
                //                                                             our ip, this threads port
                //Sleep for .5 second
                //If you have read the HAVFIL IP FILENAME FILESIZE then add the node to the last queue and check if it hasn't been added already
                //if you have read the SEAFIL IP TCPPORT add the node to the next iteration of this for.
            //show the nodes to the user.
            //get the input.
            //call transferFile(Ip,threadCommandPort);
        }
    public static String myip() throws UnknownHostException
    {
        return InetAddress.getByName("localhost").toString().split("/")[1];
    }
    public void FindNeighbors(TCPServerSocket tcpservSock)
    {
        Node [] BFSNodes = new Node [100];
        Socket nextNeghbor = null ;
        int BFSCount = 0 ;
        System.out.println("--> UDP at "+mainudp.socket.getLocalPort()+ " ...");
        try
        {
            String joejoe = "";
            System.out.println("--> Sending broadcast to nodes ...");
            Scanner input = new Scanner (System.in);
            mainudp.SendLine("ADDUDP" + " " + myip() + " "
                + first_port + " " + first_port+" "
                ,InetAddress.getByName("255.255.255.255"),8001);
            while(true)
            {
                joejoe = input.nextLine();
                if(joejoe.equals("FINISH"))
                    break ;
                mainudp.SendLine("ADDUDP" + " " + myip() + " "
                    + first_port + " " + first_port+" "
                    ,InetAddress.getByName("255.255.255.255"),Integer.parseInt(joejoe));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
            tcpservSock.getServerSocket().setSoTimeout(500);
            for ( int i = 0 ; i < 3 ; ++i)
            {
                Socket firstNeghbor = tcpservSock.getNextClient();
                firstNeghbor.setSoTimeout(200);
                String response = new String();
                response = TCPServerSocket.getInBuf(firstNeghbor).readLine();
                if(!response.split(" ")[0].equals("ADDTCP"))
                    System.out.println("SERIOUS ERROR " + response.split(" ")[0]);
                System.out.println("--> Got nodes in torrent ...");
                // 3 ta a kesayi ke behet respone dadan ro be onvane
            
                neighbours[neighbourCount] = new Node ();
                neighbours[neighbourCount].setValues(response.split(" ")[1] , Integer.parseInt(response.split(" ")[2]), Integer.parseInt(response.split(" ")[3]),0);
                neighbourCount++ ;
            }
        }
        catch(java.net.SocketTimeoutException e1){
            if(neighbourCount!=0)
                return ;
            System.out.println("--> No nodes available in the torrent ...");
            weRfirst = true;
            try {
                mainudp = new UDPSocket(8001);
            } catch (SocketException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(IOException ex)
        {

        }
        /*
        for ( int i = 0 ; i < 5 ; ++i)
        {

            try {
                nextNeghbor = tcpservSock.getNextClient();
            } catch (IOException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
            String response = new String();
            try {
                response = TCPServerSocket.getInBuf(nextNeghbor).readLine();
            } catch (IOException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        //IF WE ARE NOT THE FIRST IN THE TORRENT
/*        catch (IOException ex) {
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
        }*/
    }
}
