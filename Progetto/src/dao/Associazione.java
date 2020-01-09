package dao;

import java.util.ArrayList;

public class Associazione {
    private Docente d;

    private Corso corso;
    public Associazione(Docente doc, Corso c){
        d=doc;
        corso=c;
    }
    public Docente getDocente(){
        return d;
    }
    public Corso getCorso(){
        return corso;
    }

}
