package dao;

import java.util.ArrayList;

public class Imparte {
    private Docente d;
    private ArrayList<Corso> corso;
    public Imparte(Docente doc, ArrayList<Corso> c){
        d=doc;
        corso=c;
    }
    public Docente getDocente(){
        return d;
    }
    public ArrayList<Corso> getCorso(){
        return corso;
    }
    public void addCorso(Corso c){
        corso.add(c);
    }

}
