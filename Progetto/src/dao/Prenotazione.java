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
    public int ora(){
        return ora;
    }
    public String giorno(){
        return giorno;
    }
    public Docente docente(){
        return d;
    }
    public Corso corso(){
        return c;
    }
    public Utente utente(){
        return u;
    }
    public String stato(){
        return stato;
    }


}
