package lk.ijse.AutoCareCenter.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");  // <<< ADD THIS
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // ✅ Ensure database folder exists
        // 📍 EXE location
        String basePath = System.getProperty("user.dir");

// 📁 database folder (same place as exe)
        File dbDir = new File(basePath + File.separator + "database");
        if (!dbDir.exists()) dbDir.mkdirs();

// 🗄 SQLite DB path
        String dbPath = dbDir.getAbsolutePath() + File.separator + "OilMart.db";
        String url = "jdbc:sqlite:" + dbPath;

        connection = DriverManager.getConnection(url);
        System.out.println("✅ Connected to SQLite: " + dbPath);

        // ✅ Auto-create users table
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL)";
        connection.prepareStatement(usersTable).executeUpdate();

        // ✅ Auto-create suppliers table
        String supplierTable = "CREATE TABLE IF NOT EXISTS suppliers (" +
                "supId TEXT PRIMARY KEY, " +
                "supName TEXT NOT NULL, " +
                "contact TEXT NOT NULL, " +
                "address TEXT NOT NULL)";
        connection.prepareStatement(supplierTable).executeUpdate();

        // ✅ Auto-create material_details table
        String materialDetailTable = "CREATE TABLE IF NOT EXISTS material_details (" +
                "code TEXT PRIMARY KEY, " +
                "supId TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "brand TEXT, " +
                "unitPrice REAL NOT NULL, " +
                "qtyOnHand INTEGER NOT NULL, " +
                "addedDate TEXT DEFAULT (datetime('now')), " +
                "status TEXT DEFAULT 'Active', " +
                "barcode TEXT UNIQUE,"+
                "FOREIGN KEY (supId) REFERENCES suppliers(supId))";
        connection.prepareStatement(materialDetailTable).executeUpdate();

        // ✅ Orders table
        String ordersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "orderId TEXT PRIMARY KEY, " +
                "date TEXT NOT NULL)";
        connection.prepareStatement(ordersTable).executeUpdate();

// ✅ Order Details table
        String orderDetailsTable = "CREATE TABLE IF NOT EXISTS order_details (" +
                "orderId TEXT NOT NULL, " +
                "code TEXT NOT NULL, " +
                "qty INTEGER NOT NULL, " +
                "unitPrice REAL NOT NULL, " +
                "service_charge REAL NOT NULL, " +
                "total REAL NOT NULL, " +
                "discount REAL DEFAULT 0, " +
                "FOREIGN KEY (orderId) REFERENCES orders(orderId), " +
                "FOREIGN KEY (code) REFERENCES material_details(code))";

        connection.prepareStatement(orderDetailsTable).executeUpdate();


        // ✅ Payment table
        String paymentTable = "CREATE TABLE IF NOT EXISTS payment (" +
                "id TEXT PRIMARY KEY, " +
                "orderId TEXT NOT NULL, " +
                "code TEXT NOT NULL, " +
                "qty INTEGER NOT NULL, " +
                "unitPrice REAL NOT NULL, " +
                "service_charge REAL NOT NULL, " +
                "total REAL NOT NULL, " +
                "description TEXT, " +
                "date TEXT," +
                "discount REAL DEFAULT 0, " +
                "FOREIGN KEY (orderId) REFERENCES orders(orderId), " +
                "FOREIGN KEY (code) REFERENCES material_details(code))";

        connection.prepareStatement(paymentTable).executeUpdate();

        // ✅ Loan table
        String loanTable = "CREATE TABLE IF NOT EXISTS loan (" +
                "loanId TEXT PRIMARY KEY, " +
                "orderId TEXT NOT NULL, " +
                "customerName TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "total REAL NOT NULL, " +
                "paid REAL NOT NULL, " +
                "remaining REAL NOT NULL, " +
                "nextDue TEXT, " +
                "status TEXT, " +
                "date TEXT)";
        connection.prepareStatement(loanTable).executeUpdate();
        // ✅ Loan Payment table
        String loanPaymentTable = "CREATE TABLE IF NOT EXISTS loan_payment (" +
                "paymentId TEXT PRIMARY KEY, " +
                "loanId TEXT NOT NULL, " +
                "amount REAL NOT NULL, " +
                "date TEXT NOT NULL, " +
                "FOREIGN KEY (loanId) REFERENCES loan(loanId))";
        connection.prepareStatement(loanPaymentTable).executeUpdate();

        // ✅ Cheque Payment table
        String chequePaymentTable =
                "CREATE TABLE IF NOT EXISTS cheque_payment (" +
                        "chequeId TEXT PRIMARY KEY, " +
                        "paymentId TEXT NOT NULL, " +
                        "customerName TEXT NOT NULL, " +
                        "customerPhone TEXT NOT NULL, " +
                        "chequeNo TEXT NOT NULL, " +
                        "bankName TEXT NOT NULL, " +
                        "branch TEXT NOT NULL, " +
                        "chequeDate TEXT NOT NULL, " +
                        "amount REAL NOT NULL, " +
                        "status TEXT DEFAULT 'PENDING', " +
                        "FOREIGN KEY (paymentId) REFERENCES payment(id))";
        connection.prepareStatement(chequePaymentTable).executeUpdate();

        System.out.println("✅ Tables created (if not exists)");
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
