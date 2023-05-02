import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@dbsys.cs.vsb.cz:1521:oracle", "drg0017", "iUOK211hFb4cniGF");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean isConnection() throws SQLException {
        try ( Connection conn = DatabaseConnection.getConnection() ) {
            System.out.println("Spojeni s databazi je vporadku.");
            return conn != null;
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}