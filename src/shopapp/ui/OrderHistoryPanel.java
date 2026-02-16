/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.ui;

import shopapp.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderHistoryPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    public OrderHistoryPanel() {

        setLayout(new BorderLayout());

       model = new DefaultTableModel() {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;   // no cell editable
    }
};

        model.addColumn("Order ID");
        model.addColumn("Product ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Total");
        model.addColumn("Date");

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(180, 180, 180));
        table.setSelectionBackground(new Color(184, 207, 229));

        JButton refreshBtn = new JButton("Refresh Orders");
        refreshBtn.addActionListener(e -> loadOrders());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(refreshBtn, BorderLayout.SOUTH);

        loadOrders();
    }

    private void loadOrders() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM orders")) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
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
