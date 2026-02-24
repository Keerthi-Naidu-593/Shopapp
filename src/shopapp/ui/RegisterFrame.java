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

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame() {

        setTitle("Register - ShopApp");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> register());

        formPanel.add(new JLabel());
        formPanel.add(registerBtn);

        panel.add(formPanel, BorderLayout.CENTER);

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

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");

            new LoginFrame();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        }
    }
}
