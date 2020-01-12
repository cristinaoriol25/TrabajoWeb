package dao;

public class Corso {
    private String titulo;


    public Corso(String titulo) {
        this.titulo = titulo;
    }


    public String getTitulo() {
        return titulo;
    }

    @Override
    public String toString() {
        return titulo;
    }

    public Corso(){

    }
}
