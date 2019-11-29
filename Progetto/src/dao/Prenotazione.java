package dao;

public class Prenotazione {
    private int ora;
    private String giorno;
    private Docente d;
    private Corso c;
    private Utente u;
    private String stato;


    Prenotazione(int o, String g, Docente _d, Corso _c, Utente _u, String s){
        ora = o;
        giorno = g;
        d = _d;
        c = _c;
        u = _u;
        stato = s;
    }


}
