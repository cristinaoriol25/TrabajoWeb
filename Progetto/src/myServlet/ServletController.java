package myServlet;

import dao.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static dao.DAO.*;

@WebServlet(name = "ServletController", urlPatterns = {"/ServletController"})
public class ServletController extends javax.servlet.http.HttpServlet {


    private void mostrarePrenotazione(String account){

        ArrayList<Prenotazione> prenotazioni = getPrenotazioni(account);


    }



    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            String fun = request.getParameter("azione");
            if (fun.equals("connessione")) {
                String account = request.getParameter("utente");
                String pass = request.getParameter("password");
                if (correctlog(account,pass)) {
                    if (getRuolo(account)) {
                        out.println("Hola admin");
                    } else {
                        out.println("Hola usuario");
                    }
                }
                else {
                    out.println("Usuario inexistente");
                }
            }
            else {
                    out.println("Accion inexistente");
            }






        }
    }


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        dao.DAO.registerDriver();
        processRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        dao.DAO.registerDriver();
        processRequest(request, response);
    }



}
