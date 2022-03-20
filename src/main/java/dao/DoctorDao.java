package dao;

import bean.Doctor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    private static DoctorDao instance = null;
    private final static Logger logger = LogManager.getLogger(DoctorDao.class);

    private DoctorDao() {
    }

    public static DoctorDao getInstance() {
        if (instance == null) {
            synchronized (DoctorDao.class) {
                if (instance == null) {
                    instance = new DoctorDao();
                }
            }
        }
        return instance;
    }

    public boolean insert(Doctor doctor) throws SQLException {
        String sql = "insert into doctor values(null,?,?,?,?,?,?,?,?,?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, doctor.getAccount());
        preparedStatement.setString(2, doctor.getPassword());
        preparedStatement.setString(3, doctor.getDname());
        preparedStatement.setString(4, doctor.getFee());
        preparedStatement.setString(5, doctor.getGender());
        preparedStatement.setString(6, doctor.getAge());
        preparedStatement.setString(7, doctor.getOffice());
        preparedStatement.setString(8, doctor.getRoom());
        preparedStatement.setString(9, doctor.getCareer());
        preparedStatement.setString(10, doctor.getDescription());
        preparedStatement.setString(11, doctor.getPicpath());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Doctor queryFromAccount(String account) throws SQLException {
        String suffix = "where account=?";
        List<Doctor> query = query(suffix, new Object[]{account});
        if (query.size() > 0) {
            return query.get(0);
        } else {
            return null;
        }
    }

    public List<Doctor> query(String where, Object[] o) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "select * from doctor " + where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i - 1]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        try {
            while (resultSet.next()) {
                doctors.add(new Doctor(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getString(10),
                        resultSet.getString(11),
                        resultSet.getString(12)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(connection, preparedStatement, resultSet);
        }
        return doctors;
    }

    public int getDoctorCount(String where, Object[] o) throws SQLException {
        String sql = "select count(*) from doctor " + where;
        Connection connection = DBUtil.getConnection();
        logger.debug(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i - 1]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        String string = "";
        if (resultSet.next()) {
            string = resultSet.getString(1);
        }
        DBUtil.release(connection, preparedStatement, resultSet);
        return Integer.parseInt(string);
    }

    public boolean update(String set, Object[] o) throws SQLException {
        String sql = "update doctor " + set;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i]);
        }
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }
}
