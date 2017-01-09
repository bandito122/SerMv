/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServApp;

import GestionSocket.GestionSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import RequestResponse.ConsoleServeur;
import RequestResponse.IRequest;

/**
 *
 * @author Bob
 */
public class ClientThread extends Thread
{
    private SourceTaches        tachesAExecuter = null;
    private int                 numeroThread    = 0;
    private ConsoleServeur      guiApplication  = null;
    private GestionSocket GSocket         = null;
    private boolean             End             = false;
    private String              ipClient        = null;
    private int                 port            ;
    private String command;
    public ClientThread(SourceTaches tachesAExecuter, int numeroThread, ConsoleServeur guiApplication,String s)
    {
        this.tachesAExecuter    = tachesAExecuter;
        this.numeroThread       = numeroThread;
        this.guiApplication     = guiApplication;
         this.command=s;
    }
    
    public void run()
    {
           
            System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
            IRequest req = null; // interface
            Socket CSocket = null;
            try {        
                //tente de trouver du travail dans sa source de tâches
                CSocket = tachesAExecuter.getTache(); 
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
                // socket sécurité
                GSocket = new GestionSocket(CSocket);

            
            while(!isEnd())  
            {        
                guiApplication.TraceEvenements("En attente de requête ...");
                req = (IRequest) GSocket.Receive(); // on met tout dans req et on éxécute la requete
                System.out.println("On va executer la requete");
                End = req.executeRequest(GSocket, guiApplication);
                
            }
          
    }
    
    public boolean isEnd() {
        return End;
    }
}
