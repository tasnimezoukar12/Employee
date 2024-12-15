package com.example.employee.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyConnection {

    private String url = "jdbc:mysql://localhost:3306/employee";
    private String login = "root";
    private String pwd = "";
    private static MyConnection instance;
    private Connection cnx;

    private MyConnection() {
        try {
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connexion établie...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Méthode pour exécuter une requête de comptage
    public int countRecords(String tableName) {
        String sql = "SELECT COUNT(*) AS count FROM " + tableName;
        try (PreparedStatement prepare = cnx.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {
            if (result.next()) {
                return result.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
