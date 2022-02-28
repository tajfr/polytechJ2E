/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Taj
 */
public class Joueur {
    private String ID;
    private Portefeuille PTF;
    
    public Joueur(String ID){
        this.ID=ID;
        this.PTF=new Portefeuille(ID, 20000);
        
    }
}
