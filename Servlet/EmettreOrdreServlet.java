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
@WebServlet(name = "EmettreOrdreServlet", urlPatterns = {"/EmettreOrdreServlet"})
public class EmettreOrdreServlet extends HttpServlet {

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
        
        String sens = request.getParameter("emission"); //A GET_PARAMETERISER()
        String action = request.getParameter("action"); //A GET_PARAMETERISER()
        
        String prix = request.getParameter("prix");
        String qte = request.getParameter("qte");
            
        if (qte.equals("")){
            alert = "Erreur : Veuillez entrer une quantité !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("emettre.jsp");
        }else if (prix.equals("")){
            alert = "Erreur : Veuillez entrer un prix !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("emettre.jsp");
        }else{
            alert = "Ordre passé avec succès !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("ordres.jsp");
            AccessOrdres.writeOrder(emetteur, sens, action, prix, qte);
        }
        dispatcher.forward(request, response);
        dispatcher = request.getRequestDispatcher("ordres.jsp");
        
        
        
        
    }


}
