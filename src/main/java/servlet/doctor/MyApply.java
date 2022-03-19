package servlet.doctor;

import bean.Doctor;
import bean.WorkDay;
import dao.ApplyDao;
import dao.NumSourceDao;
import dao.WorkDayDao;
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
import java.util.List;

@WebServlet("/doctor/myApply")
public class MyApply extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String  action = req.getParameter("action") ;
        String  aid = req.getParameter("aid") ;
        ApplyDao applyDao=ApplyDao.getInstance();
        if("cancel".equals(action)){
            String set="set state='取消' where aid=?";
            try {
                applyDao.update(set,new Object[]{aid});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");

        String where=" where apply.wid=workday.wid and apply.did=? order by applytime desc";
        ArrayList<HashMap<String, String>> maps = null;
        try {
            maps = applyDao.query(where, new Object[]{doctor.getDid()});
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.setAttribute("maps",maps);
        req.getRequestDispatcher("myApply.jsp").forward(req,resp);
    }
}
