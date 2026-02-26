/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;

import shopapp.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterFrame extends JFrame {

    private JTextField usernameField, fullNameField, emailField, phoneField;
    private JTextArea addressArea;
    private JPasswordField passwordField, confirmPasswordField;
    private JRadioButton male, female;

    public RegisterFrame() {

        setTitle("Register - ShopApp");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        // Username
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        // Full Name
        formPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        // Email
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        // Phone
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        // Address
        formPanel.add(new JLabel("Address:"));
        addressArea = new JTextArea(2, 15);
        formPanel.add(new JScrollPane(addressArea));

        // Gender
        formPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");

        ButtonGroup group = new ButtonGroup();
        group.add(male);
        group.add(female);

        genderPanel.add(male);
        genderPanel.add(female);
        formPanel.add(genderPanel);

        // Password
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        // Confirm Password
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        // Register Button
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> register());

        formPanel.add(new JLabel());
        formPanel.add(registerBtn);

        panel.add(formPanel, BorderLayout.CENTER);

        // Login link
        JButton loginLink = new JButton("Already have account? Login");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setForeground(Color.BLUE);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLink.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        panel.add(loginLink, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void register() {

        String username = usernameField.getText().trim();
        String fullname = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String gender = male.isSelected() ? "Male" :
                        female.isSelected() ? "Female" : "";

        // Validation
        if (username.isEmpty() || fullname.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || address.isEmpty() || gender.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users (username, password, fullname, email, phone, address, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, 'user')";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, fullname);
            pst.setString(4, email);
            pst.setString(5, phone);
            pst.setString(6, address);
            pst.setString(7, gender);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");

            new LoginFrame();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        }
    }
}