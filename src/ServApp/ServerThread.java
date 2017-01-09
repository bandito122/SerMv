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
       
        Integer port_DISMAP = Integer.valueOf(FichierConfig.getProperty("DISMAP").toString());
        Integer taillePool = Integer.valueOf(FichierConfig.getProperty("taillePool").toString());
        
        Integer maxtaillePool = Integer.valueOf(FichierConfig.getProperty("maxtaillePool").toString());
        Integer tailleQueue = Integer.valueOf(FichierConfig.getProperty("tailleQueue").toString());
        guiApplication.TraceEvenements("Thread Executor taillePool = " + taillePool);
        guiApplication.TraceEvenements("Thread Executor maxTaillePool = " + maxtaillePool);
        guiApplication.TraceEvenements("Thread Executor tailleQueue= " + tailleQueue);
//        if(port == port_DISMAP)
//        {
//            RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
//            ThreadFactory threadFactory = Executors.defaultThreadFactory();
//            //creer le pool
//            //poolsize = 2
//            //maximum pool = 4
//            //work queue = 2;
//            //Donc si il y a 4 taches et qu'il y a plusieurs taches encore à faire, la queue va supporter seulement 2 d'entre eux, et le reste
//            // sera "supporté" par RejectedExecutionHandlerImpl
//            ThreadPoolExecutor executorPool = new ThreadPoolExecutor(taillePool,maxtaillePool,10,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(tailleQueue),rejectionHandler);
//            //start le thread monitor
//            
//            MyMonitorThread monitor = new MyMonitorThread(executorPool,3);
//            Thread monitorThread = new Thread(monitor);
//            monitorThread.start();
//            
//            guiApplication.TraceEvenements("Thread Executor");
//            //soumettre le travaille au pool
//            //ExecutorService executor = Executors.newFixedThreadPool(5);
//            for(int i=0;i<10;i++)
//            {
//                //Runnable worker = new ClientThread (tachesAExecuter, i, guiApplication);
//                //executor.execute(worker);
//                //executorPool.execute(new ClientThread (tachesAExecuter, i, guiApplication,"cmd"+i) );
//            }
//            //Réveil d'un des threads
//            while(true)
//            {
//                guiApplication.TraceEvenements("En attente de connexion DISMAP...");
//                try
//                {   
//                    Socket CSocket = SSocket.accept();
//                    i++;
//                    executorPool.execute(new ClientThread (tachesAExecuter, i, guiApplication,"cmd"+i) );
//                    // va mettre une tache dans la linktable... un thread va se debloquer !
//                    tachesAExecuter.recordTache(CSocket);           
//                }
//                catch (Exception e) 
//                { 
//                    System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]"); 
//                    System.exit(1);
//                }
//            }
//        }
       
        if(port == port_DISMAP)
        {
            //Lancement des threads client (10)
            for(int i =  0; i < Integer.parseInt(FichierConfig.getProperty("nombreThreadMax")); i++)
            {
                ClientThread threadClient = new ClientThread (tachesAExecuter, i, guiApplication,"cmd"+i);
                threadClient.start();
            }
            //Réveil d'un des threads
            while(true)
            {
                guiApplication.TraceEvenements("En attente de connexion TRAMAP...");
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
}
