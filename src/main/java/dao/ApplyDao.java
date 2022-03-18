package dao;

import bean.Apply;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplyDao {
    /*create table if not exists apply (aid int primary key auto_increment,
                        did int comment '医生id',
                        dname char(16),
                        wid int,
                        reason char(32) comment '原因',
                        applytime datetime  comment '医生这天上午或下午的号源数',
                        request char(8) comment '状态：申请出诊，申请停诊',
                        state char(8) comment '状态：等待处理，同意，拒绝',
                        foreign key(wid) references workday(wid),
                        foreign key(did) references doctor(did));;*/
    public boolean insert(Apply apply) throws SQLException {
        String sql="insert into apply values(null,?,?,?,?,now(),?,'等待处理')";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, apply.getDid());
        preparedStatement.setString(2, apply.getDname());
        preparedStatement.setString(3, apply.getWid());
        preparedStatement.setString(4, apply.getReason());
        preparedStatement.setString(5, apply.getRequest());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<HashMap<String, String>> query(String where, Object[] o) throws SQLException {
        String sql="select request, workday.worktime,ampm, aid,dname,reason,apply.state,applytime from workday,apply "+where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i]);
        }
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
        String sql="update apply "+set;
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
    public void agree(Object[] o) throws SQLException {
        //TODO 取出事务
        String sql="call agreeApply(?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, o[1]);
        preparedStatement.setObject(2, o[2]);
        preparedStatement.executeUpdate(sql);
        DBUtil.release(connection, preparedStatement, null);
    }
}
