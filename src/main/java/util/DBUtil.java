package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBUtil {
    private static DataSource dataSource = new ComboPooledDataSource();

    //从连接池中获取连接
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //释放连接回连接池
    public static void release(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int executeQueryCount(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int ret = resultSet.getInt(1);
        release(connection, statement, resultSet);
        return ret;
    }

    public static ArrayList<Integer> executeQueryCounts(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<Integer> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getInt(1));
        }
        release(connection, statement, resultSet);
        return list;
    }

    public static List<HashMap<String, String>> getHashmap(String sql) {
        Connection connection = getConnection();
        Statement statement = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                HashMap<String, String> hashMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    hashMap.put(rsmd.getColumnLabel(i), resultSet.getString(i));
                }
                list.add(hashMap);
            }
            release(connection, statement, resultSet);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
