package servlet.admin;

import bean.Office;
import bean.Room;
import dao.OfficeDao;
import dao.RoomDao;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/officeDetail")
public class OfficeDetail extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String office = Util.nullToString(req.getParameter("office"));
        String action =req.getParameter("action");

        OfficeDao officeDao=OfficeDao.getInstance();
        List<Office> list = null;
        try {
            list = officeDao.query("officename", office, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RoomDao roomDao=RoomDao.getInstance();
        String message = null;
        String roomname =Util.nullToString(req.getParameter("roomname"));
        if("add".equals(action)){

            List<Room> rooms = null;
            try {
                rooms = roomDao.query("roomname", roomname);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(rooms.size()==0){
                Room room = new Room("",office, roomname, 0);
                try {
                    if(roomDao.insert(room)){
                        message=roomname+"添加成功！";
                    }else {
                        message=roomname+"添加失败！";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                message=roomname+"已存在！";
            }
        }else if("delete".equals(action)){
            try {
                if(roomDao.delete(roomname)){
                    message="删除成功";
                }else {
                    message="删除失败";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        req.setAttribute("message",message);
        List<Room> list1 = null;
        try {
            list1 = roomDao.query("officename", office);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("office",list.get(0));
        req.setAttribute("list1",list1);
        req.getRequestDispatcher("officeDetail.jsp").forward(req,resp);
    }
}
