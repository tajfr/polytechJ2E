/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;

/**
 *
 * @author Taj
 */
public class CarnetDOrdres {
    
    private ArrayList<Ordre> listeAchat;
    private ArrayList<Ordre> listeVente;
    
    public CarnetDOrdres(){
        this.listeAchat=new ArrayList<Ordre>(100);
        this.listeVente=new ArrayList<Ordre>(100);
    }
    
    public void addOrdre(Ordre O){
        if (O.getSens()){
            this.listeAchat.add(O);
        }else{
            this.listeVente.add(O);
        }
        
    }
    
    public void removeOrdre(){
            this.listeAchat.remove(0);
            this.listeVente.remove(0);
    }
    
    public ArrayList<Ordre> getListeAchat(){
        return this.listeAchat;
    }
    
    public ArrayList<Ordre> getListeVente(){
        return this.listeVente;
    }
    
    public Transaction checkTransactions(){
        
        Transaction NULL = new Transaction();
        
        Ordre O1 = this.getListeAchat().get(0);
        Ordre O2 = this.getListeVente().get(0);
        
        if (O1.getPrix()>=O2.getPrix() && O1.getQuantite()>=O2.getQuantite()){;
            Transaction T = new Transaction(O1,O2);
            this.removeOrdre();
            return T;
        }else{
            return NULL;
        }
        
    }
    
}
