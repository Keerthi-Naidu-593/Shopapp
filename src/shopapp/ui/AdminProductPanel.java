/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;
import shopapp.db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class AdminProductPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public AdminProductPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Price", "Stock"}, 0){

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // 🔥 disables editing
    }
};

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadProducts();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        buttonPanel.add(addBtn);     
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);
        
    addBtn.addActionListener(e -> {

    String name = JOptionPane.showInputDialog(this, "Enter product name:");
    if (name == null || name.trim().isEmpty()) return;

    String priceStr = JOptionPane.showInputDialog(this, "Enter price:");
    if (priceStr == null || priceStr.trim().isEmpty()) return;

    String stockStr = JOptionPane.showInputDialog(this, "Enter stock:");
    if (stockStr == null || stockStr.trim().isEmpty()) return;

    try (Connection con = DBConnection.getConnection()) {

        String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, name);
        pst.setDouble(2, Double.parseDouble(priceStr));
        pst.setInt(3, Integer.parseInt(stockStr));

        pst.executeUpdate();

        loadProducts();

        JOptionPane.showMessageDialog(this, "Product added successfully!");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Invalid input!");
    }
});    
        
    deleteBtn.addActionListener(e -> {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a product first.");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this product?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) return;

    int id = (int) model.getValueAt(row, 0);

    try (Connection con = DBConnection.getConnection()) {

        PreparedStatement pst =
                con.prepareStatement("DELETE FROM products WHERE id=?");
        pst.setInt(1, id);
        pst.executeUpdate();

        loadProducts();

        JOptionPane.showMessageDialog(this, "Product deleted successfully!");

    } catch (Exception ex) {
        ex.printStackTrace();
    }
});
    
     updateBtn.addActionListener(e -> {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a product first.");
        return;
    }

    int id = (int) model.getValueAt(row, 0);

    String[] options = {"Name", "Price", "Stock"};
    String choice = (String) JOptionPane.showInputDialog(
            this,
            "What do you want to update?",
            "Update Product",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == null) return;

    try (Connection con = DBConnection.getConnection()) {

        if (choice.equals("Name")) {

            String newName = JOptionPane.showInputDialog("Enter new name:");

            PreparedStatement pst =
                    con.prepareStatement("UPDATE products SET name=? WHERE id=?");

            pst.setString(1, newName);
            pst.setInt(2, id);
            pst.executeUpdate();

        } else if (choice.equals("Price")) {

            String newPrice = JOptionPane.showInputDialog("Enter new price:");

            PreparedStatement pst =
                    con.prepareStatement("UPDATE products SET price=? WHERE id=?");

            pst.setDouble(1, Double.parseDouble(newPrice));
            pst.setInt(2, id);
            pst.executeUpdate();

        } else if (choice.equals("Stock")) {

            String newStock = JOptionPane.showInputDialog("Enter new stock:");

            PreparedStatement pst =
                    con.prepareStatement("UPDATE products SET stock=? WHERE id=?");

            pst.setInt(1, Integer.parseInt(newStock));
            pst.setInt(2, id);
            pst.executeUpdate();
        }

        loadProducts();

        JOptionPane.showMessageDialog(this, "Product updated successfully!");

    } catch (Exception ex) {
        ex.printStackTrace();
    }
});

        
    }
    private void loadProducts() {

    model.setRowCount(0);

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM products")) {

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
}