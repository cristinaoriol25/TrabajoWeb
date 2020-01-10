package dao;

import java.util.ArrayList;

public class Celda {
    private ArrayList<Imparte> libres;
    private String giorno;
    private int ora;
    public Celda(String g, int o, ArrayList<Imparte> imp){
        giorno=g;
        ora=o;
        libres=imp;
    }
    public ArrayList<Imparte> getLibre(){
        return libres;
    }
    public ArrayList<Imparte> getOra(){
        return ora;
    }
    public ArrayList<Imparte> getGiorno(){
        return giorno;
    }


}
