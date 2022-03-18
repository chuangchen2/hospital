package dao;

import bean.Admin;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    private static AdminDao instance = null;

    private AdminDao() {
    }

    public static AdminDao getInstance() {
        if (instance == null) {
            synchronized (AdminDao.class) {
                if (instance == null) {
                    instance = new AdminDao();
                }
            }
        }
        return instance;
    }

    public boolean insertAdmin(Admin admin) throws SQLException {
        String sql="insert into admin valuse(?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, admin.getAccount());
        preparedStatement.setString(2, admin.getPassword());
        preparedStatement.setString(3, admin.getName());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean updateAdmin(String where ,Object[] o) throws SQLException {
        String sql="update admin "+where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setString(i, (String)o[i]);
        }
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Admin> getAdmin(String account) throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String sql="select * from admin where account=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account);
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()){
                admins.add(new Admin(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, resultSet);
        }
        return admins;
    }
}
