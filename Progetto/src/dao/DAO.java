package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CompletionException;

public class DAO {
    private static final String url1 = "jdbc:mysql://localhost:3306/Ripetizione?" +
            "autoReconnect=true&useSSL=false";
    private static final String user = "admin";
    private static final String password = "admin";

    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public static boolean correctlog(String username, String pass) {
        Connection conn1 = null;
        boolean ok = false;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Utente WHERE account='" + username + "' and pass='" + pass + "';");
            rs.next();
            ok = (rs.getInt("COUNT(*)") > 0);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                    ok = false;
                }
            }
        }

        return ok;

    }

    public static boolean getRuolo(String username) {
        Connection conn1 = null;
        boolean role = false;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Utente WHERE account='" + username + "';");
            rs.next();
            role = rs.getBoolean("admin");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                    role = false;
                }
            }
        }

        return role;
    }

    public static boolean creare(Docente d) {
        Connection conn1 = null;
        boolean ok = false;

        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet r= st.executeQuery("select * from Docente where nome='"+d.getNome()+"'and cognome='"+d.getCognome()+"';");
            if(r.next()) {
                if (!r.getBoolean("attiva")) {
                    st.executeUpdate("update Docente set attiva=1 where nome='"+d.getNome()+"'and cognome='"+d.getCognome()+"' ;");
                    ok = true;
                }
            }
            else{
                st.executeUpdate("INSERT INTO `Docente` (`nome`, `cognome`, `attiva`) VALUES ('" + d.getNome() + "', '" + d.getCognome() + "', '1');");
                ok = true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return ok;
    }

    public static boolean creare(Corso c) {
        Connection conn1 = null;
        boolean ok = false;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet r= st.executeQuery("select * from Corso where titulo='"+c.getTitulo()+"';");
            if(r.next()) {
                if (!r.getBoolean("attiva")) {
                    st.executeUpdate("update Corso set attiva=1 where titulo='"+c.getTitulo()+"';");
                    ok = true;
                }
            }
            else{
                st.executeUpdate("INSERT INTO `Corso` (`titulo`, `attiva`) VALUES ('" + c.getTitulo() + "', '1');");
                ok = true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return  ok;
    }

    public static Celda oraLibera(String g, int o){
        Connection conn1 = null;
        ArrayList<Imparte> lib= new ArrayList<Imparte>();

        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Docente where attiva=1");
            ResultSet p;
            while(rs.next()){
                Docente d=new Docente(rs.getString("nome"), rs.getString("cognome"));
                Statement s1 = conn1.createStatement();
                p=s1.executeQuery("SELECT COUNT(*) FROM Prenotazione where nome='" +d.getNome()+"' and cognome='"+d.getCognome()+"' and giorno='"+g+"' and ora="+o+" and stato='ATTIVA';");
                p.next();
                if(p.getInt("COUNT(*)")==0){
                    ArrayList<Corso> c=new ArrayList<Corso>();
                    p=s1.executeQuery("SELECT corso from Imparte where nome='" +d.getNome()+"' and cognome='"+d.getCognome()+"' and attiva=1;");
                    boolean has = false;
                    while(p.next()){
                        has = true;
                        Corso co=new Corso(p.getString("corso"));
                        c.add(co);
                    }
                    if (has) {
                        Imparte i=new Imparte(d, c);
                        lib.add(i);
                    }
                }
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        Celda ora= new Celda(g, o, lib);
        return ora;

    }

    public static void prenotare(Prenotazione p){
        Connection conn1= null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            Utente u = p.getU();
            Docente d = p.getD();
            Corso c = p.getC();
            st.executeUpdate("INSERT INTO Prenotazione(`usuario`, `nome`, `cognome`, `corso`, `giorno`, `ora`, `stato`) VALUES ('"+u.getUser()+"','"+d.getNome()+"','"+d.getCognome()+"','"+c.getTitulo()+"','"+p.getGiorno()+"',"+p.getOra()+",'"+p.getStato()+"');");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static ArrayList<Prenotazione> getPrenotazioni(){
        Connection conn1 = null;

        ArrayList<Prenotazione> out = new ArrayList<>();

        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Prenotazione ;");
            while(rs.next()){
                Utente u = new Utente(rs.getString("usuario"));
                Docente d = new Docente(rs.getString("nome"),rs.getString("cognome"));
                Corso c = new Corso(rs.getString("corso"));
                String g = rs.getString("giorno");
                int ora = rs.getInt("ora");
                String s = rs.getString("stato");

                Prenotazione p = new Prenotazione(ora,g,d,c,u,s);

                out.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return out;
    }



    public static ArrayList<Prenotazione> getPrenotazioni(String utente){
        Connection conn1 = null;

        ArrayList<Prenotazione> out = new ArrayList<>();

        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Prenotazione WHERE usuario='" + utente + "';");
            while(rs.next()){
                Utente u = new Utente(rs.getString("usuario"));
                Docente d = new Docente(rs.getString("nome"),rs.getString("cognome"));
                Corso c = new Corso(rs.getString("corso"));
                String g = rs.getString("giorno");
                int ora = rs.getInt("ora");
                String s = rs.getString("stato");
                Prenotazione p = new Prenotazione(ora,g,d,c,u,s);

                out.add(p);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return out;

    }

    public static void disdire(Prenotazione p){
        Connection conn1= null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            st.executeUpdate("UPDATE Prenotazione  set stato='CANCELLATA' where usuario='"+p.getU().getUser()+"' and ora="+p.getOra()+" and giorno='"+p.getGiorno()+"'");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

    }
    public static void effetuata(Prenotazione p){
        Connection conn1= null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            st.executeUpdate("UPDATE Prenotazione set stato='EFFETTUATA' where usuario='"+p.getU().getUser()+"' and ora="+p.getOra()+" and giorno='"+p.getGiorno()+"'");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }  finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

    }

    
    public static void eliminare(Docente d) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            st.executeUpdate("UPDATE Docente SET attiva=0 WHERE nome='" + d.getNome() + "' AND cognome='" +  d.getCognome() + "';");
            st.executeUpdate("UPDATE  Imparte SET attiva=0 WHERE nome='" + d.getNome() + "' AND cognome='" + d.getCognome() + "';");
            st.executeUpdate("UPDATE  Prenotazione SET stato='CANCELLATA' WHERE nome='" + d.getNome() + "' AND cognome='" + d.getCognome() + "';");
            st.executeUpdate("UPDATE  Imparte SET attiva=0 WHERE nome='" + d.getNome() + "' AND cognome='" + d.getCognome() + "';");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    public static void eliminare(Corso c) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            st.executeUpdate("UPDATE Corso SET attiva=0 WHERE titulo='" + c.getTitulo() + "';");
            st.executeUpdate("UPDATE Imparte SET attiva=0 WHERE corso='" + c.getTitulo() + "';");
            st.executeUpdate("UPDATE  Prenotazione SET stato='CANCELLATA' WHERE  corso='" + c.getTitulo() + "';");
            st.executeUpdate("UPDATE  Imparte SET attiva=0 WHERE  corso='" + c.getTitulo() + "';");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
    }



    public static ArrayList<Docente> mostrareDoc(){
        Connection conn1 = null;
        ArrayList<Docente> d=new ArrayList<Docente>();
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Docente WHERE attiva=1;");
            while (rs.next()){
                Docente doc=new Docente(rs.getString("nome"), rs.getString("cognome"));
                d.add(doc);
            }




        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return d;
    }

    public static ArrayList<Corso> mostrareCor(){
        Connection conn1 = null;
        ArrayList<Corso> c=new ArrayList<Corso>();
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Corso WHERE attiva=1;");
            while (rs.next()){
                Corso cor=new Corso(rs.getString("titulo"));
                c.add(cor);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return c;
    }

    public static boolean creare(Associazione a){
        Connection conn1 = null;
        boolean ok = false;

        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Docente d=a.getD();
            Corso c=a.getCorso();
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Imparte WHERE nome='"+d.getNome()+"' and cognome='"+d.getCognome()+"' and corso='"+c.getTitulo()+"';");
            if (rs.next()) {
                if (!rs.getBoolean("attiva")) {
                    st.executeUpdate("update Imparte set attiva=1 where nome='"+d.getNome()+"'and  cognome='"+d.getCognome()+"' and corso='"+c.getTitulo()+"';");
                    ok = true;
                }
            } else {
                st.executeUpdate("Insert into Imparte(nome, cognome, corso, attiva) VALUES ('" + d.getNome() + "','" + d.getCognome() + "', '" + c.getTitulo() + "', 1);");
                ok = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

        return ok;
    }

    public static void eliminare(Associazione a){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Docente d=a.getD();
            Corso c=a.getCorso();
            Statement st = conn1.createStatement();
            st.executeUpdate("update Imparte set attiva=0 where nome='"+d.getNome()+"'and cognome='"+d.getCognome()+"' and corso='"+c.getTitulo()+"';");
            st.executeUpdate("update Prenotazione set stato='CANCELLATA' where nome='"+d.getNome()+"'and cognome='"+d.getCognome()+"' and corso='"+c.getTitulo()+"';");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }

    }

    public static ArrayList<Imparte> mostrareAsso(){
        Connection conn1 = null;
        ArrayList<Imparte> li=new ArrayList<Imparte>();
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Docente WHERE attiva=1;");
            while (rs.next()){
                Docente d=new Docente(rs.getString("nome"), rs.getString("cognome"));
                Statement s1=conn1.createStatement();
                Statement s2=conn1.createStatement();

                ResultSet r=s1.executeQuery("select corso from Imparte where nome='"+rs.getString("nome")+"' and cognome='"+rs.getString("cognome")+"' and attiva=1");
                ResultSet s=s2.executeQuery("select COUNT(*) from Imparte where nome='"+rs.getString("nome")+"' and cognome='"+rs.getString("cognome")+"' and attiva=1");

                ArrayList<Corso> corsos= new ArrayList<Corso>();
                int n=0;
                if(s.next()) {
                    n = s.getInt("COUNT(*)");

                    while (r.next()) {
                        Corso c = new Corso(r.getString("corso"));
                        corsos.add(c);
                    }
                    if (n != 0) {
                        Imparte i = new Imparte(d, corsos);
                        li.add(i);
                    }
                }

            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn1 != null) {
                try {
                    conn1.close();
                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                }
            }
        }
        return li;
    }

}


