package dao;


public class Prenotazione {
    private int ora;
    private String giorno;
    private Docente d;
    private Corso c;
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
    public int getOra(){ return ora; }
    public String getGiorno(){
        return giorno;
    }
    public Docente getD(){
        return d;
    }
    public Corso getC(){ return c; }
    public Utente getU(){ return u; }
    public String getStato(){
        return stato;
    }

    @Override
    public String toString() {
        return ora+" " +giorno + "-" + c.getTitulo()+"-" + d.getCognome()+","+ d.getNome() +"-" +stato;
    }
}
