package week_02;
import java.sql.*;
public class Lab2SqlTest {
    public static void main(String[] args) throws Exception {
        String password=System.getenv("LAB2_TEST_SQL_PASSWORD");
        if (password==null) throw new IllegalStateException("Set LAB2_TEST_SQL_PASSWORD for local lab integration test.");
        DbConfig.set("127.0.0.1",14333,"LAB2_MaHoaHienDai","lab2_user",password.toCharArray());
        String username="test_"+System.currentTimeMillis();
        char[] userPassword=UserDao.newTicket().toCharArray();
        try {
            System.out.println(DbConfig.test());
            UserDao.register(username,userPassword);
            if (!UserDao.login(username,userPassword)) throw new AssertionError("Login failed");
            if (UserDao.login(username,"incorrect".toCharArray())) throw new AssertionError("Wrong password accepted");
            try { UserDao.register(username,userPassword); throw new AssertionError("Duplicate accepted"); }
            catch (IllegalArgumentException expected) { }
            for (String operation:new String[]{"REGISTER","LOGIN","ENCRYPT","DECRYPT"})
                HistoryDao.insert("AES",operation,username,12,60);
            if (HistoryDao.loadLatest(50).getRowCount()<4) throw new AssertionError("History missing");
            try (Connection c=DbConfig.open(); PreparedStatement ps=c.prepareStatement("SELECT DATALENGTH(salt),DATALENGTH(password_hash) FROM dbo.app_users WHERE username=?")) {
                ps.setString(1,username); try(ResultSet rs=ps.executeQuery()) {
                    if (!rs.next() || rs.getInt(1)!=16 || rs.getInt(2)!=32) throw new AssertionError("Hash storage");
                }
            }
            System.out.println("SQL TEST OK: register, login, wrong password, duplicate, PBKDF2 storage, history insert/read.");
        } finally {
            try(Connection c=DbConfig.open()) {
                for(String sql:new String[]{"DELETE FROM dbo.crypto_history WHERE app_username=?","DELETE FROM dbo.app_users WHERE username=?"})
                    try(PreparedStatement ps=c.prepareStatement(sql)) { ps.setString(1,username); ps.executeUpdate(); }
            }
            java.util.Arrays.fill(userPassword,'\0'); DbConfig.clear();
        }
    }
}
