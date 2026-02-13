package shopapp.ui;

import shopapp.model.CartItem;
import shopapp.dao.ProductDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CartPanel extends JPanel {
    private JLabel totalLabel;

    JTable table;
    DefaultTableModel model;
    ArrayList<CartItem> cart;
    private ProductPanel productPanel;
    public CartPanel(ArrayList<CartItem> cart) {

        this.cart = cart;
        setLayout(new BorderLayout());
        
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Qty");
        model.addColumn("Total");

        table = new JTable(model);
        table.setRowHeight(30);

        // Buttons
JButton minusBtn = new JButton("-");
minusBtn.addActionListener(e -> decreaseQuantity());

JButton plusBtn = new JButton("+");
plusBtn.addActionListener(e -> increaseQuantity());

JButton removeBtn = new JButton("Remove");
removeBtn.addActionListener(e -> removeItem());

JButton checkoutBtn = new JButton("Checkout");
checkoutBtn.addActionListener(e -> checkout());

// Total Label
totalLabel = new JLabel("Grand Total: ₹0.0");
totalLabel.setFont(new Font("Arial", Font.BOLD, 14));

// Left side button panel
JPanel leftPanel = new JPanel();
leftPanel.add(minusBtn);
leftPanel.add(plusBtn);
leftPanel.add(removeBtn);
leftPanel.add(checkoutBtn);

// Bottom panel layout
JPanel bottomPanel = new JPanel(new BorderLayout());
bottomPanel.add(leftPanel, BorderLayout.WEST);
bottomPanel.add(totalLabel, BorderLayout.EAST);

// Add components
add(new JScrollPane(table), BorderLayout.CENTER);
add(bottomPanel, BorderLayout.SOUTH);

        




    }
    public void setProductPanel(ProductPanel productPanel) {
    this.productPanel = productPanel;
}

    private void loadCart() {
        model.setRowCount(0);

        for (CartItem item : cart) {
            model.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getPrice() * item.getQuantity()
            });
        }
    }
   
public void refreshCart() {

    model.setRowCount(0);

    double grandTotal = 0;

    for (CartItem item : cart) {

        double itemTotal = item.getPrice() * item.getQuantity();
        grandTotal += itemTotal;

        model.addRow(new Object[]{
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                itemTotal
        });
    }

    totalLabel.setText("Grand Total: ₹" + grandTotal);
}

    private void removeItem() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select item to remove!");
        return;
    }

    int id = (int) model.getValueAt(row, 0);

    // remove from cart list
    cart.removeIf(item -> item.getId() == id);

    refreshCart();

    JOptionPane.showMessageDialog(this, "Item removed from cart!");
}

    private void checkout() {

    if (cart.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Cart is empty!");
        return;
    }

    double total = 0;
    StringBuilder bill = new StringBuilder("===== BILL =====\n");

    for (CartItem item : cart) {
        double itemTotal = item.getPrice() * item.getQuantity();
        total += itemTotal;
        bill.append(item.getName())
            .append(" x ").append(item.getQuantity())
            .append(" = ").append(itemTotal).append("\n");
    }

    bill.append("----------------\nTotal: ").append(total);
    bill.append("\n\nConfirm Checkout?");

    int choice = JOptionPane.showConfirmDialog(
            this,
            bill.toString(),
            "Confirm Purchase",
            JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
        ProductDAO dao = new ProductDAO();

       for (CartItem item : cart) {
        dao.saveOrder(item);
}

        reduceStockInDB();
       
        cart.clear();
        refreshCart();

        if (productPanel != null) {
            productPanel.loadProducts();
        }

        JOptionPane.showMessageDialog(this, "Purchase Successful!");
    }
}

    private void increaseQuantity() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select item first!");
        return;
    }

    int id = (int) model.getValueAt(row, 0);

    for (CartItem item : cart) {

        if (item.getId() == id) {

            int currentQty = item.getQuantity();

            // Get stock from ProductPanel table
            int stock = getStockFromProductPanel(id);

            if (currentQty >= stock) {
                JOptionPane.showMessageDialog(this, "Stock limit reached!");
                return;
            }

            item.setQuantity(currentQty + 1);
            refreshCart();
            return;
        }
    }
}

    private void decreaseQuantity() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select item first!");
        return;
    }

    int id = (int) model.getValueAt(row, 0);

    for (CartItem item : cart) {

        if (item.getId() == id) {

            int currentQty = item.getQuantity();

            if (currentQty > 1) {
                item.setQuantity(currentQty - 1);
            } else {
                cart.remove(item);
            }

            refreshCart();
            return;
        }
    }
}
private int getStockFromProductPanel(int productId) {

    for (int i = 0; i < productPanel.table.getRowCount(); i++) {

        int id = (int) productPanel.table.getValueAt(i, 0);

        if (id == productId) {
            return (int) productPanel.table.getValueAt(i, 3);
        }
    }

    return 0;
}

    
    
    
    
    

    private void reduceStockInDB() {
    try {
        for (CartItem item : cart) {
            String sql = "UPDATE products SET stock = stock - ? WHERE id = ?";
            java.sql.Connection con = shopapp.db.DBConnection.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, item.getQuantity());
            ps.setInt(2, item.getId());
            ps.executeUpdate();
            ps.close();
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error updating stock: " + ex.getMessage());
    }
}


    public void addItem(int id, String name, double price, int stock) {

    for (CartItem item : cart) {
        if (item.getId() == id) {

            if (item.getQuantity() >= stock) {
                JOptionPane.showMessageDialog(this, "Stock limit reached");
                return;
            }

            item.setQuantity(item.getQuantity() + 1);
            refreshCart();

            JOptionPane.showMessageDialog(this, "Quantity updated in cart!");
            return;
        }
    }

    // item not in cart yet
    if (stock > 0) {
        cart.add(new CartItem(id, name, price, 1));
        refreshCart();

        JOptionPane.showMessageDialog(this, "Item added to cart!");
    } else {
        JOptionPane.showMessageDialog(this, "Out of stock");
    }
}


    


}
