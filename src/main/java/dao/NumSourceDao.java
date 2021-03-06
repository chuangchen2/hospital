package dao;

import bean.NumSource;
import bean.WorkDay;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NumSourceDao {
    private static NumSourceDao instance;

    private NumSourceDao() {
    }

    public static NumSourceDao getInstance() {
        if (instance == null) {
            synchronized (NumSourceDao.class) {
                if (instance == null) {
                    instance = new NumSourceDao();
                }
            }
        }
        return instance;
    }

    /*public boolean insert(NumSource numSource){
            Object[] o=new Object[]{numSource.getWorkdayid(),
                    office.getDescription(),
                    office.getDoctornum()};
            String sql="insert into numsource (id ,workdayid ,serialnumber ,nstime ,state) select null,id as workdayid, 1,'8:30','可预约' from workday where worktime=? and ampm=? and doctorid=?";
            return DBUtil.executeUpdate(sql,o);
        }
        public static void a(){
            Connection conn = DBUtil.createConn();
            try {
                Statement statement=conn.createStatement();

                statement.addBatch("");
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*//*上午8.30-12.30 4*60=240
    下午1.30-5.30 4
    create table if not exists numsource (id int primary key auto_increment,
                        workdayid int comment '工作日id',
                        serialnumber int comment '就诊序号',
                        nstime  time comment '号源时间',
                        state char(8) comment '状态：已被预约，可预约',
                        foreign key(workdayid) references workday(id));*/
    /*
        预约成功操作
     */
    /*create table if not exists recode (rid int primary key auto_increment,
                                       pid int comment '病人id',
                                       wid int comment '工作日id',
                                       did int comment '医生id',
                                       serialnumber int comment '就诊序号',
                                       visitdate  date comment '就诊日期',
                                       visitnoon  date comment '就诊上午或下午',
                                       visittime  time comment '就诊时间',
                                       ordertime  datetime comment '预约记录的时间',
                                       state char(8) comment '预约状态：成功，取消，完成，爽约',
    foreign key(pid) references patient(pid),
    foreign key(did) references doctor(did),
    foreign key(wid) references workday(wid));*/
    public boolean order(String pid, String did, NumSource numSource) throws SQLException {//病人id,号源id
        String sql = "insert into recode values(null,?,?,?,?,?,?,?,now(),'成功')";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, pid);
        preparedStatement.setString(2, numSource.getState());
        preparedStatement.setString(3, did);
        preparedStatement.setString(4, numSource.getSerialnumber());
        preparedStatement.setString(5, numSource.getVisitdate());
        preparedStatement.setString(6, numSource.getVisitnoon());
        preparedStatement.setString(7, numSource.getVisittime());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
        /*Connection conn = DBUtil.createConn();
        Statement statement=null;
        String sql="update numsource set state='已被预约' where id='"+nid+"'" ;
        try {
            statement= conn.createStatement();
            statement.addBatch(sql);
            sql="insert into recode values(null,'"+pid+"','"+nid+"',now(),'成功')";
            statement.addBatch(sql);
            sql="update workday set orderednum=orderednum+1 where id=(select workdayid from numsource where id='"+nid+"')";
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                statement.close();conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }

    public List<NumSource> query(String where, Object[] o) throws SQLException {
        WorkDayDao workDayDao = WorkDayDao.getInstance();
        List<WorkDay> workDays = workDayDao.query(where, o);
        WorkDay workDay = workDays.get(0);
        int nsnum = Integer.valueOf(workDay.getNsnum());
        List<NumSource> list = new ArrayList<>();
        String sql = "select id,workdayid,serialnumber,date_format(nstime,'%H:%i'),state from numsource  " + where;
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 1; i <= o.length; i++) {
            preparedStatement.setObject(i, o[i - 1]);
        }
        ResultSet rs = preparedStatement.executeQuery();
        try {
            while (rs.next()) {
                list.add(new NumSource(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return list;
    }

    public boolean update(String where, Object[] o) throws SQLException {
        String sql = "update  numsource " + where;
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
}
