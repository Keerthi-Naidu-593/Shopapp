package shopapp.ui;

import shopapp.dao.ProductDAO;
import shopapp.model.CartItem;
import shopapp.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class ProductPanel extends JPanel {
    private CartPanel cartPanel;
    JTable table;
    DefaultTableModel model;


   ArrayList<CartItem> cart;
   


public ProductPanel(ArrayList<CartItem> cart, CartPanel cartPanel) {
    this.cart = cart;
    this.cartPanel = cartPanel;

       setLayout(new BorderLayout());

        setOpaque(false);
        
        //setBackground(new Color(245, 247, 250));

        model = new DefaultTableModel() {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;   // no cell editable
    }
};

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Stock");

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(180, 180, 180));
        table.setSelectionBackground(new Color(184, 207, 229));

        JButton addToCartBtn = new JButton("Add To Cart");
        addToCartBtn.addActionListener(e -> addToCart());

       add(new JScrollPane(table), BorderLayout.CENTER);
       add(addToCartBtn, BorderLayout.SOUTH);

        loadProducts();
    }

    public void loadProducts() {
    model.setRowCount(0); // <<< ADD THIS LINE to clear previous rows

    ProductDAO dao = new ProductDAO();
    List<Product> products = dao.getAllProducts();

    for (Product p : products) {
        model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock()
        });
    }
}


    private void addToCart() {
        
    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a product first!");
        return;
    }

    int id = (int) model.getValueAt(row, 0);
    String name = (String) model.getValueAt(row, 1);
    double price = (double) model.getValueAt(row, 2);
    int stock = (int) model.getValueAt(row, 3);
    cartPanel.addItem(id, name, price, stock);

   

}
// Setter to link CartPanel
public void setCartPanel(CartPanel cartPanel) {
    this.cartPanel = cartPanel;
}



    // This method will be used by CartPanel
    public ArrayList<CartItem> getCart() {
        return cart;
    }
}
