package dao;


public class Prenotazione {
    public int ora;
    public String giorno;
    public Docente d;
    public Corso c;
    public Utente u;
    public String stato;


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

    @Override
    public String toString() {
        return ora+" " +giorno + "-" + c.getTitulo()+"-" + d.getCognome()+","+ d.getNome() +"-" +stato;
    }
}
