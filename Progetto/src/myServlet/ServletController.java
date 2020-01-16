package myServlet;

import com.fasterxml.jackson.core.type.TypeReference;
import dao.*;
import myBeans.JSONManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static dao.DAO.*;


@WebServlet(name = "ServletController", urlPatterns = {"/ServletController"})
public class ServletController extends javax.servlet.http.HttpServlet {


    private  String mostrareOraLibera(JSONManager JSONMan) {
        ArrayList<Celda> celdas = new ArrayList<>();
        String[] giorni = {"LUNEDI","MARTEDI","MERCOLEDI","GIOVEDI","VENERDI"};
        for (int i = 15; i < 19; i++){
            for (String g:giorni) {
                celdas.add(oraLibera(g, i));
            }
        }
        return JSONMan.serializeJson(celdas);
    }

    private String mostrarePrenotazione(String account,JSONManager JSONMan){
        ArrayList<Prenotazione> prenotazioni;
        if (account.equals("admin"))
            prenotazioni = getPrenotazioni();
        else{
            prenotazioni = getPrenotazioni(account);
        }


        return JSONMan.serializeJson(prenotazioni);

    }
    /*
    private String creareDoc(String nome, String cognome, JSONManager JSONMan){
        Docente doc = new Docente(nome, cognome);
        creare(doc);
        return JSONMan.serializeJson(doc);
    }

    private String creareCorso(String corso, JSONManager JSONMan){
        Corso c = new Corso(corso);
        if (creare(c)) {
            return JSONMan.serializeJson("1");
        } else {
            return JSONMan.serializeJson("0");
        }

    }

    private String  creareAssoc(Corso c,Docente doc, JSONManager JSONMan){
        Associazione a = new Associazione(doc,c);
        creare(a);
        // La vista labora con la clase imparte.
        ArrayList<Corso> arrayCorso = new ArrayList<Corso>();
        arrayCorso.add(c);
        return JSONMan.serializeJson(new Imparte(doc,arrayCorso));
    }
     */

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();

        try (PrintWriter out = response.getWriter()) {
            JSONManager JSONMan = new JSONManager();
            String fun = request.getParameter("azione");
            if (fun.equals("connessione")) {
                String account = request.getParameter("utente");
                String pass = request.getParameter("password");
                if (correctlog(account,pass)) {
                    if (account != null && pass != null) {
                        session.setAttribute("account", account);
                        session.setAttribute("password", pass);
                    }

                    if (getRuolo(account)) {
                        out.println(JSONMan.serializeJson("ad"));
                    } else {
                        out.println(JSONMan.serializeJson("a"));
                    }
                }
                else {
                    out.println(JSONMan.serializeJson("f"));
                }
            } else if (fun.equals("giaLogin")) {
                String account = (String) session.getAttribute("account");
                String pass = (String) session.getAttribute("password");
                if (account != null && pass != null) {
                    if (correctlog(account,pass)) {
                        if (getRuolo(account)) {
                            out.println(JSONMan.serializeJson(account + "9" + pass + "9ad"));
                        } else {
                            out.println(JSONMan.serializeJson(account + "9" + pass + "9a"));
                        }
                    } else {
                        out.println(JSONMan.serializeJson(account + "9" + pass + "9f"));
                    }
                } else {
                    out.print("vuoto");
                }
            } else if (fun.equals("logout")) {
                session.invalidate();
            }
            else if (fun.equals("oraLibera")) {
                out.println(mostrareOraLibera(JSONMan));
            }
            else if (fun.equals("elencoPre")){
                String account = request.getParameter("utente");
                out.println(mostrarePrenotazione(account,JSONMan));
            }
            else if (fun.equals("elencoDoc")){
                out.println(JSONMan.serializeJson(mostrareDoc()));
            }
            else if (fun.equals("elencoAsso")){
                ArrayList<Imparte> i = mostrareAsso();
                out.println(JSONMan.serializeJson(i));
            }
            else if (fun.equals("elencoCor")){
                out.println(JSONMan.serializeJson(mostrareCor()));
            }
            else if (fun.equals("nuovoDoc")){
                String nom = request.getParameter("nome");
                String cog = request.getParameter("cognome");
                Docente doc = new Docente(nom, cog);

                if (creare(doc)) {
                    out.println(JSONMan.serializeJson("1"));
                } else {
                    out.println(JSONMan.serializeJson("0"));
                }
            }
            else if (fun.equals("rimDoc")){
                Docente d = JSONMan.parseJson(request.getParameter("docente"), Docente.class);
                eliminare(d);
            }
            else if (fun.equals("nuovoCor")){
                String cor = request.getParameter("corso");
                Corso c = new Corso(cor);

                if (creare(c)) {
                    out.println(JSONMan.serializeJson("1"));
                } else {
                    out.println(JSONMan.serializeJson("0"));
                }
            }
            else if (fun.equals("rimCorso")){
                Corso c = JSONMan.parseJson(request.getParameter("corso"), Corso.class);
                eliminare(c);
            }
            else if (fun.equals("nuovaAssoc")){
                Corso c = JSONMan.parseJson(request.getParameter("corso"), Corso.class);
                Docente d = JSONMan.parseJson(request.getParameter("docente"), Docente.class);
                Associazione a = new Associazione(d,c);

                if (creare(a)) {
                    out.print("1");
                } else {
                    out.print("0");
                }
            }
            else if (fun.equals("rimAsso")){
                Corso c = JSONMan.parseJson(request.getParameter("corso"), Corso.class);
                Docente d = JSONMan.parseJson(request.getParameter("docente"), Docente.class);
                Associazione a = new Associazione(d,c);
                eliminare(a);
            }
            else if (fun.equals("effettuare")){
                Prenotazione p = JSONMan.parseJson(request.getParameter("pre"), Prenotazione.class);
                effetuata(p);
            }
            else if (fun.equals("disdire")){
                Prenotazione p = JSONMan.parseJson(request.getParameter("pre"), Prenotazione.class);
                disdire(p);
            }
            else if (fun.equals("prenotare")){
                Prenotazione[] pre = JSONMan.parseJson(request.getParameter("prenotazioni"), Prenotazione[].class);
                for (Prenotazione p : pre){
                    prenotare(p);
                }
            }
            else {
                out.println("Accion inexistente");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
