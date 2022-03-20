package servlet.doctor;

import bean.Apply;
import bean.Doctor;
import dao.ApplyDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/doctor/applyWork")
public class ApplyWork extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String wid = req.getParameter("wid");
        String request = req.getParameter("request");
        String reason = req.getParameter("reason");
        Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");
        Apply apply = new Apply("", doctor.getDid(), doctor.getDname(), wid, reason, "", "", request);
        ApplyDao applyDao = ApplyDao.getInstance();
        String message = "";
        try {
            if (applyDao.insert(apply)) {
                message = "提交申请成功";
            } else {
                message = "提交申请失败，请稍后再试";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("message", message);
        req.getRequestDispatcher("myApply").forward(req, resp);
    }
}
