package dao;

import java.sql.*;

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
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Utente WHERE account='" + username + "' and pass='" + pass + "';");
            rs.next();

            return (rs.getInt("COUNT(*)") > 0);

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return false;
        }

    }

    public static boolean getRuolo(String username) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM Utente WHERE account='" + username + "';");
            rs.next();
            Boolean role = rs.getBoolean("admin");
            return role;

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return false;

        }


    }

    public static void creare(Docente d) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            st.executeUpdate("INSERT INTO `Docente` (`nome`, `cognome`, `attiva`) VALUES ('" + d.getNome() + "', '" +
                    d.getCognome() + "', '1');");


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

    public static void creare(Corso c) {
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            st.executeUpdate("INSERT INTO `Corso` (`titulo`, `attiva`) VALUES ('" +
                    c.getTitulo() + "', '1');");


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

}


