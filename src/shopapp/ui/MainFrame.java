/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;

import shopapp.model.CartItem;
import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private int userId;
    private OrderHistoryPanel historyPanel;   // 👈 declare at class level

    public MainFrame(int userId) {

        this.userId = userId;

        ArrayList<CartItem> cart = new ArrayList<>();

        // 🔥 FIRST create history panel
        historyPanel = new OrderHistoryPanel(userId);

        // THEN pass it to CartPanel
        CartPanel cartPanel = new CartPanel(cart, userId, historyPanel);

        ProductPanel productPanel = new ProductPanel(cart, cartPanel);

        cartPanel.setProductPanel(productPanel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Products", productPanel);
        tabs.addTab("Cart", cartPanel);
        tabs.addTab("Order History", historyPanel);
        JPanel logoutPanel = new JPanel();
        tabs.addTab("Logout", logoutPanel);
        add(tabs);
        tabs.addChangeListener(e -> {
    int selectedIndex = tabs.getSelectedIndex();
    String title = tabs.getTitleAt(selectedIndex);

    if (title.equals("Logout")) {
        dispose();              // Close MainFrame
        new LoginFrame();       // Open Login again
    }
});
        setTitle("Shop App");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
