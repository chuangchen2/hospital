package dao;

import bean.Patient;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientDao {
    private static PatientDao instance = null;

    private PatientDao() {
    }

    public static PatientDao getInstance() {
        if (instance == null) {
            synchronized (PatientDao.class) {
                if (instance == null) {
                    instance = new PatientDao();
                }
            }
        }
        return instance;
    }

    public boolean update(String set, Object[] o) throws SQLException {
        String sql="update patient "+set;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i - 1]);
        }
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean insert(Patient patient) throws SQLException {
        String sql="insert into patient values(null,?,?,?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, patient.getEmail());
        preparedStatement.setString(2, patient.getPassword());
        preparedStatement.setString(3, patient.getName());
        preparedStatement.setString(4, patient.getIntegrity());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Patient> query(String clounm, String where) throws SQLException {
        List<Patient> lists = new ArrayList<>();
        String sql="select * from patient where "+clounm+"=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, where);
        ResultSet rs = preparedStatement.executeQuery();
        try {
            while (rs.next()){
                lists.add(new Patient(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return lists;
    }

    public ArrayList<HashMap<String, String>> patientList(String did) throws SQLException {
         /*serialnumber int comment '就诊序号',
                visitdate  date comment '就诊日期',
                visitnoon  char(4) comment '就诊上午或下午',
                visittime  time comment '就诊时间',*/
        String sql="select patient.pid, patient.name as pname,recode.ordertime,recode.state, recode.rid,recode.serialnumber,recode.visitdate,recode.visitnoon,recode.visittime " +
                "from recode,patient where " +
                "recode.did=? and recode.pid=patient.pid " +
                "order by ordertime desc";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, did);
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
}
