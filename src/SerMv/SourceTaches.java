/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SerMv;

import java.net.Socket;

/**
 *
 * @author Bob
 */
public interface SourceTaches {
    
    public Socket getTache() throws InterruptedException;
    public boolean existTaches();
    public void recordTache (Socket s);
}
