import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseManager(String url, String username, String password) {
        this.url = "jdbc:mysql://localhost:3306/db_jokiml";;
        this.username = "root";
        this.password = "";
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setBalance(resultSet.getDouble("balance"));
                    return user;
                }
            }
        }

        return null;
    }

    public void updateBalance(int userId, double newBalance) throws SQLException {
        String query = "UPDATE users SET balance = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    public void createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (user_id, service, status) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order.getUserId());
            statement.setString(2, order.getService());
            statement.setString(3, order.getStatus());
            statement.executeUpdate();
        }
    }

    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order();
                    order.setId(resultSet.getInt("id"));
                    order.setUserId(resultSet.getInt("user_id"));
                    order.setService(resultSet.getString("service"));
                    order.setStatus(resultSet.getString("status"));
                    orders.add(order);
                }
            }
        }

        return orders;
    }
}