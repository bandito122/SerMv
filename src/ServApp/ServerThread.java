/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServApp;

import java.io.IOException;
import java.net.*;
import RequestResponse.ConsoleServeur;
import ServApp.SourceTaches;
import Utils.FichierConfig;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import threadpool.MyMonitorThread;
import threadpool.RejectedExecutionHandlerImpl;

/**
 *
 * @author Bob
 */
public class ServerThread extends Thread
{
    private int port; // port client - serveur
    private SourceTaches tachesAExecuter; // taches à exécuté par les threads new ListTaches()
    private ConsoleServeur guiApplication;
    private ServerSocket SSocket = null;
    int portMouvement=5000;
    int i;
    public ServerThread(int port, SourceTaches tachesAExecuter, ConsoleServeur guiApplication){
         this.port = port;
         this.tachesAExecuter = tachesAExecuter;
         this.guiApplication = guiApplication;
    }
     
    @Override
    public void run()
    {
        //Création de la socket
        try
        {  
            SSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Erreur de port d'écoute ! ? [" + e + "]"); 
            System.exit(1);
        }
       
        //Integer port_DISMAP = Integer.valueOf(FichierConfig.getProperty("DISMAP").toString());

 
            //Lancement des threads client (10)
            for(int i =  0; i < Integer.parseInt(FichierConfig.getProperty("nombreThreadMax")); i++)
            {
                ClientThread threadClient = new ClientThread (tachesAExecuter, i, guiApplication,"cmd"+i);
                threadClient.start();
            }
            //Réveil d'un des threads
            while(true)
            {
                guiApplication.TraceEvenements("En attente de connexion DISMAP...");
                try
                {   
                    Socket CSocket = SSocket.accept();
                    // va mettre une tache dans la linktable... un thread va se debloquer !
                    tachesAExecuter.recordTache(CSocket);           
                }
                catch (Exception e) 
                { 
                     System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); 
                    System.exit(1);
                }
            }
        

    }
}
