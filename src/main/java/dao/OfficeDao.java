package dao;

import bean.Office;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfficeDao {
    private static OfficeDao instance;

    private OfficeDao() {
    }

    public static OfficeDao getInstance() {
        if (instance == null) {
            synchronized (OfficeDao.class) {
                if (instance == null) {
                    instance = new OfficeDao();
                }
            }
        }
        return instance;
    }

    public boolean insert(Office office) throws SQLException {
        Object[] o=new Object[]{office.getOfficename(),
                office.getDescription(),
                office.getDoctornum()};
        String sql="insert into office values(?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, office.getOfficename());
        preparedStatement.setString(2, office.getDescription());
        preparedStatement.setObject(3, office.getDoctornum());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Office> query(String clounm, String where,String limit) throws SQLException {
        List<Office> offices=new ArrayList<>();
        String sql="select * from office where "+clounm+" like ? order by doctornum desc "+limit;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + where + "%");
        ResultSet rs = preparedStatement.executeQuery();
        try {
            while (rs.next()){
                offices.add(new Office(rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return offices;
    }

    public int getOfficeCount(String clounm, String where) throws SQLException {
        String sql="select count(*) from office where "+clounm+" like ? ";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + where + "%");
        ResultSet rs = preparedStatement.executeQuery();
        int count = 0;
        try {
            rs.next();
            String string = rs.getString(1);
            count = Integer.parseInt(string);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return count;
    }
}
