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
public class Portefeuille {
    private String IDowner;
    public float capital;
    private ArrayList<Ordre> listeOrdres;
    private ArrayList<Transaction> histoperso;
    //liste de produits financiers acquis
    
    public Portefeuille(String ID, float cap){
        this.IDowner=ID;
        this.capital=cap;
        this.listeOrdres=new ArrayList<Ordre>(200);
        //pareil
        this.histoperso=new ArrayList<Transaction>(10000);
    }
    
    public String getIDowner(){
        return this.IDowner;
    }
    
    public void addOrdre(Ordre o){
        this.listeOrdres.add(o);
    }
    
    public void addTransaction(Transaction t){
        this.histoperso.add(0,t); //on ajoute la transaction au debut, pour avoir les plus récentes en haut de tableau
    }
    
}
