package dao;

public class Utente {
    private String user;
    private String pass = "";
    private Boolean admin = false;

    public Utente(String u, String p, Boolean a){
        user=u;
        pass=p;
        admin=a;
    }

    public Utente(String u){
        user=u;
    }


    public String getUser(){
        return user;
    }
    public String getPass(){
        return pass;
    }
    public Boolean isAdmin(){
        return admin;
    }

    public Utente(){}
}
