/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Utils.FichierConfig;

/**
 *
 * @author bobmastrolilli
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("chemin = " + FichierConfig.getNomFichierConfig());
        System.out.println("fichier config = " + FichierConfig.getProperty("DISMAP").toString() );
    }
    
}
