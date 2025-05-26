import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/registration_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static boolean saveUser(User user) {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "INSERT INTO users (username, phone, dob, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getDob());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());

            int result = stmt.executeUpdate();
            conn.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
