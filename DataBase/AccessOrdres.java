/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

/**
 *
 * @author Taj
 */

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;

import Entity.CarnetDOrdres;
import Entity.Ordre;
import Entity.Transaction;


public class AccessOrdres {
    
    public static String generateRandomId(){
            String uniqueID = UUID.randomUUID().toString();
            return(uniqueID);
    }
    
    public static String generateRandomName(){
            
        ArrayList<String> A = new ArrayList(10);
        ArrayList<String> B = new ArrayList(10);
            
        A.add("Etienne");
        A.add("Didier");
        A.add("Anne");
        A.add("François");
        A.add("Marius");
        A.add("Gilbert");
        A.add("Camille");
        A.add("Johnny");
        A.add("Sébastien");
        A.add("Robert");
        A.add("Elise");
        A.add("Damien");
        A.add("Alfred");
        A.add("Fanny");
        A.add("Marcel");
        A.add("Giselle");
        A.add("Christian");
        A.add("Jenna");
        A.add("Sammy");
        A.add("Ronnie");
        
        B.add("Hubert");
        B.add("Frédéric");
        B.add("Thomas");
        B.add("Seyrat");
        B.add("Séveno");
        B.add("Tanré");
        B.add("Jeanjean");
        B.add("Martin");
        B.add("Michel");
        B.add("Hugues");
        B.add("OSullivan");
        B.add("Forestier");
        B.add("Tullier");
        B.add("Oudghiri");
        B.add("Carrel");
        B.add("Tonnant");
        B.add("Julien");
        B.add("Dumoulin");
        B.add("Dazza");
        B.add("Conan");
        
        int i = 0 + (int)(Math.random() * 20);
        int j = 0 + (int)(Math.random() * 20); 
            
        return(A.get(i)+" "+B.get(j));
    }

     
    public static String getDate(){
         Locale locale = Locale.getDefault();
	 java.util.Date actuelle = new java.util.Date();
         DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
         String dat = dateFormat.format(actuelle);
         return(dat);
    }

    
    public static int writeOrder( String emetteur, String sens, String action, String prix,String qte){   
                int statut=0;
        try{ 

                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                String id = generateRandomId();
                String date = getDate();
                String expiration = "29-02-2020";
                statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+emetteur+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state.getGeneratedKeys();
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
    }
    
    public static int crediterSolde(String solde){   
                int statut=0;
        try{ 

                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs1=con.createStatement().executeQuery("select * from public.joueurs where nom='admin'");
                rs1.next();
                
                Float fsolde = Float.parseFloat(solde);
                Float fcapital = Float.parseFloat(rs1.getString("capital"));
                
                Float fnewCap = fsolde+fcapital;
                
                String newCap = Float.toString(fnewCap);
                
                Statement state8 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                statut = state8.executeUpdate( "UPDATE joueurs SET capital ='"+newCap+"' WHERE nom='admin';", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state8.getGeneratedKeys();
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
    }
    

    public static int writePTF(String owner){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");

                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs1=con.createStatement().executeQuery("select * from public.transactions where (acheteur='"+owner+"' or vendeur='"+owner+"')");
                
                int qtotal=0;
                int qloreal=0;
                int qrenault=0;
                int qcarrefour=0;
                int qairliquide=0;
                int qbnpparibas=0;
                
                while(rs1.next()){
                    
                    boolean b = rs1.getString("acheteur").equals(owner);
                    String action = rs1.getString("titre");
                    String qte = rs1.getString("quantite");
                    int qtemp=0;
                    
                    if (b){
                        qtemp = Integer.parseInt(qte);
                    }else{
                        qtemp = -Integer.parseInt(qte);
                    }
                    
                    if(action.equals("Total")){
                        qtotal=qtotal+qtemp;
                    }else if(action.equals("L Oréal")){
                        qloreal=qloreal+qtemp;
                    }else if(action.equals("Renault")){
                        qrenault=qrenault+qtemp;
                    }else if(action.equals("Carrefour")){
                        qcarrefour=qcarrefour+qtemp;
                    }else if(action.equals("Air Liquide")){
                        qairliquide=qairliquide+qtemp;
                    }else if(action.equals("BNP Paribas")){
                        qbnpparibas=qbnpparibas+qtemp;
                    }
                    
                }
                
                String total = Integer.toString(qtotal);
                String loreal = Integer.toString(qloreal);
                String renault = Integer.toString(qrenault);
                String carrefour = Integer.toString(qcarrefour);
                String airliquide = Integer.toString(qairliquide);
                String bnp = Integer.toString(qbnpparibas);
                String id = generateRandomId();
                
                statut = state.executeUpdate( "INSERT INTO ptf VALUES ('"+id+"','"+owner+"','"+total+"','"+loreal+"','"+renault+"','"+carrefour+"', '"+airliquide+"','"+bnp+"');", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state.getGeneratedKeys();          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int clearPTF(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                String query1 = "DELETE FROM ptf";

                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2=statement11.executeQuery(query1);

                rs2.next();     
                rs2.updateRow();
                rs2.close();

                statement11.close();
          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int clearCoursAction(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                String query1 = "DELETE FROM cours_action";

                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2=statement11.executeQuery(query1);

                rs2.next();     
                rs2.updateRow();
                rs2.close();

                statement11.close();
          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int clearOrdres(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                String query1 = "DELETE FROM ordres";

                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2=statement11.executeQuery(query1);

                rs2.next();     
                rs2.updateRow();
                rs2.close();

                statement11.close();
          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int clearTransactions(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                String query1 = "DELETE FROM transactions";

                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2=statement11.executeQuery(query1);

                rs2.next();     
                rs2.updateRow();
                rs2.close();

                statement11.close();
          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int clearJoueurs(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                String query1 = "DELETE FROM joueurs";

                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2=statement11.executeQuery(query1);

                rs2.next();     
                rs2.updateRow();
                rs2.close();

                statement11.close();
          
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeAction(String total, String loreal, String renault, String carrefour, String airliquide, String bnp){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                statut = state.executeUpdate( "INSERT INTO cours_action VALUES (NOW(),'"+total+"','"+loreal+"','"+renault+"','"+carrefour+"', '"+airliquide+"','"+bnp+"');", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state.getGeneratedKeys();           
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeCoursAction(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                ArrayList<String> A = new ArrayList<String>(6);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                
                ArrayList<String> B = new ArrayList<String>(6);
                
                int i=0;
                
                while(i<6){
                
                Statement statement=con.createStatement();
                String action = A.get(i);
                
                //INSERTION DES TRANSACTION INITIALISEEES
                
                /*Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                String S = "CAST('2016-02-23 10:24:35.7939' AS TIMESTAMP WITHOUT TIME ZONE)";
                statut = state.executeUpdate( "INSERT INTO transactions VALUES (2016-02-23 10:24:35.7939,'test','test','test','"+action+"', '0','test');", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs35 = state.getGeneratedKeys();  */
                
                //FIN

                ResultSet rs=con.createStatement().executeQuery("select * from public.transactions where titre='"+action+"' order by id desc;");
                
                if (rs.next()){;
                
                String t = rs.getString("prix");
                B.add(t);
                
                }else{
                    B.add("100");
                }

                i++;
                }
                
                String total= B.get(0);
                String loreal= B.get(1);
                String renault= B.get(2);
                String carrefour= B.get(3);
                String airliquide= B.get(4);
                String bnp= B.get(5);
  
                
                writeAction(total,loreal,renault,carrefour,airliquide,bnp);

        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeOrderDEBITER( String emetteur, String sens, String action, String prix,String qte){   
                int statut=0;
        try{ 
                String id = generateRandomId();
                String date = getDate();
                String expiration = "29-02-2020";
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                Statement state2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                ResultSet rs1=con.createStatement().executeQuery("select * from public.joueurs where nom='admin'"); //a faire pour les joueurs fictifs aussi
                rs1.next();
                String capital = rs1.getString("capital");
                
                Float fqte = Float.parseFloat(qte);
                Float fprix = Float.parseFloat(prix);
                Float fcapital = Float.parseFloat(capital);
                
                Float m = fprix*fqte;
                
                if(emetteur.equals("admin")){
                    if(fcapital>=m){
                        String newCap = Float.toString(fcapital-m);
                        statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+emetteur+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);
                        statut = state2.executeUpdate( "UPDATE joueurs SET capital ='"+newCap+"' WHERE nom='admin';", Statement.RETURN_GENERATED_KEYS);
                        ResultSet rs = state.getGeneratedKeys();
                    }
                }else{
                    
                statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+emetteur+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = state.getGeneratedKeys();
                    
                }
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeTransactionsDEBITE_ADMIN(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                ArrayList<String> A = new ArrayList<String>(6);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                
                int i=0;
                
                while(i<6){
                
                    Statement statement=con.createStatement();
                    
                    String action = A.get(i);

                    //ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");
                    ResultSet rs_vente=con.createStatement().executeQuery("select * from public.ordres where (sens='Vente' and titre ='"+action+"') order by cast(prix as float) asc;");

                    //rs_achat.next();
                    //rs_vente.next();

                    while (rs_vente.next()){

                        ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");

                        while (rs_achat.next()){

                            String id1 = rs_achat.getString("id");
                            String id2 = rs_vente.getString("id");

                            String quantite = rs_achat.getString("quantite");
                            int iquantite = Integer.parseInt(quantite);
                            String quantite2 = rs_vente.getString("quantite");
                            int iquantite2 = Integer.parseInt(quantite2);

                            //ORDRE EN EXCES//

                            int idiff = Math.abs(iquantite2-iquantite);
                            String diff = Integer.toString(idiff);

                            String em = "";
                            String se = "";
                            String pr = "";
                            String ex = "";

                            if (iquantite<iquantite2){

                                em = rs_vente.getString("emetteur");
                                se = rs_vente.getString("sens");
                                pr = rs_vente.getString("prix");
                                ex = rs_vente.getString("expiration");

                            }else if (iquantite>iquantite2){

                                em = rs_achat.getString("emetteur");
                                se = rs_achat.getString("sens");
                                pr = rs_achat.getString("prix");
                                ex = rs_achat.getString("expiration");
                            }

                            //FIN - ORDRE EN EXCES//

                            String prix = rs_achat.getString("prix");
                            float fprix = Float.parseFloat(prix);
                            String prix2 = rs_vente.getString("prix");
                            float fprix2 = Float.parseFloat(prix2);

                            String acheteur = rs_achat.getString("emetteur");
                            String vendeur = rs_vente.getString("emetteur");

                            if (!(fprix < fprix2) && !acheteur.equals(vendeur)){

                                int q = Math.min(iquantite,iquantite2);
                                String qte = Integer.toString(q);

                                String id3 = generateRandomId();
                                String date3 = getDate();

                                String prix3 = prix2;
                                
                                Double fric = Math.floor(q*fprix2*100)/100; //argent à débiter
                                
                                if(acheteur.equals("admin") || vendeur.equals("admin")){
                                    
                                    ResultSet rs9=con.createStatement().executeQuery("select * from public.joueurs where nom='admin'"); //a faire pour les joueurs fictifs aussi
                                    rs9.next();
                                    String capital = rs9.getString("capital");
                                    Double dcapital = Double.parseDouble(capital);
                                    Double diffdouble = 0.0;
                                    
                                    if (acheteur.equals("admin")){
                                        diffdouble = dcapital-fric;
                                    }else{
                                        diffdouble = dcapital+fric;
                                    }
                                    
                                    if(diffdouble>=0){
                                        String newCap = Double.toString(diffdouble);
                                        Statement state8 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        statut = state8.executeUpdate( "UPDATE joueurs SET capital ='"+newCap+"' WHERE nom='admin';", Statement.RETURN_GENERATED_KEYS);
                                        ResultSet rs = state8.getGeneratedKeys();
                                        
                                        //ATTENTION//
                                        
                                        //INSERTION DES ORDRES RESTANTS//

                                        if (iquantite!=iquantite2){
                                            Statement state2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                            statut = state2.executeUpdate( "INSERT INTO ordres VALUES ('"+id3+"','"+em+"','"+date3+"','"+se+"','"+action+"', '"+pr+"','"+diff+"','"+ex+"');", Statement.RETURN_GENERATED_KEYS);
                                            ResultSet rs00 = state2.getGeneratedKeys();
                                        }

                                        //INSERTION DES ORDRES DANS L'HISTO DES TRANSACTIONS//

                                        Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        statut = state.executeUpdate( "INSERT INTO transactions VALUES (NOW(),'"+date3+"','"+acheteur+"','"+vendeur+"','"+action+"', '"+prix3+"','"+qte+"');", Statement.RETURN_GENERATED_KEYS);
                                        ResultSet rs0 = state.getGeneratedKeys();

                                        //SUPPRESSION DES ORDRES DANS LE CARNET//

                                        String query1 = "DELETE FROM ordres WHERE (ID='"+id1+"' or ID='"+id2+"');";

                                        Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        ResultSet rs1=statement11.executeQuery(query1);

                                        rs1.next();     
                                        rs1.updateRow();
                                        rs1.close();

                                        statement11.close();

                                    }
                                    
                                }else{

                                //INSERTION DES ORDRES RESTANTS//

                                if (iquantite!=iquantite2){
                                    Statement state2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    statut = state2.executeUpdate( "INSERT INTO ordres VALUES ('"+id3+"','"+em+"','"+date3+"','"+se+"','"+action+"', '"+pr+"','"+diff+"','"+ex+"');", Statement.RETURN_GENERATED_KEYS);
                                    ResultSet rs00 = state2.getGeneratedKeys();
                                }

                                //INSERTION DES ORDRES DANS L'HISTO DES TRANSACTIONS//

                                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                statut = state.executeUpdate( "INSERT INTO transactions VALUES (NOW(),'"+date3+"','"+acheteur+"','"+vendeur+"','"+action+"', '"+prix3+"','"+qte+"');", Statement.RETURN_GENERATED_KEYS);
                                ResultSet rs0 = state.getGeneratedKeys();

                                //SUPPRESSION DES ORDRES DANS LE CARNET//

                                String query1 = "DELETE FROM ordres WHERE (ID='"+id1+"' or ID='"+id2+"');";

                                Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                ResultSet rs1=statement11.executeQuery(query1);

                                rs1.next();     
                                rs1.updateRow();
                                rs1.close();

                                statement11.close();

                                }
                            }

                        }

                    }


                    i++;
                }

        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeTransactions(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                ArrayList<String> A = new ArrayList<String>(6);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                
                int i=0;
                
                while(i<6){
                
                    Statement statement=con.createStatement();
                    
                    String action = A.get(i);

                    //ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");
                    ResultSet rs_vente=con.createStatement().executeQuery("select * from public.ordres where (sens='Vente' and titre ='"+action+"') order by cast(prix as float) asc;");

                    //rs_achat.next();
                    //rs_vente.next();

                    while (rs_vente.next()){

                        ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");

                        while (rs_achat.next()){

                            String id1 = rs_achat.getString("id");
                            String id2 = rs_vente.getString("id");

                            String quantite = rs_achat.getString("quantite");
                            int iquantite = Integer.parseInt(quantite);
                            String quantite2 = rs_vente.getString("quantite");
                            int iquantite2 = Integer.parseInt(quantite2);

                            //ORDRE EN EXCES//

                            int idiff = Math.abs(iquantite2-iquantite);
                            String diff = Integer.toString(idiff);

                            String em = "";
                            String se = "";
                            String pr = "";
                            String ex = "";

                            if (iquantite<iquantite2){

                                em = rs_vente.getString("emetteur");
                                se = rs_vente.getString("sens");
                                pr = rs_vente.getString("prix");
                                ex = rs_vente.getString("expiration");

                            }else if (iquantite>iquantite2){

                                em = rs_achat.getString("emetteur");
                                se = rs_achat.getString("sens");
                                pr = rs_achat.getString("prix");
                                ex = rs_achat.getString("expiration");
                            }

                            //FIN - ORDRE EN EXCES//

                            String prix = rs_achat.getString("prix");
                            float fprix = Float.parseFloat(prix);
                            String prix2 = rs_vente.getString("prix");
                            float fprix2 = Float.parseFloat(prix2);

                            String acheteur = rs_achat.getString("emetteur");
                            String vendeur = rs_vente.getString("emetteur");

                            if (!(fprix < fprix2) && !acheteur.equals(vendeur)){

                                int q = Math.min(iquantite,iquantite2);
                                String qte = Integer.toString(q);

                                String id3 = generateRandomId();
                                String date3 = getDate();

                                String prix3 = prix2;
                                
                                Double fric = Math.floor(q*fprix2*100)/100; //argent à débiter
                                
                                ResultSet rs9=con.createStatement().executeQuery("select * from public.joueurs where nom='"+acheteur+"'"); //a faire pour les joueurs fictifs aussi
                                rs9.next();
                                String capital_a = rs9.getString("capital");
                                Double dcapital_a = Double.parseDouble(capital_a);
                                Double diffdouble_a = 0.0;
                                diffdouble_a = Math.floor((dcapital_a-fric)*100)/100;
                                
                                ResultSet rs10=con.createStatement().executeQuery("select * from public.joueurs where nom='"+vendeur+"'"); //a faire pour les joueurs fictifs aussi
                                rs10.next();
                                String capital_v = rs10.getString("capital");
                                Double dcapital_v = Double.parseDouble(capital_v);
                                Double diffdouble_v = 0.0;
                                diffdouble_v = Math.floor((dcapital_v+fric)*100)/100;
                                
                                if(diffdouble_a>=0){
                                        String newCap = Double.toString(diffdouble_a);
                                        Statement state8 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        statut = state8.executeUpdate( "UPDATE joueurs SET capital ='"+newCap+"' WHERE nom='"+acheteur+"';", Statement.RETURN_GENERATED_KEYS);
                                        ResultSet rs = state8.getGeneratedKeys();
                                        
                                        String newCapV = Double.toString(diffdouble_v);
                                        Statement state78 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        statut = state78.executeUpdate( "UPDATE joueurs SET capital ='"+newCapV+"' WHERE nom='"+vendeur+"';", Statement.RETURN_GENERATED_KEYS);
                                        ResultSet rs78 = state78.getGeneratedKeys();
                                        
                                        //ATTENTION//
                                        
                                        //INSERTION DES ORDRES RESTANTS//

                                        if (iquantite!=iquantite2){
                                            Statement state2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                            statut = state2.executeUpdate( "INSERT INTO ordres VALUES ('"+id3+"','"+em+"','"+date3+"','"+se+"','"+action+"', '"+pr+"','"+diff+"','"+ex+"');", Statement.RETURN_GENERATED_KEYS);
                                            ResultSet rs00 = state2.getGeneratedKeys();
                                        }

                                        //INSERTION DES ORDRES DANS L'HISTO DES TRANSACTIONS//

                                        Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        statut = state.executeUpdate( "INSERT INTO transactions VALUES (NOW(),'"+date3+"','"+acheteur+"','"+vendeur+"','"+action+"', '"+prix3+"','"+qte+"');", Statement.RETURN_GENERATED_KEYS);
                                        ResultSet rs0 = state.getGeneratedKeys();

                                        //SUPPRESSION DES ORDRES DANS LE CARNET//

                                        String query1 = "DELETE FROM ordres WHERE (ID='"+id1+"' or ID='"+id2+"');";

                                        Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                        ResultSet rs1=statement11.executeQuery(query1);

                                        rs1.next();     
                                        rs1.updateRow();
                                        rs1.close();

                                        statement11.close();

                                }

                            }

                        }

                    }


                    i++;
                }

        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int writeTransactionsCELLEQUIMARCHE(){   
                int statut=0;
        try{ 
                
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                
                ArrayList<String> A = new ArrayList<String>(6);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                
                int i=0;
                
                while(i<6){
                
                Statement statement=con.createStatement();

                String action = A.get(i);
                
                //ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");
                ResultSet rs_vente=con.createStatement().executeQuery("select * from public.ordres where (sens='Vente' and titre ='"+action+"') order by cast(prix as float) asc;");
                
                //rs_achat.next();
                //rs_vente.next();
                
                while (rs_vente.next()){
                    
                    ResultSet rs_achat=con.createStatement().executeQuery("select * from public.ordres where (sens='Achat' and titre ='"+action+"') order by cast(prix as float) desc;");
                    
                    while (rs_achat.next()){
                
                String id1 = rs_achat.getString("id");
                String id2 = rs_vente.getString("id");
                
                String quantite = rs_achat.getString("quantite");
                int iquantite = Integer.parseInt(quantite);
                String quantite2 = rs_vente.getString("quantite");
                int iquantite2 = Integer.parseInt(quantite2);
                
                //ORDRE EN EXCES//
                
                int idiff = Math.abs(iquantite2-iquantite);
                String diff = Integer.toString(idiff);
                
                String em = "";
                String se = "";
                String pr = "";
                String ex = "";
                
                if (iquantite<iquantite2){
                    
                    em = rs_vente.getString("emetteur");
                    se = rs_vente.getString("sens");
                    pr = rs_vente.getString("prix");
                    ex = rs_vente.getString("expiration");
                
                }else if (iquantite>iquantite2){
                   
                    em = rs_achat.getString("emetteur");
                    se = rs_achat.getString("sens");
                    pr = rs_achat.getString("prix");
                    ex = rs_achat.getString("expiration");
                }
                
                //FIN - ORDRE EN EXCES//

                String prix = rs_achat.getString("prix");
                float fprix = Float.parseFloat(prix);
                String prix2 = rs_vente.getString("prix");
                float fprix2 = Float.parseFloat(prix2);
                    
                String acheteur = rs_achat.getString("emetteur");
                String vendeur = rs_vente.getString("emetteur");
                    
                if (!(fprix < fprix2) && !acheteur.equals(vendeur)){

                    int q = Math.min(iquantite,iquantite2);
                    String qte = Integer.toString(q);

                    String id3 = generateRandomId();
                    String date3 = getDate();
                        
                    String prix3 = prix2;
                    
                    //INSERTION DES ORDRES RESTANTS//
                    
                    if (iquantite!=iquantite2){
                        Statement state2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        statut = state2.executeUpdate( "INSERT INTO ordres VALUES ('"+id3+"','"+em+"','"+date3+"','"+se+"','"+action+"', '"+pr+"','"+diff+"','"+ex+"');", Statement.RETURN_GENERATED_KEYS);
                        ResultSet rs00 = state2.getGeneratedKeys();
                    }
                    
                    //INSERTION DES ORDRES DANS L'HISTO DES TRANSACTIONS//
                    
                    Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    statut = state.executeUpdate( "INSERT INTO transactions VALUES (NOW(),'"+date3+"','"+acheteur+"','"+vendeur+"','"+action+"', '"+prix3+"','"+qte+"');", Statement.RETURN_GENERATED_KEYS);
                    ResultSet rs0 = state.getGeneratedKeys();
                    
                    //SUPPRESSION DES ORDRES DANS LE CARNET//
                    
                    String query1 = "DELETE FROM ordres WHERE (ID='"+id1+"' or ID='"+id2+"');";
                            
                    Statement statement11=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs1=statement11.executeQuery(query1);

                    rs1.next();     
                    rs1.updateRow();
                    rs1.close();

                    statement11.close();


                }
                
                //i++;
                
                }
                
                }

                
                i++;
                }

        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static String getCoursAction(){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.cours_action order by id desc;");  
               
                rs.next();

                    affichage+="<tr><td>Total</td>";
                    affichage+="<td>"+rs.getString("total")+" €</td></tr>";
                    affichage+="<tr><td>L'Oréal</td>";
                    affichage+="<td>"+rs.getString("loreal")+" €</td></tr>";
                    affichage+="<tr><td>Renault</td>";
                    affichage+="<td>"+rs.getString("renault")+" €</td></tr>";
                    affichage+="<tr><td>Carrefour</td>";
                    affichage+="<td>"+rs.getString("carrefour")+" €</td></tr>";
                    affichage+="<tr><td>Air Liquide</td>";
                    affichage+="<td>"+rs.getString("airliquide")+" €</td></tr>";
                    affichage+="<tr><td>BNP Paribas</td>";
                    affichage+="<td>"+rs.getString("bnpparibas")+" €</td></tr>";
                   
                
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    public static String getOrdres(){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.ordres where emetteur='admin';");  
               
                while(rs.next()){

                    affichage+="<tr><td>"+rs.getString("date")+"</td>";
                    
                    affichage+="<td>"+rs.getString("sens")+"</td>";
  
                    affichage+="<td>"+rs.getString("titre")+"</td>";

                    affichage+="<td>"+rs.getString("prix")+" EUR</td>";

                    affichage+="<td>"+rs.getString("quantite")+"</td>";
                    
                    affichage+="<td>"+rs.getString("expiration")+"</td></tr>";
                   
                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static String getTransactions(){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.transactions where (acheteur='admin' or vendeur='admin') order by id desc;");  
                //int i = 0; && i<15
                while(rs.next()){

                    affichage+="<tr><td>"+rs.getString("date")+"</td>";
                    
                    String sens="";
                    
                    if (rs.getString("vendeur").equals("admin")){
                        affichage+="<td>"+rs.getString("acheteur")+"</td>";
                        sens = "Vente";
                    }else{
                        affichage+="<td>"+rs.getString("vendeur")+"</td>";
                        sens = "Achat";
                    }    
                    
                    affichage+="<td>"+sens+"</td>";
  
                    affichage+="<td>"+rs.getString("titre")+"</td>";

                    affichage+="<td>"+rs.getString("prix")+" EUR</td>";   
                    
                    affichage+="<td>"+rs.getString("quantite")+"</td></tr>";
                   
                    //i++;
                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static String getCOachat(String action){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.ordres where sens='Achat' and titre='"+action+"' order by cast(prix as float) desc;");  
               
                int i = 0;
                
                while(rs.next() && i<14){

                    affichage+="<tr><td>"+rs.getString("quantite")+"</td>";

                    affichage+="<td>"+rs.getString("prix")+"</td></tr>";
                    
                    i++;
                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static String getCOvente(String action){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.ordres where sens='Vente' and titre='"+action+"' order by cast(prix as float) asc;");  
                
                int i=0;
                while(rs.next() && i<14){

                    affichage+="<tr><td>"+rs.getString("prix")+"</td>";

                    affichage+="<td>"+rs.getString("quantite")+"</td></tr>";
                    
                    i++;
                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static String getCapital(String owner){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();  
                
                ResultSet rs=statement.executeQuery("select * from public.joueurs where nom='"+owner+"';");  
                
                if (rs.next()){
                    affichage+=rs.getString("capital")+" €";
                }else{
                    affichage+="0 €";
                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static int actualiserPTF(){   
                int statut=0;
        try{ 

                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                Statement statement=con.createStatement();
                ResultSet rs=statement.executeQuery("select * from public.joueurs");

                
                while(rs.next()){
                    writePTF(rs.getString("nom"));
                }

                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static String getPTF(String owner){
            String affichage= "";
            try{  
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");  
                
                Statement statement=con.createStatement();
                Statement statement2=con.createStatement();
                
                ResultSet rs=statement.executeQuery("select * from public.ptf where owner='"+owner+"'");
                ResultSet rs2=statement2.executeQuery("select * from public.cours_action order by id desc"); 
                
                rs2.next();
               
                while(rs.next()){
                    
                    Float ftotal = Float.parseFloat(rs.getString("total"))*Float.parseFloat(rs2.getString("total"));
                    Float floreal = Float.parseFloat(rs.getString("loreal"))*Float.parseFloat(rs2.getString("loreal"));
                    Float frenault = Float.parseFloat(rs.getString("renault"))*Float.parseFloat(rs2.getString("renault"));
                    Float fcarrefour = Float.parseFloat(rs.getString("carrefour"))*Float.parseFloat(rs2.getString("carrefour"));
                    Float fairliquide = Float.parseFloat(rs.getString("airliquide"))*Float.parseFloat(rs2.getString("airliquide"));
                    Float fbnp = Float.parseFloat(rs.getString("bnpparibas"))*Float.parseFloat(rs2.getString("bnpparibas"));
                    
                    Double dtotal = Math.floor(ftotal*100)/100;
                    Double dloreal = Math.floor(floreal*100)/100;
                    Double dcarrefour = Math.floor(fcarrefour*100)/100;
                    Double drenault = Math.floor(frenault*100)/100;
                    Double dairliquide = Math.floor(fairliquide*100)/100;
                    Double dbnp = Math.floor(fbnp*100)/100;
                    
                    String ptotal = Double.toString(dtotal);
                    String ploreal = Double.toString(dloreal);
                    String prenault = Double.toString(drenault);
                    String pcarrefour = Double.toString(dcarrefour);
                    String pairliquide = Double.toString(dairliquide);
                    String pbnp = Double.toString(dbnp);
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='Total' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("total")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("total")+"' readonly>";
                    affichage+="<td>Total</td>";
                    affichage+="<td>"+rs.getString("total")+"</td>";
                    affichage+="<td>"+rs2.getString("total")+" €</td>"; 
                    affichage+="<td>"+ptotal+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form>";
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='L Oréal' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("loreal")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("loreal")+"' readonly>";
                    affichage+="<td>L'Oréal</td>";
                    affichage+="<td>"+rs.getString("loreal")+"</td>";
                    affichage+="<td>"+rs2.getString("loreal")+" €</td>"; 
                    affichage+="<td>"+ploreal+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form>";
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='Renault' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("renault")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("renault")+"' readonly>";
                    affichage+="<td>Renault</td>";
                    affichage+="<td>"+rs.getString("renault")+"</td>";
                    affichage+="<td>"+rs2.getString("renault")+" €</td>";
                    affichage+="<td>"+prenault+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form>";
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='Carrefour' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("carrefour")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("carrefour")+"' readonly>";
                    affichage+="<td>Carrefour</td>";
                    affichage+="<td>"+rs.getString("carrefour")+"</td>";
                    affichage+="<td>"+rs2.getString("carrefour")+" €</td>";
                    affichage+="<td>"+pcarrefour+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form>";
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='Air Liquide' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("airliquide")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("airliquide")+"' readonly>";
                    affichage+="<td>Air Liquide</td>";
                    affichage+="<td>"+rs.getString("airliquide")+"</td>";
                    affichage+="<td>"+rs2.getString("airliquide")+" €</td>";
                    affichage+="<td>"+pairliquide+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form>";
                    
                    affichage+="<tr><form name='PTF' action='VentePTFServlet' method='post' ><td style=\"display:none\"><input type='hidden' name='id' value='BNP Paribas' readonly>";
                    affichage+="<input type='hidden' name='qte' value='"+rs.getString("bnpparibas")+"' readonly>";
                    affichage+="<input type='hidden' name='price' value='"+rs2.getString("bnpparibas")+"' readonly>";
                    affichage+="<td>BNP Paribas</td>";
                    affichage+="<td>"+rs.getString("bnpparibas")+"</td>";
                    affichage+="<td>"+rs2.getString("bnpparibas")+" €</td>";
                    affichage+="<td>"+pbnp+" €</td>";
                    affichage+="<td><input type=\"submit\" name=\"i\" value=\"Vendre\"></td></form></tr>";

                }
              
            }catch(Exception e){e.printStackTrace();}  
            return affichage;  
        }
    
    public static int generateJoueurs(String nb_joueurs, String capital){   
                int statut=0;
        try{ 
                int i = 0;
                int j = 0;
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                int m = Integer.parseInt(nb_joueurs);
                
                ArrayList<String> J = new ArrayList<String>(m);
                
                statut = state.executeUpdate( "INSERT INTO joueurs VALUES ('admin','admin','"+capital+"','admin');", Statement.RETURN_GENERATED_KEYS);

                while (i<m){
                
                    J.add(generateRandomName());    
                    String id = generateRandomId();
                    String idptf = generateRandomId();
                    String name = J.get(i);

                    statut = state.executeUpdate( "INSERT INTO joueurs VALUES ('"+id+"','"+name+"','"+capital+"','"+idptf+"');", Statement.RETURN_GENERATED_KEYS);

                    i++;
                }
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int generateOrdres(String nb_ordresagenerer){   
                int statut=0;
        try{ 
                int i = 0;
                int j = 0;
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                Statement statement=con.createStatement();
                ResultSet rs=statement.executeQuery("select * from public.joueurs where nom!='admin'");
                
                ArrayList<String> J = new ArrayList<String>();
                
                while(rs.next()){
                    J.add(rs.getString("nom"));
                }

                String date = getDate();
                String expiration = "29-02-2020";
                
                int n = Integer.parseInt(nb_ordresagenerer);
                
                int min = 70;
                int max = 130;
                
                ArrayList<String> A = new ArrayList<String>(7);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                A.add("CAC 40");
                
                boolean b = true;
                
                while (i<n){
                    
                String id = generateRandomId();
                    
                b=!b;

                int q = (int) Math.floor((Math.random()*(max-min))+1);
                String qte = Integer.toString(q);
                
                int k = i%7;
                int r = i%J.size();
                
                String action = A.get(k);
                
                String sens="";
                String prix="";
                
                if(b){
                    sens = "Achat";
                    Double p = Math.floor((70 + Math.random()*(50))*100)/100;
                    prix = Double.toString(p);
                }else{
                    sens = "Vente";
                    Double p = Math.floor((100 + Math.random()*(50))*100)/100;
                    prix = Double.toString(p);
                }
                
                String name = J.get(r);
                
                statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+name+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);

                i++;

                }
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int generateMoreOrdres(int n){   
                int statut=0;
        try{ 
                int i = 0;
                int j = 0;
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                Statement statement=con.createStatement();
                ResultSet rs=statement.executeQuery("select * from public.joueurs where nom!='admin'");
                
                ArrayList<String> J = new ArrayList<String>();
                
                while(rs.next()){
                    J.add(rs.getString("nom"));
                }
                
                Statement statement2=con.createStatement();
                ResultSet rs2=statement2.executeQuery("select * from public.cours_action order by id desc");
                
                rs2.next();
                
                String date = getDate();
                String expiration = "29-02-2020";
                
                
                
                int min = 70;
                int max = 130;
                
                ArrayList<String> A = new ArrayList<String>(7);
                A.add("Total");
                A.add("L Oréal");
                A.add("Renault");
                A.add("Carrefour");
                A.add("Air Liquide");
                A.add("BNP Paribas");
                A.add("CAC 40");
                
                boolean b = true;
                
                while (i<n){
                    
                String id = generateRandomId();
                    
                b=!b;

                int q = (int) Math.floor((Math.random()*(max-min))+1);
                String qte = Integer.toString(q);
                
                int k = i%7;
                int r = i%J.size();
                
                String action = A.get(k);
                String cours ="";
                
                if (action.equals("Total")){
                    cours = rs2.getString("total");
                }else if (action.equals("L Oréal")){
                    cours = rs2.getString("loreal");
                }else if (action.equals("Renault")){
                    cours = rs2.getString("renault");
                }else if (action.equals("Carrefour")){
                    cours = rs2.getString("carrefour");
                }else if (action.equals("Air Liquide")){
                    cours = rs2.getString("airliquide");
                }else if (action.equals("BNP Paribas")){
                    cours = rs2.getString("bnpparibas");
                }else{
                    cours = "100";
                }
                
                String sens="";
                String prix="";
                
                double z1 = Math.random();
                double z2 = Math.random();
                double av = Math.random();
                
                double bm = Math.sqrt(-2*Math.log(z1))*Math.cos(2*(Math.PI)*z2);
                double dcours = Double.parseDouble(cours);
                bm = bm*10+dcours;
                
                if (av>0.5){
                    sens = "Achat";
                }else{
                    sens = "Vente";
                }
                
                if(b){
                    
                    Double p = Math.floor((dcours + bm)*50)/100;
                    prix = Double.toString(p);
                }else{
                    
                    Double p = Math.floor((dcours + bm)*50)/100;
                    prix = Double.toString(p);
                }
                
                String name = J.get(r);
                
                statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+name+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);

                i++;

                }
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
    
    public static int generateOrdresByAction(int n,String action){   
                int statut=0;
        try{ 
                int i = 0;
                int j = 0;
                Class.forName("org.postgresql.Driver");  
                Connection con=DriverManager.getConnection(  
                "jdbc:postgresql://localhost:5432/PFE","postgres","souktani");
                Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
                Statement statement=con.createStatement();
                ResultSet rs=statement.executeQuery("select * from public.joueurs where nom!='admin'");
                
                ArrayList<String> J = new ArrayList<String>();
                
                while(rs.next()){
                    J.add(rs.getString("nom"));
                }
                
                Statement statement2=con.createStatement();
                ResultSet rs2=statement2.executeQuery("select * from public.cours_action order by id desc");
                
                rs2.next();
                
                String date = getDate();
                String expiration = "29-02-2020";
                
                
                
                int min = 70;
                int max = 130;
                
                
                
                boolean b = true;
                
                while (i<n){
                    
                String id = generateRandomId();
                    
                b=!b;

                int q = (int) Math.floor((Math.random()*(max-min))+1);
                String qte = Integer.toString(q);
                
                
                int r = i%J.size();
                
                
                String cours ="";
                
                
                if (action.equals("Total")){
                    cours = rs2.getString("total");
                }else if (action.equals("L Oréal")){
                    cours = rs2.getString("loreal");
                }else if (action.equals("Renault")){
                    cours = rs2.getString("renault");
                }else if (action.equals("Carrefour")){
                    cours = rs2.getString("carrefour");
                }else if (action.equals("Air Liquide")){
                    cours = rs2.getString("airliquide");
                }else if (action.equals("BNP Paribas")){
                    cours = rs2.getString("bnpparibas");
                }else{
                    cours = "100";
                }
                
                String sens="";
                String prix="";
                
                double z1 = Math.random();
                double z2 = Math.random();
                double av = Math.random();
                
                double bm = Math.sqrt(-2*Math.log(z1))*Math.cos(2*(Math.PI)*z2);
                double dcours = Double.parseDouble(cours);
                bm = bm*10+dcours;
                
                if (av>0.5){
                    sens = "Achat";
                }else{
                    sens = "Vente";
                }
                
                if(b){
                    
                    Double p = Math.floor((dcours + bm)*50)/100;
                    prix = Double.toString(p);
                }else{
                    
                    Double p = Math.floor((dcours + bm)*50)/100;
                    prix = Double.toString(p);
                }
                
                String name = J.get(r);
                
                statut = state.executeUpdate( "INSERT INTO ordres VALUES ('"+id+"','"+name+"','"+date+"','"+sens+"','"+action+"','"+prix+"', '"+qte+"','"+expiration+"');", Statement.RETURN_GENERATED_KEYS);

                i++;

                }
                
        }catch(Exception e){e.printStackTrace();}  
                return statut;
        }
}
