import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class InventoryManagementGUI {

    // Inventory management data
    private static ArrayList<Shelf> shelves = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>(); // User storage
    private static User currentUser = null; // Logged-in user

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagementGUI::createLoginFrame);
    }

    // Login Frame
    private static void createLoginFrame() {
        JFrame loginFrame = new JFrame("Inventory Management - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);

        loginFrame.setVisible(true);

        // Login action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            currentUser = authenticateUser(username, password);

            if (currentUser != null) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.dispose();
                createDashboardFrame();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register action
        registerButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(loginFrame, "Enter a new username:");
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password = JOptionPane.showInputDialog(loginFrame, "Enter a new password:");
            if (password == null || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Dashboard Frame
    private static void createDashboardFrame() {
        JFrame dashboardFrame = new JFrame("Inventory Management - Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton addShelfButton = new JButton("Add Shelf");
        JButton viewShelvesButton = new JButton("View Shelves");
        JButton deleteAllShelvesButton = new JButton("Delete All Shelves");
        JButton exitButton = new JButton("Exit");

        panel.add(addShelfButton);
        panel.add(viewShelvesButton);
        panel.add(deleteAllShelvesButton);
        panel.add(exitButton);

        dashboardFrame.add(panel);
        dashboardFrame.setVisible(true);

        // Add Shelf action
        addShelfButton.addActionListener(e -> {
            String shelfName = JOptionPane.showInputDialog(dashboardFrame, "Enter Shelf Name:");
            if (shelfName != null && !shelfName.trim().isEmpty()) {
                shelves.add(new Shelf(shelfName));
                JOptionPane.showMessageDialog(dashboardFrame, "Shelf added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dashboardFrame, "Shelf name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // View Shelves action
        viewShelvesButton.addActionListener(e -> {
            if (shelves.isEmpty()) {
                JOptionPane.showMessageDialog(dashboardFrame, "No shelves available!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                createShelvesFrame();
            }
        });

        // Delete All Shelves action
        deleteAllShelvesButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dashboardFrame, "Are you sure you want to delete all shelves?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shelves.clear();
                JOptionPane.showMessageDialog(dashboardFrame, "All shelves deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Exit action
        exitButton.addActionListener(e -> System.exit(0));
    }

    // Shelves Frame
    private static void createShelvesFrame() {
        JFrame shelvesFrame = new JFrame("Manage Shelves");
        shelvesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shelvesFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(shelves.size(), 1, 10, 10));

        for (Shelf shelf : shelves) {
            JButton shelfButton = new JButton(shelf.getName());
            panel.add(shelfButton);

            // Action for managing shelf
            shelfButton.addActionListener(e -> createShelfDetailFrame(shelf));
        }

        shelvesFrame.add(new JScrollPane(panel));
        shelvesFrame.setVisible(true);
    }

    // Shelf Detail Frame
    private static void createShelfDetailFrame(Shelf shelf) {
        JFrame shelfFrame = new JFrame("Shelf: " + shelf.getName());
        shelfFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shelfFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton addProductButton = new JButton("Add Product");
        JButton viewProductsButton = new JButton("View Products");
        JButton deleteAllProductsButton = new JButton("Delete All Products");
        JButton deleteShelfButton = new JButton("Delete Shelf");

        panel.add(addProductButton);
        panel.add(viewProductsButton);
        panel.add(deleteAllProductsButton);
        panel.add(deleteShelfButton);

        shelfFrame.add(panel);
        shelfFrame.setVisible(true);

        // Add Product action
        addProductButton.addActionListener(e -> {
            String productName = JOptionPane.showInputDialog(shelfFrame, "Enter Product Name:");
            String productStockStr = JOptionPane.showInputDialog(shelfFrame, "Enter Product Stock:");

            try {
                int stock = Integer.parseInt(productStockStr);
                if (productName != null && !productName.trim().isEmpty()) {
                    shelf.addProduct(new Product(productName, stock));
                    JOptionPane.showMessageDialog(shelfFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(shelfFrame, "Invalid product details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(shelfFrame, "Invalid stock value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // View Products action
        viewProductsButton.addActionListener(e -> {
            if (shelf.getProducts().isEmpty()) {
                JOptionPane.showMessageDialog(shelfFrame, "No products available!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                createProductsFrame(shelf);
            }
        });

        // Delete All Products action
        deleteAllProductsButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(shelfFrame, "Are you sure you want to delete all products in this shelf?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shelf.getProducts().clear();
                JOptionPane.showMessageDialog(shelfFrame, "All products in this shelf deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Delete Shelf action
        deleteShelfButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(shelfFrame, "Are you sure you want to delete this shelf?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shelves.remove(shelf);
                shelfFrame.dispose();
                JOptionPane.showMessageDialog(null, "Shelf deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // Products Frame
    private static void createProductsFrame(Shelf shelf) {
        JFrame productsFrame = new JFrame("Products in Shelf: " + shelf.getName());
        productsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productsFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(shelf.getProducts().size(), 1, 10, 10));

        for (Product product : shelf.getProducts()) {
            JButton productButton = new JButton(product.getName() + " (Stock: " + product.getStock() + ")");
            panel.add(productButton);

            // Action for managing product
            productButton.addActionListener(e -> {
                String[] options = {"Update Stock", "Delete Product"};
                int choice = JOptionPane.showOptionDialog(productsFrame, "Choose an action for " + product.getName(),
                        "Product Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == 0) { // Update Stock
                    String newStockStr = JOptionPane.showInputDialog(productsFrame, "Enter new stock for " + product.getName() + ":");
                    try {
                        int newStock = Integer.parseInt(newStockStr);
                        product.setStock(newStock);
                        JOptionPane.showMessageDialog(productsFrame, "Stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        productButton.setText(product.getName() + " (Stock: " + product.getStock() + ")");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(productsFrame, "Invalid stock value!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (choice == 1) { // Delete Product
                    shelf.getProducts().remove(product);
                    panel.remove(productButton);
                    panel.revalidate();
                    panel.repaint();
                    JOptionPane.showMessageDialog(productsFrame, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        productsFrame.add(new JScrollPane(panel));
        productsFrame.setVisible(true);
    }

    // Authenticate user
    private static User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Register user
    private static boolean registerUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password));
        return true;
    }

    // Shelf Class
    private static class Shelf {
        private String name;
        private ArrayList<Product> products = new ArrayList<>();

        public Shelf(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Product> getProducts() {
            return products;
        }

        public void addProduct(Product product) {
            products.add(product);
        }
    }

    // Product Class
    private static class Product {
        private String name;
        private int stock;

        public Product(String name, int stock) {
            this.name = name;
            this.stock = stock;
        }

        public String getName() {
            return name;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    // User Class
    private static class User {
        private String username;
        private String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
