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
public class Transaction {
    private float prix;
    private Ordre ordreA;
    private Ordre ordreV;
    private int quantite;
    
    public Transaction (Ordre o1, Ordre o2){
        if(!(o1.getEmetteur().equals(o2.getEmetteur()))){

            if (o1.getSens()){
                this.ordreA=o1;
                this.ordreV=o2;
                this.prix=o2.getPrix();
            }else{
                this.ordreA=o2;
                this.ordreV=o1;
                this.prix=o1.getPrix();
            }
            
            this.quantite = o1.getQuantite()-o2.getQuantite();
        }
    }
    
    public Transaction(){
        this.prix=0;
    }
    
    public float getPrix(){
        return this.prix;
    }
    
    public Ordre getOrdreA(){
        return this.ordreA;
    }
    
    public Ordre getOrdreV(){
        return this.ordreV;
    }
    
    public int getQuantite(){
        return this.quantite;
    }

}
