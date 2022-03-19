package servlet.admin;

import dao.ApplyDao;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/admin/applyManage")
public class ApplyManage extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ApplyDao applyDao=ApplyDao.getInstance();
        String action = req.getParameter("action");
        String aid = req.getParameter("aid");
        if("agree".equals(action)){
            int nsnum = Util.nullToZero(req.getParameter("nsnum")) ;
            try {
                applyDao.agree(new Object[]{nsnum,Integer.valueOf(aid)});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if("reject".equals(action)){
            String set=" set state='拒绝' where aid=?";
            try {
                applyDao.update(set,new Object[]{aid});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ArrayList<HashMap<String, String>> maps = null;
        try {
            maps = applyDao.query(" where apply.wid=workday.wid  order by applytime desc", null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("maps",maps);
        req.getRequestDispatcher("applyManage.jsp").forward(req,resp);
    }
}
