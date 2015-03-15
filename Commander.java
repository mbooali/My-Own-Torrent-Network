
import java.util.Scanner;
import java.util.StringTokenizer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Maziar
 */
public class Commander {

    public static void main(String [] args)
    {
        Commander co = new Commander() ;
        co.start() ;
    }

    public void start()
    {
        Scanner sc = new Scanner(System.in);
        String command =  new String();
        String ip = new String();
        String filename = new String();
        int range1,range2,filesize,port;
        

        while(true)
        {
            command = sc.nextLine();
            try
            {
                if(command.equals("exit"))
                    break;

                StringTokenizer tk = new StringTokenizer(command);
                if(tk.hasMoreTokens())
                    command = tk.nextToken();
                System.out.println(command);

                if(command.equals("SEARCH"))
                {
                    ip = tk.nextToken();
                    port = Integer.parseInt(tk.nextToken());
                }
                else if(command.equals("ADDUDP"))
                {
                    ip = tk.nextToken();
                    range1 = Integer.parseInt(tk.nextToken());
                    range2 = Integer.parseInt(tk.nextToken());
                }
                else if(command.equals("ADDTCP"))
                {
                    ip = tk.nextToken();
                    range1 = Integer.parseInt(tk.nextToken());
                    range2 = Integer.parseInt(tk.nextToken());
                }
                else if(command.equals("HAVFIL"))
                {
                    ip = tk.nextToken();
                    filename = tk.nextToken();
                    filesize = Integer.parseInt(tk.nextToken());
                }
                else if(command.equals("SEAFIL"))
                {
                    ip = tk.nextToken();
                    filename = tk.nextToken();
                }
                else if(command.equals("TRAFIL"))
                {
                    ip = tk.nextToken();
                    filename = tk.nextToken();
                    port = Integer.parseInt(tk.nextToken());
                }
                else if(command.equals("STOFIL"))
                {
                    ip = tk.nextToken();
                    filename = tk.nextToken();
                }
            }
            catch(NumberFormatException e1)
            {
                System.out.println("invalid command");
            }
                
        }

    }

}
