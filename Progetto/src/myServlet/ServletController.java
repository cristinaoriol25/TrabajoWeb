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
        ArrayList<Prenotazione> prenotazioni;
        if (account.equals("admin"))
            prenotazioni = getPrenotazioni();
        else{
            prenotazioni = getPrenotazioni(account);
        }
        for (Prenotazione p : prenotazioni){
            System.out.println(p.getU().getUser());
        }

        return JSONMan.serializeJson(prenotazioni);

    }

    private String creareDoc(String nome, String cognome, JSONManager JSONMan){
        Docente doc = new Docente(nome, cognome);
        creare(doc);
        return JSONMan.serializeJson(doc);
    }

    private String creareCorso(String corso, JSONManager JSONMan){
        Corso c = new Corso(corso);
        creare(c);
        return JSONMan.serializeJson(c);
    }

    private void rimuovereDoc(String nome, String cognome, JSONManager JSONMan){
        Docente doc = new Docente(nome, cognome);
        eliminare(doc);
    }

    private void rimuovereCorso(String titolo, JSONManager JSONMan){
        Corso c = new Corso(titolo);
        eliminare(c);
    }

    private String  creareAssoc(String titolo,String nome, String cognome, JSONManager JSONMan){
        Corso c = new Corso(titolo);
        Docente doc = new Docente(nome, cognome);
        //Imparte imp = new Imparte(doc,c);
        return "";
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
            else if (fun.equals("elencoDoc")){
                out.println(JSONMan.serializeJson(mostrareDoc()));
            }
            else if(fun.equals("nuovoDoc")){
                String nom = request.getParameter("nome");
                String cog = request.getParameter("cognome");
                out.println(JSONMan.serializeJson(creareDoc(nom,cog,JSONMan)));
            }
            else if(fun.equals("rimDoc")){
                String nom = request.getParameter("nome");
                String cog = request.getParameter("cognome");
                rimuovereDoc(nom,cog,JSONMan);
            }
            else if (fun.equals("elencoCor")){
                out.println(JSONMan.serializeJson(mostrareCor()));
            }
            else if(fun.equals("nuovoCor")){
                String cor = request.getParameter("corso");
                out.println(JSONMan.serializeJson(creareCorso(cor,JSONMan)));
            }
            else if(fun.equals("rimCorso")){
                String cor = request.getParameter("corso");
                rimuovereCorso(cor,JSONMan);
            }
            else if(fun.equals("nuovaAssoc")){
                String cor = request.getParameter("corso");
                String nom = request.getParameter("nome");
                String cog = request.getParameter("cognome");
                out.println(JSONMan.serializeJson(creareAssoc(cor,nom,cog,JSONMan)));
            }
            else {
                    out.println("Accion inexistente");
            }






        }
    }


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        dao.DAO.registerDriver();
    }
}
