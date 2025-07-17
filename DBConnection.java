import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        String url = "jdbc:mysql://localhost:3306/banking_system?useSSL=false&serverTimezone=UTC";
        String user = "root"; // Replace with your DB username
        String password = "Vickypedia@125"; // Replace with your DB password

        return DriverManager.getConnection(url, user, password);
    }
}
