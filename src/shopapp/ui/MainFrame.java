/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;

import shopapp.model.CartItem;
import javax.swing.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    public MainFrame() {

        ArrayList<CartItem> cart = new ArrayList<>();

CartPanel cartPanel = new CartPanel(cart);
ProductPanel productPanel = new ProductPanel(cart, cartPanel);
OrderHistoryPanel historyPanel = new OrderHistoryPanel();

cartPanel.setProductPanel(productPanel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Products", productPanel);
        tabs.addTab("Cart", cartPanel);
        tabs.addTab("Order History", historyPanel);

        add(tabs);

        setTitle("Shop App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
     
    public static void main(String[] args) {
        new MainFrame();
    }
}

