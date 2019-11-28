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
    //Implementar: Comprobación usuario, rol,
    public static boolean correctlog(String username, String pass){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM UTENTE WHERE account='"+username+"' and password='"+pass+"';");
            rs.next();

            return (rs.getInt("COUNT(*)") > 0);

        }catch (SQLException e) {
            System.out.println(e.getMessage());

            return false;
        }

    }
    public static boolean getRuolo(String username){
        Connection conn1 = null;
        try {
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database ripetizioni");
            }
            Statement st = conn1.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM UTENTE WHERE account='"+username+"';");
            rs.next();
            Boolean role = rs.getBoolean("ruolo");
            return role;

        }catch (SQLException e) {
            System.out.println(e.getMessage());

            return false;

        }

    }



}
