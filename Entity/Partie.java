/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;
import java.util.ArrayList;
import java.lang.Math;
/**
 *
 * @author Taj
 */
public class Partie {
    private int time_elapsed=0;
    private int nb_joueurs=2;
    private ArrayList<Ordre> Ordres;
    private ArrayList<Joueur> Joueurs;
    
    public Partie(int nb_joueurs){
        this.nb_joueurs+=nb_joueurs;
        
    }
    
    public int getTime(){
        return this.time_elapsed;
    }
    
    public void elapseTime(){
        this.time_elapsed++;
    }
    
    public void generateOrdres(int i){
        int k=0;
        ArrayList<Ordre> L = new ArrayList<Ordre>(i); //liste d'ordres de taille i
        boolean achat=true;
        
        while (k<i){ //génération de i ordres
            achat=!achat; //on alterne les ordre d'achat et de vente pour garder un équilibre
            double random1 = Math.random(); //diversification des ordres
            double random2 = Math.random();
            //---------------GENERATION--------------------
            //Ordre O = new Ordre(String_id, Int_qté, float_prix, bool_aumarché, achat, int_expiration);
            //L.add(O);
            
            k++;
        }
        
        this.Ordres.addAll(L);
    }
    
    public void generateJoueurs(int i){
        int k=0;
        ArrayList<Joueur> L = new ArrayList<Joueur>(i);
        
        while (k<i){
            
            k++;
        }
        
        this.Joueurs.addAll(L);
    }
    
    
    
    
}
