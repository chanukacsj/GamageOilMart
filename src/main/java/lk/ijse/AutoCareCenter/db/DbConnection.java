package lk.ijse.AutoCareCenter.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {

        // ✅ Ensure database folder exists
        File dbDir = new File("C:/Users/CHANUKA/Documents/intellig/GamageOilMart/database");
        if (!dbDir.exists()) dbDir.mkdirs();

        String url = "jdbc:sqlite:C:/Users/CHANUKA/Documents/intellig/GamageOilMart/database/OilMart.db";
        connection = DriverManager.getConnection(url);
        System.out.println("✅ Connected to SQLite database successfully!");

        // ✅ Auto-create users table
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL)";
        PreparedStatement stmt = connection.prepareStatement(usersTable);
        stmt.executeUpdate();
        System.out.println("✅ Users table created (if not exists)");
    }

    public static DbConnection getInstance() throws SQLException {
        if (dbConnection == null) {
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
