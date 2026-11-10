package week_01;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public final class DbConfig {

    private static String server;
    private static int port;
    private static String database;
    private static String user;
    private static char[] password;

    private DbConfig() {
    }

    // Ghi nhớ thông tin nhập từ form kết nối SQL.
    public static synchronized void set(
            String newServer,
            int newPort,
            String newDatabase,
            String newUser,
            char[] newPassword) {

        if (newServer.isBlank()
                || newDatabase.isBlank()
                || newUser.isBlank()) {
            throw new IllegalArgumentException(
                    "Server, Database và User không được để trống.");
        }

        if (newPort < 1 || newPort > 65535) {
            throw new IllegalArgumentException(
                    "Port phải nằm trong khoảng 1 - 65535.");
        }

        if (password != null) {
            Arrays.fill(password, '\0');
        }

        server = newServer.trim();
        port = newPort;
        database = newDatabase.trim();
        user = newUser.trim();
        password = newPassword.clone();
    }

    // Xóa cấu hình khi kết nối thất bại.
    public static synchronized void clear() {
        if (password != null) {
            Arrays.fill(password, '\0');
        }

        server = null;
        database = null;
        user = null;
        password = null;
        port = 0;
    }

    // Cho biết đã có cấu hình kết nối hay chưa.
    public static synchronized boolean isReady() {
        return server != null;
    }

    // Mở một kết nối mới.
    public static synchronized Connection open() throws SQLException {
        if (!isReady()) {
            throw new SQLException(
                    "Chưa cấu hình SQL Server. "
                    + "Hãy mở form Kết nối SQL Server và bấm Test Connection.");
        }

        String url = "jdbc:sqlserver://" + server + ":" + port
                + ";databaseName=" + database
                + ";encrypt=true;trustServerCertificate=true;loginTimeout=5";

        return DriverManager.getConnection(
                url, user, new String(password));
    }

    // Thử kết nối và trả về thông tin SQL Server.
    public static String test() throws SQLException {
        try (Connection con = open()) {
            DatabaseMetaData md = con.getMetaData();

            return "Kết nối thành công: "
                    + md.getDatabaseProductName() + " "
                    + md.getDatabaseProductVersion()
                    + " - database " + con.getCatalog();
        }
    }
}