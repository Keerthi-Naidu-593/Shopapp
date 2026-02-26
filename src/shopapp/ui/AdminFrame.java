/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {

        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Manage Products", new AdminProductPanel());
        tabs.addTab("View Orders", new AdminOrderPanel());

        JPanel logoutPanel = new JPanel();
        tabs.addTab("Logout", logoutPanel);

        tabs.addChangeListener(e -> {
            int index = tabs.getSelectedIndex();
            String title = tabs.getTitleAt(index);

            if (title.equals("Logout")) {
                dispose();
                new LoginFrame();
            }
        });

        add(tabs);

        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}