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
/**
 *
 * @author KEERTHI
 */
public class AdminOrderPanel extends JPanel{
      private JTable table;
    private DefaultTableModel model;

    public AdminOrderPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(
        new String[]{"Order ID", "User ID", "Product", "Quantity", "Total", "Date"}, 0) {

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
        
        loadOrders();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadOrders() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM orders")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total"),
                        rs.getTimestamp("order_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
