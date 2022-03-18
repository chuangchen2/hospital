package dao;

import bean.Integrity;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntegrityDao {
    /*create table if not exists integrity(iid int primary key auto_increment,
                        pid int comment '病人id',
                        dname char(16) comment '医生名字',
                        office char(16) comment '科室名字',
                        time  datetime comment '诚信记录的时间',
                        reason char(32) comment '原因',
                        score tinyint comment '分值',
                        foreign key(pid) references patient(pid));;*/

    public boolean insert(Integrity integrity) throws SQLException {
        String sql="insert into integrity values(null,?,?,?,now(),?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, integrity.getPid());
        preparedStatement.setString(2, integrity.getDname());
        preparedStatement.setString(3, integrity.getOffice());
        preparedStatement.setString(4, integrity.getReason());
        preparedStatement.setString(5, integrity.getScore());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Integrity> query(String where,Object[] o) throws SQLException {
        List<Integrity> list=new ArrayList<>();
        String sql="select * from integrity "+where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i]);
        }
        ResultSet rs = preparedStatement.executeQuery();
        try {
            while (rs.next()){
                list.add(new Integrity(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return list;
    }
}
