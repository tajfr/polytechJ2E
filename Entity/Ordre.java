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
public class Ordre {
    private String id;
    private String emetteur;
    private String date;
    private boolean achat;
    private String titre;
    private int quantite;
    private float prix;

    
    private String expiration;
    
    public Ordre(String ID,String emetteur,String date,boolean achat,String titre,float p, int q, String exp){
        
        this.id=ID;
        this.emetteur=emetteur;
        this.date=date;
        this.achat=achat;
        this.titre=titre;
        this.prix=p;
        this.quantite=q;
        this.expiration=exp;
        
    }
    
    public String getID(){
        return this.id;
    }
    
    public String getEmetteur(){
        return this.emetteur;
    }
    
    public String getDate(){
        return this.date;
    }
    
    public String getTitre(){
        return this.titre;
    }
    
    public int getQuantite(){
        return this.quantite;
    }
    
    public float getPrix(){
        return this.prix;
    }
    
    public boolean getSens(){
        return this.achat;
    }
    
    public String getExpiration(){
        return this.expiration;
    }
    
}
