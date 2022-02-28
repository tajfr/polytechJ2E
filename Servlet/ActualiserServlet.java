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
@WebServlet(name = "ActualiserServlet", urlPatterns = {"/ActualiserServlet"})
public class ActualiserServlet extends HttpServlet {

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
        
        String page = request.getParameter("page");

        alert = "";
        request.setAttribute("error", alert);
        dispatcher = request.getRequestDispatcher(""+page+".jsp");
        
        AccessOrdres.writeTransactions();
        AccessOrdres.writeCoursAction();
        AccessOrdres.generateMoreOrdres(7);

        dispatcher.forward(request, response);
        dispatcher = request.getRequestDispatcher(""+page+".jsp");

    }


}
