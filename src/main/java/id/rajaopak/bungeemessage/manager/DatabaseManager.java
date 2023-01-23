package id.rajaopak.bungeemessage.manager;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private JdbcConnectionPool pool;

    public void connect() {
        String url = "jdbc:h2:file:database_name";
        String username = "username";
        String password = "password";
        pool = JdbcConnectionPool.create(url, username, password);
    }

    public void close() {
        pool.dispose();
    }

    public void executeUpdate(String sql) {
        try (Connection conn = pool.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        try (Connection conn = pool.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                return stmt.executeQuery(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
