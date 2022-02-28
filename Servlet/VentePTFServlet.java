/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DataBase.AccessOrdres;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Taj
 */
@WebServlet(name = "VentePTFServlet", urlPatterns = {"/VentePTFServlet"})
public class VentePTFServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                throws IOException, ServletException {
        String alert="";
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();

        String emetteur = "admin";
        String action = request.getParameter("id"); 
        String qte = request.getParameter("qte");
        String price = request.getParameter("price");
        
        int iqte = Integer.parseInt(qte);
        
        String sens="";
        
        if (iqte<0){
            sens="Achat";
            qte=Integer.toString(-iqte);
        }else if(iqte>0){
            sens="Vente";
        }
        
        if (qte.equals("0")){
            
            alert = "Erreur : Vous n'avez rien à vendre !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("ptf.jsp");
            
        }else{ 
            
            alert = "Ordre passé avec succès !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("ordres.jsp");
            AccessOrdres.writeOrder(emetteur, sens, action, price, qte);
        }
        
        dispatcher.forward(request, response);
        dispatcher = request.getRequestDispatcher("ptf.jsp");
        
        
        
        
    }


}
