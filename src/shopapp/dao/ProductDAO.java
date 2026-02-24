/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shopapp.dao;

import shopapp.db.DBConnection;
import shopapp.model.Product;
import shopapp.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
    public void saveOrder(CartItem item, int userId)
 {

    String sql = "INSERT INTO orders (product_id, product_name, price, quantity, total, user_id) VALUES (?, ?, ?, ?, ?, ?)";


    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, item.getId());
        pst.setString(2, item.getName());
        pst.setDouble(3, item.getPrice());
        pst.setInt(4, item.getQuantity());
        pst.setDouble(5, item.getPrice() * item.getQuantity());
        pst.setInt(6, userId);
        pst.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
