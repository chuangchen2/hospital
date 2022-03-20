package servlet.doctor;

import bean.Doctor;
import bean.WorkDay;
import dao.DoctorDao;
import dao.WorkDayDao;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/doctor/myWork")
public class MyWork extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WorkDayDao workDayDao = WorkDayDao.getInstance();
        String action = Util.nullToString(req.getParameter("action"));
        switch (action) {
            case "offWork":
                String wid = req.getParameter("wid");
                String set = "set state ='停诊' where id=?";
                try {
                    workDayDao.update(set, new Object[]{wid});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");

        //List<WorkDay> workDays= workDayDao.queryWorkday1(doctor.getDid());
        String where = " where did=? order by worktime asc";
        List<WorkDay> workDays = null;
        try {
            workDays = workDayDao.query(where, new Object[]{doctor.getDid()});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("workDays", workDays);
        req.getRequestDispatcher("myWork.jsp").forward(req, resp);
    }
}
