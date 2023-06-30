/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author lenovo
 */
public class NetbensApp extends javax.swing.JFrame {
    private DatabaseManager databaseManager;
    private User currentUser;
    private JLabel balanceLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField serviceField;
    private JTextArea ordersTextArea;

    public NetbensApp(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        initializeUI();
    }
    private void initializeUI() {
        // Set up JFrame
        setTitle("Mobile Legends Joki");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        loginPanel.add(usernameLabel);

        usernameField = new JTextField();
        loginPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        loginPanel.add(loginButton);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.anchor = GridBagConstraints.WEST;

        JLabel balanceTitleLabel = new JLabel("Balance:");
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainPanel.add(balanceTitleLabel, mainConstraints);

        balanceLabel = new JLabel();
        mainConstraints.gridx = 1;
        mainConstraints.gridy = 0;
        mainPanel.add(balanceLabel, mainConstraints);

        JLabel serviceLabel = new JLabel("Service:");
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 1;
        mainPanel.add(serviceLabel, mainConstraints);

        serviceField = new JTextField();
        serviceField.setEnabled(false);
        mainConstraints.gridx = 1;
        mainConstraints.gridy = 1;
        mainPanel.add(serviceField, mainConstraints);

        JButton orderButton = new JButton("Order");
        orderButton.setEnabled(false);
        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });
        mainConstraints.gridx = 1;
        mainConstraints.gridy = 2;
        mainPanel.add(orderButton, mainConstraints);

        // Orders Panel
        JPanel ordersPanel = new JPanel(new BorderLayout());

        JLabel ordersLabel = new JLabel("Your Orders:");
        ordersPanel.add(ordersLabel, BorderLayout.NORTH);

        ordersTextArea = new JTextArea(10, 30);
        ordersTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ordersTextArea);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to the main frame
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(ordersPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            databaseManager.connect();
            User user = databaseManager.getUserByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                updateBalanceLabel();
                loadOrders();
                serviceField.setEnabled(true);
                balanceLabel.setText(String.valueOf(user.getBalance()));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            databaseManager.disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalanceLabel() {
        try {
            databaseManager.connect();
            User updatedUser = databaseManager.getUserByUsername(currentUser.getUsername());
            currentUser.setBalance((double) updatedUser.getBalance());
            balanceLabel.setText(String.valueOf(currentUser.getBalance()));
            databaseManager.disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrders() {
        try {
            databaseManager.connect();
            List<Order> orders = databaseManager.getOrdersByUserId(currentUser.getId());
            ordersTextArea.setText("");

            for (Order order : orders) {
                ordersTextArea.append("Order ID: " + order.getId() + "\n");
                ordersTextArea.append("Service: " + order.getService() + "\n");
                ordersTextArea.append("Status: " + order.getStatus() + "\n");
                ordersTextArea.append("\n");
            }

            databaseManager.disconnect();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createOrder() {
        String service = serviceField.getText();
        Order order = new Order();
        order.setUserId(currentUser.getId());
        order.setService(service);
        order.setStatus("Pending");

        try {
            databaseManager.connect();
            databaseManager.createOrder(order);
            databaseManager.disconnect();
            serviceField.setText("");
            loadOrders();
            JOptionPane.showMessageDialog(this, "Order created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Ganti dengan URL, username, dan password sesuai dengan konfigurasi database Anda
        String url = "jdbc:mysql://localhost:3306/neatbens";
        String username = "root";
        String password = "password";

        DatabaseManager databaseManager = new DatabaseManager(url, username, password);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetbensApp app = new NetbensApp(databaseManager);
            }
        });
    }
}
    /**
     * Creates new form NetbensApp
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
