package myServlet;

import dao.*;
import myBeans.JSONManager;

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


    private String mostrarePrenotazione(String account,JSONManager JSONMan){
        ArrayList<Prenotazione> prenotazioni = getPrenotazioni(account);

        for (Prenotazione p : prenotazioni){
            System.out.println();
        }

        return JSONMan.serializeJson(prenotazioni);

    }



    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            JSONManager JSONMan = new JSONManager();
            String fun = request.getParameter("azione");
            if (fun.equals("connessione")) {
                String account = request.getParameter("utente");
                String pass = request.getParameter("password");
                if (correctlog(account,pass)) {
                    if (getRuolo(account)) {
                        out.println(JSONMan.serializeJson("ad"));
                    } else {
                        out.println(JSONMan.serializeJson("a"));
                    }
                }
                else {
                    out.println(JSONMan.serializeJson("f"));
                }
            }
            else if (fun.equals("elencoPre")){
                String account = request.getParameter("utente");
                out.println(mostrarePrenotazione(account,JSONMan));
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
