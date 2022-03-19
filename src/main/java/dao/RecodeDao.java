package dao;

import bean.Recode;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecodeDao {
    private static RecodeDao instance;

    private RecodeDao() {
    }

    public static RecodeDao getInstance() {
        if (instance == null) {
            synchronized (RecodeDao.class) {
                if (instance == null) {
                    instance = new RecodeDao();
                }
            }
        }
        return instance;
    }

    public List<HashMap<String, String>> orderList(String patientid) throws SQLException {
        String sql="select recode.rid,recode.pid,recode.wid,recode.did,recode.serialnumber," +
                "recode.visitdate,recode.visitnoon,recode.visittime,recode.ordertime,recode.state," +
                "doctor.dname,doctor.office,doctor.room,doctor.picpath,doctor.fee" +
                " from recode,doctor" +
                " where recode.pid=?  and doctor.did=recode.did" +
                " order by recode.ordertime desc;";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, patientid);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()){
                HashMap<String, String> hashMap = new HashMap<>();
                for(int i=1; i<= columnCount; i++){
                    hashMap.put(rsmd.getColumnLabel(i), resultSet.getString(i));
                }
                list.add(hashMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, resultSet);
        }
        return list;
    }

    public boolean update(String set, Object[] o) throws SQLException {
        String sql="update recode "+set;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i - 1]);
        }
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Recode> query (String where, Object[] o) throws SQLException {
        String sql ="select * from recode "+where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++){
            preparedStatement.setObject(i, o[i - 1]);
        }
        ResultSet rs = preparedStatement.executeQuery();
        List<Recode> list = new ArrayList<>();
        try {
            while (rs.next()){
                list.add(new Recode(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)
                        ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return list;
    }
}
