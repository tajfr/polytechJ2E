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
@WebServlet(name = "LancerSilumationServlet", urlPatterns = {"/LancerSimulationServlet"})
public class LancerSimulationServlet extends HttpServlet {

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

        String nb_ordresagenerer = request.getParameter("ordresagenerer"); 
        String nb_joueurs = request.getParameter("nbjoueurs");
        String capital = request.getParameter("capital");
            
        if (nb_ordresagenerer.equals("") || nb_joueurs.equals("") || capital.equals("")){
            alert = "Erreur : Veuillez remplir tous les champs !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("config.jsp");
        }else if (Integer.parseInt(nb_ordresagenerer)>1000){
            alert = "Erreur : Vous générez trop d'ordres !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("config.jsp");
        }else if (Integer.parseInt(nb_joueurs)>100){
            alert = "Erreur : Vous générez trop de joueurs !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("config.jsp");
        }else{
            alert = "Simulation lancée avec succès !";
            request.setAttribute("error", alert);
            dispatcher = request.getRequestDispatcher("config.jsp");
            AccessOrdres.generateJoueurs(nb_joueurs,capital);
            AccessOrdres.generateOrdres(nb_ordresagenerer);
            //AccessOrdres.writeAction("100","100","100","100","100","100");
        }
        dispatcher.forward(request, response);
        dispatcher = request.getRequestDispatcher("config.jsp");
        
        
        
        
    }


}
