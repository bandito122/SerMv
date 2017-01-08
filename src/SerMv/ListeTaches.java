/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SerMv;

import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author Bob
 */
public class ListeTaches implements SourceTaches{
    
    private LinkedList listeTaches;
    public ListeTaches()
    {
        listeTaches = new LinkedList();
    }
    
    @Override
    public synchronized Socket getTache() throws InterruptedException
    {
        //System.out.println("getTache avant wait");
        while (!existTaches()) wait();
        // retourne le premier element de la linkList
        return (Socket)listeTaches.remove();
    }
    
    @Override
    public synchronized boolean existTaches()
    {
        return !listeTaches.isEmpty();
    }
    
    @Override
    public synchronized void recordTache (Socket s)
    {
       // mémorisera au moyen d'une méthode recordTache()
        listeTaches.addLast(s);
        //System.out.println("ListeTaches : tache dans la file");
        notify(); // prevenir qu'une tache est disponible, on debloque le wait de getTache
    }
}
