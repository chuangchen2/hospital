package dao;

import bean.Room;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDao {
    private static RoomDao instance;

    private RoomDao() {
    }

    public static RoomDao getInstance() {
        if (instance == null) {
            synchronized (RoomDao.class) {
                if (instance == null) {
                    instance = new RoomDao();
                }
            }
        }
        return instance;
    }
    /*private String id;
    private String officename;
    private String roomname;
    private int doctornum;*/
    public boolean insert(Room room) throws SQLException {
        Object[] o=new Object[]{
                room.getOfficename(),
                room.getRoomname(),
                room.getDoctornum()};
        String sql="insert into room values(null,?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, room.getOfficename());
        preparedStatement.setString(2, room.getRoomname());
        preparedStatement.setObject(3, room.getDoctornum());
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Room> query(String clounm, String where) throws SQLException {
        List<Room> offices=new ArrayList<>();
        String sql="select * from room where "+clounm+" like ? order by doctornum desc";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + where + "%");
        ResultSet rs = preparedStatement.executeQuery();
        try {
            while (rs.next()){
                offices.add(new Room(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.release(connection, preparedStatement, rs);
        }
        return offices;
    }

    public boolean delete(String roomname) throws SQLException {
        String sql =" delete from room where roomname=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, roomname);
        int i = preparedStatement.executeUpdate();
        DBUtil.release(connection, preparedStatement, null);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }
}
