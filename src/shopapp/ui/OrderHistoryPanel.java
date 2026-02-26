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
import java.sql.PreparedStatement;


public class OrderHistoryPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    private int userId;

public OrderHistoryPanel(int userId) {
    this.userId = userId;


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
        table.getTableHeader().setResizingAllowed(false);
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

    public void loadOrders() {

    model.setRowCount(0);

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(
                 "SELECT * FROM orders WHERE user_id = ?")) {

        pst.setInt(1, userId);

        ResultSet rs = pst.executeQuery();

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
