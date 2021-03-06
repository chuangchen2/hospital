package servlet.patient;

import bean.Doctor;
import bean.WorkDay;
import dao.DoctorDao;
import dao.OfficeDao;
import dao.WorkDayDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/showWeek")
public class ShowWorkday extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String doctorid = req.getParameter("did");
        if (doctorid == null) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {
            DoctorDao doctorDao = DoctorDao.getInstance();
            List<Doctor> doctors = null;
            try {
                doctors = doctorDao.query("where did=? ", new Object[]{doctorid});
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (doctors.size() > 0) {
                WorkDayDao workDayDao = WorkDayDao.getInstance();
                String where = " where did=?";
                List<WorkDay> workDays = null;
                try {
                    workDays = workDayDao.query(where, new Object[]{doctorid});
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                req.setAttribute("workDays", workDays);
                req.setAttribute("doctor", doctors.get(0));
                req.getRequestDispatcher("doctorInfo.jsp").forward(req, resp);
            } else {
                resp.getWriter().write("没有这个医生");
            }
        }
    }
}
